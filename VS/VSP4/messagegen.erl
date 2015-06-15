-module(messagegen).
-export([start/2]).
-import(werkzeug, [createBinaryS/1, createBinaryD/1, createBinaryNS/1]).

-define(NAME, "messagegen").
-define(LOGFILE, lists:flatten(io_lib:format("log/~p.log", [?NAME]))).
-define(DEBUG, true).
-define(SENDTIMEOFFSET, 10).

start(SenderPID, StationClass) ->
	PIDList = waitForInitialValues([]),
	MessageGenPID = self(),
	PufferPID = spawn_link(fun() -> puffer(MessageGenPID, data) end),
	debug("puffer spawned", ?DEBUG),
	DataSourcePID = spawn_link(fun() -> getDataFromSource(PufferPID) end),
	debug("getdatafromsource spawned", ?DEBUG),
	
	{_TimeSyncKey, TimeSyncPID} = lists:keyfind(timesyncpid, 1, PIDList),
	{_SlotreservationKey, SlotReservationPID} = lists:keyfind(slotreservationpid, 1, PIDList),
	
	
	NewSlot = getInitialSlot(SlotReservationPID),
	
	io:format("~p~n", [NewSlot]),
	
	loop(PufferPID, SenderPID, StationClass, TimeSyncPID, SlotReservationPID, NewSlot, false),
	
	exit(PufferPID, "messagegen killed"),
	debug("puffer killed", ?DEBUG),
	exit(DataSourcePID, "messagegen killed"),
	debug("getdatafromsource killed", ?DEBUG),
	debug("messagegen terminated", ?DEBUG).
	
loop(PufferPID, SenderPID, StationClass, TimeSyncPID, SlotReservationPID, OldSlot, false) ->
	TimeSyncPID ! {getTime, self()},
	receive 
		{currentTime, Timestamp} ->
			debug("received currentTime", ?DEBUG),
			_Killed = false;
		kill ->
			Timestamp = -1,
			_Killed = true
	end,
	Sendtime = calcSendTime(OldSlot, Timestamp),
	
	%Sendtime expired??
	
	waitSendtime(Sendtime),
	SlotReservationPID ! {getSlot, self()},
	receive 
		%{nextSlot, nok} ->
		%	debug("there is no next slot available", ?DEBUG),
			
		{nextSlot, NextSlot} ->
			debug("received next slot", ?DEBUG),
			_KilledNew = false;
		kill ->
			NextSlot = -1,
			_KilledNew = true
	end,
	{Message, KilledNewNew} = prepareMessage(NextSlot, StationClass, PufferPID),
	SenderPID ! Message,
	loop(PufferPID, SenderPID, StationClass, TimeSyncPID, SlotReservationPID, NextSlot, KilledNewNew);
loop(_PufferPID, _SenderPID, _StationClass, _TimeSyncPID, _SlotReservationPID, _OldSlot, true) ->
	debug("killed", ?DEBUG).


puffer(MessageGenPID, Data) ->
	receive 
		{data, DataNew} ->
			debug("got new data from source", ?DEBUG);
		getdata ->
			MessageGenPID ! {newdata, Data},
			debug("send new data to messagegen", ?DEBUG),
			DataNew = Data
	end,
	puffer(MessageGenPID, DataNew).
	
getDataFromSource(PufferPID) ->
	Data = io:get_chars("", 24),
	PufferPID ! {data, Data},
	debug("send new data to puffer", ?DEBUG),
	getDataFromSource(PufferPID).
	
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

getInitialSlot(SlotReservationPID) ->
	%initialen slot holen
	SlotReservationPID ! {getSlot, self()},
	receive 
		{nextSlot, nok} ->
			debug("there is no initial slot available", ?DEBUG),
			timer:sleep(200000),
			getInitialSlot(SlotReservationPID);
		{nextSlot, NewSlot} ->
			debug("received initial slot", ?DEBUG),
			NewSlot
	end.

calcSendTime(Slot, Timestamp) ->
	debug("calculate sendtime", ?DEBUG),
	%wenn Slot-Nummerieung bei 1 beginnt, muss einer abgezogen werden
	((Slot - 1) * 40) + ?SENDTIMEOFFSET.

waitSendtime(Sendtime) ->
	debug("wait sendtime", ?DEBUG),
	timer:sleep(Sendtime).
	
prepareMessage(Slot, StationClass, PufferPID) ->
	PufferPID ! getdata,
	receive 
		{newdata, Data} ->
			debug("received new data", ?DEBUG),
			Killed = false;
		kill ->
			Data = -1,
			Killed = true
	end,
	{{createBinaryS(StationClass), createBinaryNS(Slot), createBinaryD(Data)}, Killed}.


debug(Text, true) ->
	io:format("messagegen_module: ~p~n", [Text]).
