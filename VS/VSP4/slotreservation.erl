-module(slotreservation).
-export([start/1]).

-define(NAME, "slotreservation").
-define(LOGFILE, lists:flatten(io_lib:format("log/~p.log", [?NAME]))).
-define(DEBUG, true).

start(SenderPID) ->
	SenderPID ! {helloSlot, self()},
	debug("send own pid to sender", ?DEBUG).


getNewSlot() ->
	ok.

debug(Text, true) ->
	io:format("slotreservation_module: ~p~n", [Text]).
