-module(hbq).
-import(werkzeug, [logging/2, timeMilliSecond/0, get_config_value/2, to_String/1, timeMilliSecond/0]).
-import(dlq, [push2DLQ/3, expectedNr/1, initDLQ/2, deliverMSG/4, getSize/1]).
-export([start/0, loop/0]).

-define(SEVERCFG, "server.cfg").
-define(LOGFILE, lists:flatten(io_lib:format("~p.log", [node()]))).

%Die Nummern in den Kommentaren beziehen sich auf:
%Das Diagramm "HBQ-DLQ Algorithmus"

%HBQ ist eigener Prozss. Dies ist der Einstiegspunkt.
start() ->
	{ok, ConfigListe} = file:consult(?SEVERCFG),
	{ok, HBQname} = get_config_value(hbqname, ConfigListe),
	HBQPID = spawn(hbq, loop, []),
	erlang:register(HBQname, HBQPID),
	logging(?LOGFILE, lists:flatten(io_lib:format("HBQ>>> ~p geöffnet...~n", [?SEVERCFG]))).		
	
loop() ->
	receive 
		{Pid, {request, initHBQ}} ->
			{ok, ConfigListe} = file:consult(?SEVERCFG),
			{ok, DLQlimit} = get_config_value(dlqlimit, ConfigListe),
			loop(Pid, {0,[]}, DLQlimit, init)
	end.

loop(Pid, MSGs, DLQlimit, init) ->
	logging(?LOGFILE, lists:flatten(io_lib:format("HBQ>>> initialisiert worden von " ++ to_String(Pid) ++ ". ~n", []))),
	{queue, DLQ} = initDLQ(DLQlimit, ?LOGFILE),
	Pid ! {reply, ok},
	loop(MSGs, DLQ, DLQlimit, running);

loop(HBQ, DLQ, DLQlimit, running) ->
	receive
		%1.) Nachricht angekommen. Nachricht in HBQ eintragen.
		{Pid, {request,pushHBQ,[NNr,Msg,TSclientout]}} ->
			{Size, _HBQnew} = HBQ,
			%Schritt 6 aus dem Aktivitätsdiagramm -> Zeitstempel hinzufuegen
			HBQnn = insertHBQ(HBQ, [NNr, Msg, TSclientout, timeMilliSecond()]),
			logging(?LOGFILE, lists:flatten(io_lib:format("HBQ>>> Nachricht ~p in HBQ eingefuegt. ~n", [NNr]))),
			%2.) Entsteht eine Lücke? Ja und Nein innerhalb der Funktion.
			{HBQn, DLQn} = hbqdlqAlg({Size + 1, HBQnn}, DLQ, DLQlimit, expectedNr(DLQ), NNr),
			Pid ! {reply, ok},
			loop(HBQn, DLQn, DLQlimit, running);
		%Fordert Senden der Nachricht bei der DLQ an.
		{Pid, {request,deliverMSG,NNr,ToClient}} ->
			{reply, SendNNr} = deliverMSG(NNr,ToClient,DLQ, ?LOGFILE),
			Pid ! {reply, SendNNr},
			loop(HBQ, DLQ, DLQlimit, running);
		%Terminierungsanfrage der HBQ von Seiten des Servers.
		{Pid, {request,dellHBQ}} ->
			{HBQsize, HBQn} = HBQ,
			DLQsize = getSize(DLQ),
			logging(?LOGFILE, lists:flatten(io_lib:format("HBQ>>> Downtime: ~p| von HBQ und DLQ ~p; Anzahl Restnachrichten HBQ/DLQ:~p/~p (~p). ~n", [timeMilliSecond(), self(), HBQsize, DLQsize, (HBQsize + DLQsize)]))),
			Pid ! {reply, ok};
		Any ->
				logging(?LOGFILE, lists:flatten(io_lib:format("HBQ>>> !!!UNDEFINIERTE NACHRICHT ERHALTEN!!!. ~n", []))),
			loop(HBQ, DLQ, DLQlimit, running)
	end.

