-module(messagegen).
-export([start/2]).
-import(werkzeug, [logging/2, createBinaryS/1, createBinaryD/1, createBinaryNS/1, getUTC/0]).

-define(NAME, lists:flatten(io_lib:format("messagegen@~p", [node()]))).
-define(LOGFILE, lists:flatten(io_lib:format("log/~p.log", [?NAME]))).
-define(DEBUG, false).
-define(SENDTIMEOFFSET, 10).
-define(SLOTLENGTH, 40).

start(SenderPID, StationClass) ->
	PIDList = waitForInitialValues([]),
	MessageGenPID = self(),
	PufferPID = spawn_link(fun() -> puffer(MessageGenPID, data) end),
	debug("puffer spawned", ?DEBUG),
	DataSourcePID = spawn_link(fun() -> getDataFromSource(PufferPID) end),
	debug("getdatafromsource spawned", ?DEBUG),
	
	{_TimeSyncKey, TimeSyncPID} = lists:keyfind(timesyncpid, 1, PIDList),
	{_SlotreservationKey, SlotReservationPID} = lists:keyfind(slotreservationpid, 1, PIDList),
	
	
	NewSlot = getInitialSlot(),
	
	%io:format("~p~n", [NewSlot]),
	
	loop(PufferPID, SenderPID, StationClass, TimeSyncPID, SlotReservationPID, 0, NewSlot, false),
	
	exit(PufferPID, "messagegen killed"),
	debug("puffer killed", ?DEBUG),
	exit(DataSourcePID, "messagegen killed"),
	debug("getdatafromsource killed", ?DEBUG),
	debug("messagegen terminated", ?DEBUG).
	
loop(PufferPID, SenderPID, StationClass, TimeSyncPID, SlotReservationPID, OldSlot, NewSlot, false) ->
	%TimeSyncPID ! {getTime, self()},
	%receive 
	%	{currentTime, Timestamp} ->
	%		debug("received currentTime", ?DEBUG),
	%		_Killed = false;
	%	kill ->
	%		Timestamp = -1,
	%		_Killed = true
	%end,
	
	
	Sendtime = calcSendTime(NewSlot, OldSlot),
	%io:format("sendtime: ~p~n", [Sendtime]),
	
	Timestamp = getUTC(),
	%io:format("~p~n",[Sendtime]),
	waitSendtime(Sendtime),
	Time = getUTC() - Timestamp,
	
	%Sendtime expired?
	case Sendtime =< (Time + ((?SLOTLENGTH / 2) - ?SENDTIMEOFFSET)) of
		false ->
			debug("sendtime expired", ?DEBUG),
			logging(?LOGFILE, lists:flatten(io_lib:format("sendtime expired ~n", []))),
			{NextSlot, KilledNew} = getNextSlot(SlotReservationPID),
			SenderPID ! {nomessage};
		true ->
			{NextSlot, Killed} = getNextSlot(SlotReservationPID),
			{Message, KilledNew} = prepareMessage(NextSlot, StationClass, PufferPID, Killed),
			logging(?LOGFILE, lists:flatten(io_lib:format("created new message ~p ~n", [Message]))),
			SenderPID ! {message, Message, OldSlot}
	end,
	
	loop(PufferPID, SenderPID, StationClass, TimeSyncPID, SlotReservationPID, NewSlot, NextSlot, KilledNew);
loop(_PufferPID, _SenderPID, _StationClass, _TimeSyncPID, _SlotReservationPID, _OldSlot, _NewSlot, true) ->
	debug("killed", ?DEBUG).


puffer(MessageGenPID, Data) ->
	receive 
		{data, DataNew} ->
			ok;
			%debug("got new data from source", ?DEBUG);
		getdata ->
			MessageGenPID ! {newdata, Data},
			%debug("send new data to messagegen", ?DEBUG),
			DataNew = Data
	end,
	puffer(MessageGenPID, DataNew).
	
getDataFromSource(PufferPID) ->
	Data = io:get_chars("", 24),
	PufferPID ! {data, Data},
	%debug("send new data to puffer", ?DEBUG),
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

getInitialSlot() ->
	%initialen slot erhalten
	%SlotReservationPID ! {getSlot, self()},
	receive 
		{initialSlot, NewSlot} ->
			debug("received initial slot", ?DEBUG);
		{nextSlot, NewSlot} ->
			debug("received next slot", ?DEBUG)
	end,
	NewSlot.
	
getNextSlot(SlotReservationPID) ->
	SlotReservationPID ! {getSlot, self()},
	receive 
		%zum debuggen noch drin gelassen
		{nextSlot, nok} ->
			debug("there is no next slot available", ?DEBUG),
			Killed = false,
			NextSlot = -1;
		{nextSlot, NextSlot} ->
			debug("received next slot", ?DEBUG),
			Killed = false;
		kill ->
			NextSlot = -1,
			Killed = true
	end,
	{NextSlot, Killed}.

calcSendTime(NewSlot, OldSlot) ->
	debug("calculate sendtime", ?DEBUG),
	%wenn Slot-Nummerieung bei 1 beginnt, muss einer abgezogen werden
	io:format("oldslot: ~p newslot: ~p~n", [OldSlot, NewSlot]),
	((25 - OldSlot) * 40) + ((NewSlot - 1) * 40) + ?SENDTIMEOFFSET.

waitSendtime(Sendtime) ->
	debug("wait sendtime", ?DEBUG),
	timer:sleep(Sendtime).
	
prepareMessage(Slot, StationClass, PufferPID, Killed) ->
	PufferPID ! getdata,
	receive 
		{newdata, Data} ->
			debug("received new data", ?DEBUG),
			KilledNew = Killed;
		kill ->
			Data = -1,
			KilledNew = true
	end,
	{{createBinaryS(StationClass), createBinaryNS(Slot), createBinaryD(Data)}, KilledNew}.


debug(Text, true) ->
	io:format("messagegen_module: ~p~n", [Text]).
