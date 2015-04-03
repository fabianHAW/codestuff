-module(server).
-import(werkzeug, [get_config_value/2]).
-export([start/1]).
-define(LOGFILE, lists:flatten(io_lib:format("~p.log", [node()]))).

start(Timer) ->
	p("Server started ~n"),
	{Clientlifetime, Servername, HBQname, HBQnode} = readCfg(),
	erlang:register(Servername, self()),
	CMEM = initHBQCMEM(Clientlifetime, Servername, HBQname, HBQnode),
	loop(Timer, 1, HBQname, HBQnode, CMEM).

%Steuernde Werte aus server.cfg lesen.
readCfg() ->
	{ok, ConfigListe} = file:consult("server.cfg"),
    {ok, Clientlifetime} = get_config_value(clientlifetime, ConfigListe),
    {ok, Servername} = get_config_value(servername, ConfigListe),
    {ok, HBQname} = get_config_value(hbqname, ConfigListe),
    {ok, HBQnode} = get_config_value(hbqnode, ConfigListe),
    {Clientlifetime, Servername, HBQname, HBQnode}.

initHBQCMEM(Clientlifetime, Servername, HBQname, HBQnode) ->
	{HBQname, HBQnode} ! {self(), {request, initHBQ}},
	CMEM = cmem:initCMEM(Clientlifetime, ?LOGFILE),
	receive
		{reply, ok} ->
			werkzeug:logging(?LOGFILE, "Server: HBQ konnte initialisiert werden. ~n")
	end,
	p("1"),
	CMEM.

loop(Timer, INNR, HBQname, HBQnode, CMEM) ->
	receive 
		{Pid, getmsgid} ->
			p("Client will Nachrichtennr ~n"),
			Pid ! {nid, INNR},
			loop(lifetimeTimer:resetTimer(Timer), INNR + 1, HBQname, HBQnode, CMEM);
		{dropmessage, [INNRn, Msg, TSclientout]} ->
			io:format("Client sendet Nachricht ~n"),
			p("Server NNr: ~p ~n", [INNRn]),
			{HBQname, HBQnode} !  {self(), {request,pushHBQ,[INNRn,Msg,TSclientout]}},
							
			readReturnValHBQ(),
			loop(lifetimeTimer:resetTimer(Timer), INNR, HBQname, HBQnode, CMEM);
		{Pid, getmessages} ->
			io:format("Client ~p will Nachricht ~n", [Pid]),
			%HBQ nach Nachricht fragen - in HBQ evtl. Dummy
			p("~p ~p ~n", [CMEM, Pid]),
			NNr = cmem:getClientNNr(CMEM, Pid),
			CMEMNew = cmem:updateClient(CMEM, Pid, NNr, ?LOGFILE),				
			{HBQname, HBQnode} ! {self(), {request,deliverMSG,NNr,Pid}},
			loop(lifetimeTimer:resetTimer(Timer), INNR, HBQname, HBQnode, CMEMNew);
		{srvtimeout} ->
			io:format("Server Timeout! ~n")
	end
.

readReturnValHBQ() ->
	receive
		{reply, ok} ->
			io:format("Nachricht erfolgreich in HBQ eingetragen ~n")
	end
.

p(Text) ->
	io:format(Text).
p(Text, Params) ->
	io:format(Text, Params).
   

   	
   	
