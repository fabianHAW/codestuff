-module(sender).
-export([start/4]).

-define(NAME, "sender").
-define(LOGFILE, lists:flatten(io_lib:format("log/~p.log", [?NAME]))).
-define(DEBUG, true).

start(InterfaceName, MulticastAddr, ReceivePort, StationNumber) ->
	MessageGenPID = spawn(messagegen, start, [self()]),
	debug("messagegen spawned", ?DEBUG),
	SendPort = ReceivePort + StationNumber,
	PIDList = waitForInitialValues(MessageGenPID, []),
	debug(PIDList, ?DEBUG).



waitForInitialValues(MessageGenPID, PIDList) when length(PIDList) == 2 ->
	PIDList;
waitForInitialValues(MessageGenPID, PIDList) ->
	receive
		{helloTime, TimeSyncPID} -> 
			debug("received timesyncpid", ?DEBUG),
			MessageGenPID ! {helloTime, TimeSyncPID},
			debug("send timesyncpid to messagegen", ?DEBUG),
			ListElement = [{timesyncpid, TimeSyncPID}];
		{helloSlot, SlotReservationPID} ->
			debug("received slotreservationpid", ?DEBUG),
			MessageGenPID ! {helloSlot, SlotReservationPID},
			debug("send slotreservationpid to messagegen", ?DEBUG),
			ListElement = [{slotreservationpid, SlotReservationPID}]
	end,
	waitForInitialValues(MessageGenPID, lists:append(PIDList, ListElement)).


checkSlot(Slot) ->
	ok.
	
sendMulticast(Message) ->
	ok.

debug(Text, true) ->
	io:format("sender_module: ~p~n", [Text]).
