-module(lifetimeTimer).
-import(werkzeug, [reset_timer/3, get_config_value/2]).
-import(server, [start/1]).
-import(client, [loop/3]).
-export([createServer/0, createClient/0, resetTimer/1]).

-define(CLIENTCFG, "client.cfg").
-define(SEVERCFG, "server.cfg").

%Erzeugt den Server-Prozess und den Lifetime Timer.
createClient() ->
	{Clients, Lifetime, Servername, Servernode, Sendinterval} = init(?CLIENTCFG),
	createClient(Clients, Lifetime, Servername, Servernode, Sendinterval).
	
createClient(0, _Lifetime, _Servername, _Servernode, _Sendinterval) ->
	all_Clients_created;
createClient(Clients, Lifetime, Servername, Servernode, Sendinterval) ->
	ClientPID = spawn(client, loop, [Servername, Servernode, Sendinterval]),
	%Client kann nur im Reader-Modus oder während der Anforderung einer Nachrichtennummer interrupted werden
	timer:send_after(Lifetime * 1000, ClientPID, {interrupt, timeout}),
	%einen Moment warten bis der naechste Client erzeugt wird
	timer:sleep(1000),
	createClient(Clients - 1, Lifetime, Servername, Servernode, Sendinterval).

%Config-File auslesen und alle Parameter als Tupel zurueckgeben
init(Datei) ->
	{ok, ConfigList} = file:consult(Datei),
	{ok, Clients} = get_config_value(clients, ConfigList),
	{ok, Lifetime} = get_config_value(lifetime, ConfigList),
	{ok, Servername} = get_config_value(servername, ConfigList),
	{ok, Servernode} = get_config_value(servernode, ConfigList),
	{ok, Sendinterval} = get_config_value(sendeintervall, ConfigList),
	{Clients, Lifetime, Servername, Servernode, Sendinterval}.

% Erzeugt den Server-Prozess und den Lifetime Timer.
createServer() ->
	{ok, ConfigListe} = file:consult(?SEVERCFG),
    {ok, Lifetime} = get_config_value(latency, ConfigListe),
    {ok, Servername} = get_config_value(servername, ConfigListe),    
    {ok, Timer} = timer:send_after(Lifetime*1000, Servername, {srvtimeout}),
	timer:send_after(1000*1000, Servername, {dellExpired, Servername}),
	spawn(server, start, [Timer]).
    
% Liefert einen neu gesetzten Timer.
resetTimer(Timer) ->
	{ok, ConfigListe} = file:consult(?SEVERCFG),
    {ok, Lifetime} = get_config_value(latency, ConfigListe),
    reset_timer(Timer, Lifetime, {srvtimeout}).