insertHBQ({_Size, []}, MSG) ->
	[MSG];
insertHBQ({Size, [[NNRn, Msgn, TSclientoutn, TShbqinn] | Msgs]}, [NNr, Msg, TSclientout, TShbqin]) when NNRn < NNr->
    [[NNRn, Msgn, TSclientoutn, TShbqinn]]  ++ insertHBQ({Size, Msgs}, [NNr, Msg, TSclientout, TShbqin]);
insertHBQ({_Size, [[NNRn, Msgn, TSclientoutn, TShbqinn] | Msgs]}, [NNr, Msg, TSclientout, TShbqin]) ->
	[[NNr, Msg, TSclientout, TShbqin],[NNRn, Msgn, TSclientoutn, TShbqinn]] ++ Msgs.

%2.) Entsteht eine Lücke? Nein-Zweig.
hbqdlqAlg({Size, [ Msg | Msgn]}, DLQ, _DLQlimit, {reply, ExpNr}, NNr) when ExpNr == NNr -> 
	%3.) Nachricht in DLQ übertragen. Ja-Nein Entscheidung in DLQ realisiert.
	%Schritte 7. - 8. im DLQ-Modul.
	{queue, DLQn} = push2DLQ(Msg,DLQ, ?LOGFILE),
	{{Size - 1, Msgn}, DLQn};
%2.) Entsteht eine Lücke? Ja-Zweig.
%4.) Ist die Größe der HBQ kleiner als 2/3 der DLQ-Größe. Ja-Zweig.
hbqdlqAlg({Size, MSGs}, DLQ, DLQlimit, {reply, _ExpNr}, _NNr) when Size < (2 * (DLQlimit div 3)) -> 
	{{Size, MSGs}, DLQ};

%4.) Ist die Größe der HBQ kleiner als 2/3 der DLQ-Größe. Nein-Zweig.
hbqdlqAlg(HBQ, DLQ, _DLQlimit, {reply, ExpNr}, _NNr) -> 
	{_Size, [[NNRn, _Msg, TSclientout, _TShbqin] | _MSGs]} = HBQ,
	%5.) Lücke mit Fehlernachricht schließen.
	{queue, DLQn} = push2DLQ([ExpNr, lists:flatten(io_lib:format(
	"Fehlernachricht fuer Nachrichten ~p bis ~p generiert um " ++ timeMilliSecond(), [ExpNr, NNRn - 1])),
	TSclientout, erlang:now()], DLQ, ?LOGFILE),
	{Size, HBQn} = HBQ,
	errorHandling({Size, HBQn}, DLQn, NNRn, Size).
	
%6.) Nachrichten der HBQ in die DLQ übertragen bis eine neue Lücke entdeckt wurde.	
errorHandling({Size, [[NNRn, Msgn, TSclientoutn, TShbqinn] | MSGs]}, DLQ, LastNNr, SizeOld) when (NNRn - LastNNr) =< 1 ->
	{queue, DLQn} = push2DLQ([NNRn, Msgn,TSclientoutn,TShbqinn],DLQ, ?LOGFILE),
	errorHandling({Size - 1, MSGs},  DLQn, NNRn, SizeOld);
errorHandling({Size, HBQ}, DLQ, _LastNNr, _SizeOld) when Size == 0->
logging(?LOGFILE, lists:flatten(io_lib:format("HBQ>>> HBQ wurde komplett in die DLQ uebertragen. ~n", []))),
	{{Size, HBQ}, DLQ};
errorHandling({Size, HBQ}, DLQ, _LastNNr, SizeOld) ->
	Diff = SizeOld - Size,
logging(?LOGFILE, lists:flatten(io_lib:format("HBQ>>> ~p von ~p Nachrichten in die DLQ uebertragen. ~n", [Diff, SizeOld]))),
	{{Size, HBQ}, DLQ}.
