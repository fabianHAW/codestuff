-module(lifetimeTimer).
-import(werkzeug, [reset_timer/3, get_config_value/2]).
-import(server, [start/1]).
-import(client, [loop/0]).
-export([createServer/0, createClient/0, resetTimer/1]).

-define(CLIENTCFG, "client.cfg").

%Erzeugt den Server-Prozess und den Lifetime Timer.
createClient() ->
	{Lifetime, Servername, Servernode, Sendinterval} = init(?CLIENTCFG),
	ClientPID = spawn(client, loop, [Servername, Servernode, Sendinterval]),
	%Client kann nur im Reader-Modus oder während der Anforderung einer Nachrichtennummer interrupted werden
	timer:send_after(Lifetime * 1000, ClientPID, {interrupt, timeout}).

%Config-File auslesen und alle Parameter als Tupel zurueckgeben
init(Datei) ->
	{ok, ConfigList} = file:consult(Datei),
	{ok, Lifetime} = get_config_value(lifetime, ConfigList),
	{ok, Servername} = get_config_value(servername, ConfigList),
	{ok, Servernode} = get_config_value(servernode, ConfigList),
	{ok, Sendinterval} = get_config_value(sendeintervall, ConfigList),
	{Lifetime, Servername, Servernode, Sendinterval}.



% Erzeugt den Server-Prozess und den Lifetime Timer.
createServer() ->
	{ok, ConfigListe} = file:consult("server.cfg"),
    {ok, Lifetime} = get_config_value(latency, ConfigListe),
    {ok, Servername} = get_config_value(servername, ConfigListe),
    %-----neu beginn----
    %muss nicht explizit gestartet werden, wird gestartet wenn nötig
    %timer:start(),
    
    {ok, Timer} = timer:send_after(Lifetime*1000, Servername, {srvtimeout}),
	spawn(server, start, [Timer]).
    

% Liefert einen neu gesetzten Timer.
resetTimer(Timer) ->
	{ok, ConfigListe} = file:consult("server.cfg"),
    {ok, Lifetime} = get_config_value(latency, ConfigListe),
    werkzeug:reset_timer(Timer, Lifetime, {srvtimeout}).


