-module(lifetimeTimer).
-import(werkzeug, [reset_time/3, get_config_value/2]).
-import(server, [start/1]).
-export([createServer/0, resetTimer/1]).

% Erzeugt den Server-Prozess und den Lifetime Timer.
createServer() ->
	{ok, ConfigListe} = file:consult("server.cfg"),
    {ok, Lifetime} = get_config_value(latency, ConfigListe),
    timer:start(),
    {ok, Timer} = timer:send_after(Lifetime*1000, SrvPid, {srvtimeout}),
    SrvPid = spawn(server, start, [Timer]),
    

% Liefert einen neu gesetzten Timer.
resetTimer(Timer) ->
	{ok, ConfigListe} = file:consult("server.cfg"),
    {ok, Lifetime} = get_config_value(lifetime, ConfigListe),
    werkzeug:reset_time(Timer, Lifetime, {srvtimeout}).


