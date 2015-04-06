-module(hbq).
-import(werkzeug, [logging/2, timeMilliSecond/0, get_config_value/2, to_String/1, timeMilliSecond/0]).
-import(dlq, [push2DLQ/3, expectedNr/1, initDLQ/2, deliverMSG/4]).
-export([start/0, loop/0]).
-define(LOGFILE, lists:flatten(io_lib:format("~p.log", [node()]))).

start() ->
	{ok, ConfigListe} = file:consult("server.cfg"),
	{ok, HBQname} = get_config_value(hbqname, ConfigListe),
	
	%----neu beginn----
	HBQPID = spawn(hbq, loop, []),
	erlang:register(HBQname, HBQPID),
	%----neu ende----
	logging(?LOGFILE, lists:flatten(io_lib:format("HBQ>>> server.cfg geöffnet...~n", []))).		
	%loop().
	
loop() ->
	receive 
		{Pid, {request, initHBQ}} ->
			{ok, ConfigListe} = file:consult("server.cfg"),
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
		{Pid, {request,pushHBQ,[NNr,Msg,TSclientout]}} ->
			{Size, _HBQnew} = HBQ,
			HBQnn = insertHBQ(HBQ, [NNr, Msg, TSclientout, timeMilliSecond()]),
			p("NNr: ~p expNr: ~p~n",[NNr, expectedNr(DLQ)]),
			{HBQn, DLQn} = hbqdlqAlg({Size + 1, HBQnn}, DLQ, DLQlimit, expectedNr(DLQ), NNr),
			%io:format("dlq: ~p~n", [DLQn]),
			Pid ! {reply, ok},
			io:format("hbq: ~p~n", [HBQn]),
			loop(HBQn, DLQn, DLQlimit, running);
		{Pid, {request,deliverMSG,NNr,ToClient}} ->
			%io:format("dlq: ~p~n", [DLQ]),
			
			%-----alt auskommentiert-----
			%dlq:deliverMSG(NNr,ToClient,DLQ, ?LOGFILE),
			
			%-----neu beginn-----
			{reply, SendNNr} = deliverMSG(NNr,ToClient,DLQ, ?LOGFILE),
			Pid ! {reply, SendNNr},
			%-----neu ende-----
			
			loop(HBQ, DLQ, DLQlimit, running);
		{Pid, {request,dellHBQ}} ->
			io:format("HBQ: Goodnight!~n"),
			Pid ! {reply, ok};
		Any ->
			io:format("HBQ: Nonsense: ~p ~n ", [Any])
	end.

insertHBQ({Size, []}, MSG) ->
	[MSG];
insertHBQ({Size, [[NNRn, Msgn, TSclientoutn, TShbqinn] | Msgs]}, [NNr, Msg, TSclientout, TShbqin]) when NNRn < NNr->
    [[NNRn, Msgn, TSclientoutn, TShbqinn]]  ++ insertHBQ({Size, Msgs}, [NNr, Msg, TSclientout, TShbqin]);
insertHBQ({Size, [[NNRn, Msgn, TSclientoutn, TShbqinn] | Msgs]}, [NNr, Msg, TSclientout, TShbqin]) ->
	[[NNr, Msg, TSclientout, TShbqin],[NNRn, Msgn, TSclientoutn, TShbqinn]] ++ Msgs.

hbqdlqAlg({Size, [ Msg | Msgn]}, DLQ, _DLQlimit, {reply, ExpNr}, NNr) when ExpNr == NNr -> %Keine Lücke
	io:format("1in hbqdlqalg~n",[]),
	{queue, DLQn} = push2DLQ(Msg,DLQ, ?LOGFILE),
	%io:format("DLQn~p~n",[DLQn]),
	{{Size - 1, Msgn}, DLQn};
hbqdlqAlg({Size, MSGs}, DLQ, DLQlimit, {reply, ExpNr}, NNr) when Size < (2 * (DLQlimit div 3)) -> %berechnung war nicht ganz korrekt (Size div 3)*2 < DLQlimit-> %HBQ.Size < 2/3 
	io:format("2in hbqdlqalg limit: ~p size: ~p~n",[DLQlimit, Size]),
	{{Size, MSGs}, DLQ};
%hbqdlqAlg({Size, [[NNRn, Msg, TSclientout, TShbqin] | MSGs]}, DLQ, DLQlimit, {reply, ExpNr}, NNr) -> %Fehlerbehandlung
%-----neu beginn----
hbqdlqAlg(HBQ, DLQ, DLQlimit, {reply, ExpNr}, NNr) -> %Fehlerbehandlung
	io:format("3in hbqdlqalg~n",[]),
	{Size, [[NNRn, Msg, TSclientout, TShbqin] | MSGs]} = HBQ,
	{queue, DLQn} = push2DLQ([ExpNr, lists:flatten(io_lib:format(
	"Fehlernachricht fuer Nachrichten ~p bis ~p generiert um " ++ timeMilliSecond(), [ExpNr, NNRn - 1])),
	TSclientout, erlang:now()], DLQ, ?LOGFILE),
	
	io:format("NNRn: ~p ExpNr: ~p~n", [NNRn, ExpNr]),
	
	%{queue, DLQn} = push2DLQ([ExpNr,"Fehlernachricht fuer Nachrichten " 
	%++ ExpNr 
	%++ " bis " 
	%++ NNRn - 1
	%++ " generiert um " 
	%++ timeMilliSecond() 
	%++ "|.", 
	%TSclientout,erlang:now()],DLQ,erlang:node()),
	%{HBQn, DLQnn} = errorHandling({Size, [[NNRn, Msg, TSclientout, TShbqin], MSGs]}, DLQ, NNRn).
	%errorHandling({Size, [[NNRn, Msg, TSclientout, TShbqin], MSGs]}, DLQn, NNRn).
	errorHandling(HBQ, DLQn, NNRn).
%-----neu ende----
	
	
errorHandling({Size, [[NNRn, Msgn, TSclientoutn, TShbqinn] | MSGs]}, DLQ, LastNNr) when (NNRn - LastNNr) =< 1 ->
	io:format("NNRn: ~p LastNNr: ~p~n", [NNRn, LastNNr]),
	{queue, DLQn} = push2DLQ([NNRn, Msgn,TSclientoutn,TShbqinn],DLQ, ?LOGFILE),
	errorHandling({Size - 1, MSGs},  DLQn, NNRn);
errorHandling(HBQ, DLQ, LastNNr) ->
	io:format("LastNNr: ~p~nDLQ: ~p ~n",[LastNNr, DLQ]),
	{HBQ, DLQ}.
	
p(Text) ->
	io:format(Text).
p(Text, Params) ->
	io:format(Text, Params).	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
