-module(server).
-import(werkzeug, [get_config_value/2]).
-export([start/1]).

start(Timer) ->
	io:format("Server started"),
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
	HBQname ! {request, initHBQ},
	CMEM = cmem:initCMEM(Clientlifetime, erlang:node()),
	receive
		{reply, ok} ->
			werkzeug:logging(erlang:node() ++ ".log", "Server: HBQ konnte initialisiert werden.")
	end,
	CMEM.

loop(Timer, INNR, HBQname, HBQnode, CMEM) ->
	receive 
		{Pid, getmsgid} ->
			io:format("Client ~p will Nachrichtennr", [Pid]),
			Pid ! {nid, INNR},
			loop(lifetimeTimer:resetTimer(Timer), INNR + 1, HBQname, HBQnode, CMEM);
		{dropmessage, [INNRn, Msg, TSclientout]} ->
			io:format("Client sendet Nachricht"),
			HBQname !  {request,pushHBQ,[INNRn,Msg,TSclientout]},
			loop(lifetimeTimer:resetTimer(Timer), INNR, HBQname, HBQnode, CMEM);
		{Pid, getmessages} ->
			io:format("Client ~p will Nachricht", [Pid]),
			%HBQ nach Nachricht fragen - in HBQ evtl. Dummy
			NNr = cmem:getClientNNR(CMEM, Pid),
			CMEMNew = cmem:updateClient(CMEM, Pid, NNr, erlang:node() ++ ".log"),				
			HBQname ! {self(), {request,deliverMSG,NNr,Pid}},
			loop(lifetimeTimer:resetTimer(Timer), INNR, HBQname, HBQnode, CMEMNew);
		{srvtimeout} ->
			io:format("Server Timeout!")
	end
.
		

    
   

   	
   	
