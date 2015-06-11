-module(messagegen).
-export([start/1]).

-define(NAME, "messagegen").
-define(LOGFILE, lists:flatten(io_lib:format("log/~p.log", [?NAME]))).
-define(DEBUG, true).

start(SenderPID) ->
	PIDList = waitForInitialValues([]),
	debug(PIDList, ?DEBUG).


waitForInitialValues(PIDList) when length(PIDList) == 2 ->
	PIDList;
waitForInitialValues(PIDList) ->
	receive
		{helloTime, TimeSyncPID} -> 
			debug("received timesyncpid", ?DEBUG),
			ListElement = [{timesyncpid, TimeSyncPID}];
		{helloSlot, SlotReservationPID} ->
			debug("received slotreservationpid", ?DEBUG),
			ListElement = [{slotreservationpid, SlotReservationPID}]
	end,
	waitForInitialValues(lists:append(PIDList, ListElement)).

calcSendTime(Slot, Timestamp) ->
	ok.

prepareMessage(Slot, Timestamp) ->
	ok.

waitSendtime(Sendtime) ->
	ok.

debug(Text, true) ->
	io:format("messagegen_module: ~p~n", [Text]).
