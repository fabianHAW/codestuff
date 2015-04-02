-module(hbq).
-import(werkzeug, [logging/2, timeMilliSecond/0, get_config_value/2]).
-import(dlq, [push2DLQ/3, expectedNr/1, initDLQ/2, deliverMSG/4]).
-export([start/0]).

start() ->
	{ok, ConfigListe} = file:consult("server.cfg"),
	{ok, HBQname} = get_config_value(hbqname, ConfigListe),
	
	io:format("~p ~n",[erlang:register(HBQname, self())]),
	io:format("~p ~n", [registered()]),
	werkzeug:logging(werkzeug:to_String(erlang:node()) ++ ".log", "HBQ>>> server.cfg geöffnet..."),			
	loop().
	
loop() ->
	receive 
		{Pid, {request, initHBQ}} ->
			{ok, ConfigListe} = file:consult("server.cfg"),
			{ok, DLQlimit} = get_config_value(dlqlimit, ConfigListe),
			loop(Pid, {0,[[]]}, DLQlimit, init)
	end
.

loop(Pid, MSGs, DLQlimit, init) ->
	werkzeug:logging(erlang:node() ++ ".log", "HBQ>>> initialisiert worden von " ++ Pid ++ "."),
	DLQ = dlq:initDLQ(DLQlimit, erlang:node() ++ ".log"),
	Pid ! {reply, ok},
	loop(MSGs, DLQ, DLQlimit, running);
loop(HBQ, DLQ, DLQlimit, running) ->
	receive
		{Pid, {request,pushHBQ,[NNr,Msg,TSclientout]}} ->
			{Size, _HBQnew} = HBQ,
			HBQnn = insertHBQ(HBQ, [NNr, Msg, TSclientout, erlang:now()]),
			{HBQn, DLQn} = hbqdlqAlg({Size + 1, HBQnn}, DLQ, DLQlimit, dlq:expectedNr(DLQ), NNr),
			Pid ! {reply, ok},
			loop(HBQn, DLQn, DLQlimit, running);
		{Pid, {request,deliverMSG,NNr,ToClient}} ->
			dlq:deliverMSG(NNr,ToClient,DLQ,erlang:node() ++ ".log"),
			loop(HBQ, DLQ, DLQlimit, running);
		{Pid, {request,dellHBQ}} ->
			io:format("HBQ: Goodnight!"),
			Pid ! {reply, ok}
	end
.

insertHBQ({Size, [[NNRn, Msgn, TSclientoutn, TShbqinn] | Msgs]}, [NNr, Msg, TSclientout, TShbqin]) when NNRn < NNr->
    [NNRn, Msgn, TSclientoutn, TShbqinn]  ++ insertHBQ({Size, Msgs}, [NNr, Msg, TSclientout, TShbqin]);
insertHBQ({Size, [[NNRn, Msgn, TSclientoutn, TShbqinn] | Msgs]}, [NNr, Msg, TSclientout, TShbqin]) when NNRn > NNr->
	[[NNr, Msg, TSclientout, TShbqin],[NNRn, Msgn, TSclientoutn, TShbqinn]] ++ Msgs.

hbqdlqAlg({Size, [ Msg | Msgn]}, DLQ, _DLQlimit, ExpNr, NNr) when ExpNr == NNr -> %Keine Lücke
	{[Size - 1, Msgn], push2DLQ(Msg,DLQ,erlang:node())};
hbqdlqAlg({Size, MSGs}, DLQ, DLQlimit, ExpNr, NNr) when (Size div 3)*2 < DLQlimit-> %HBQ.Size < 2/3 
	{[Size, MSGs], DLQ};
hbqdlqAlg({Size, [[NNRn, Msg, TSclientout, TShbqin] | MSGs]}, DLQ, DLQlimit, ExpNr, NNr) -> %Fehlerbehandlung
	DLQn = push2DLQ([ExpNr,"Fehlernachricht fuer Nachrichten " 
	++ ExpNr 
	++ " bis " 
	++ NNRn - 1
	++ " generiert um " 
	++ timeMilliSecond() 
	++ "|.", 
	TSclientout,erlang:now()],DLQ,erlang:node()),
	{HBQn, DLQnn} = errorHandling({Size, [[NNRn, Msg, TSclientout, TShbqin], MSGs]}, DLQ, NNRn).
	
	
errorHandling({Size, [[NNRn, Msgn, TSclientoutn, TShbqinn] | MSGs]}, DLQ, LastNNr) when NNRn - LastNNr =< 1 ->
	errorHandling({Size - 1, MSGs},  push2DLQ([NNRn, Msgn,TSclientoutn,TShbqinn],DLQ,erlang:node()), NNRn);
errorHandling(HBQ, DLQ, _LastNNr) ->
	{HBQ, DLQ}.
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
