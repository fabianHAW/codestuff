-module(timesync).
-export([start/3]).

-define(NAME, "timesync").
-define(LOGFILE, lists:flatten(io_lib:format("log/~p.log", [?NAME]))).
-define(DEBUG, true).


start(StationClass, UtcOffsetMs, SenderPID) ->
	SenderPID ! {helloTime, self()},
	debug("send own pid to sender", ?DEBUG).


getNewTime() ->
	ok.

debug(Text, true) ->
	io:format("timesync_module: ~p~n", [Text]).
