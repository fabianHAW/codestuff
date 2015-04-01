-module(client).
-import(werkzeug, [get_config_value/2, timeMilliSecond/0, logging/2]).
-export([start/0, loop/3]).

-define(CFG, "client.cfg").
-define(HOSTNAME, inet:gethostname()).
-define(GROUP, 3).
-define(TEAM, 01).
-define(LOGFILE, "client_" ++ ?TEAM ++ node() ++ ".log").

%auslagern in lifetimetimer
start() ->
	{Lifetime, Servername, Servernode, Sendinterval} = init(?CFG),
	ClientPID = spawn(client, loop, [Servername, Servernode, Sendinterval]),
	timer:send_after(Lifetime * 1000, ClientPID, {interrupt, timeout}).


%Config-File auslesen und alle Parameter als Tupel zurueckgeben
init(Datei) ->
	{ok, ConfigList} = file:consult(Datei),
	{ok, Lifetime} = get_config_value(lifetime, ConfigList),
	{ok, Servername} = get_config_value(servername, ConfigList),
	{ok, Servernode} = get_config_value(servernode, ConfigList),
	{ok, Sendinterval} = get_config_value(sendeintervall, ConfigList),
	{Lifetime, Servername, Servernode, Sendinterval}.

loop(Servername, Servernode, Sendinterval) ->
	%Rolle des Redakteur-Clients
	loopEditor(Servername, Servernode, Sendinterval),
	%Rolle des Leser-Clients
	loopReader().

loopEditor(Servername, Servernode, Sendinterval) ->
	io:format("in loopEditor~n", []),
	loopEditor(Servername, Servernode, Sendinterval, 0).
	
loopEditor(Servername, Servernode, Sendinterval, Counter) when Counter < 5 ->	
	%1. neue Nachrichten Nummer besorgen
	Number = getMSGNum(Servername),
	%3.1. neue Nachricht generieren 
	{ok, Client} = ?HOSTNAME, 
	Msg = lists:flatten(io_lib:format("~p-~p-client@~p-~p-~pte_Nachricht. C Out: ", [?GROUP, ?TEAM, Client, self(), Number])),
	%4. einen bestimmten Zeitabstand warten
	timer:sleep(Sendinterval * 1000),
	%3.2. der Timestamp darf erst nach dem abwarten des Sendeintervalls erzeugt
	%und der Nachricht angehangen werden
	Timestamp = timeMilliSecond(),
	MsgNew = Msg ++ Timestamp,
	%5. generierte Nachricht senden
	Servername ! {dropmessage, [Number, MsgNew, Timestamp]},
	logging(?LOGFILE, MsgNew),
	loopEditor(Servername, Servernode, Sendinterval, Counter + 1);
%wurden alle 5 Nachrichten gesendet, wird Zeitabstand neu berechnet
%ebenfalls wird neue Nachrichtennummer angefordert und diese geloggt aber nicht verschickt
loopEditor(Servername, Servernode, Sendinterval, _Counter) ->
	io:format("forget to send msg~n", []).
	
loopReader() ->
	io:format("in loopReader~n", []),
	receive
		{interrupt, timeout} ->
			io:format("reader-client interrupted~n", [])
	end.

getMSGNum(Servername) ->
	Servername ! {self(), getmsgid},
	%2. auf neue Nachrichten Nummer warten
	receive
		{nid, Number} ->
			io:format("got new number~n" ,[]);
		Any ->
			io:format("an error occured, while waiting for new message number: ~p~n" ,[Any]),
			Number = -1			
	end,
	Number.
