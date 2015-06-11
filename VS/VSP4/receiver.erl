-module(receiver).
-export([start/6]).

-define(NAME, "receiver").
-define(LOGFILE, lists:flatten(io_lib:format("log/~p.log", [?NAME]))).
-define(DEBUG, true).

start(InterfaceName, MulticastAddr, ReceivePort, SenderPID, StationClass, UtcOffsetMs) ->
	TimeSyncPID = spawn(timesync, start, [StationClass, UtcOffsetMs, SenderPID]),
	debug("timesync spawned", ?DEBUG),
	SlotReservationPID = spawn(slotreservation, start, [SenderPID]),
	debug("slotreservation spawned", ?DEBUG).



getDataFromMulticast() ->
	ok.


debug(Text, true) ->
	io:format("receiver_module: ~p~n", [Text]).
