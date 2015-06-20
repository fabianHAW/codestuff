-module(messagegen).
-export([start/2]).
-import(werkzeug, [logging/2, createBinaryS/1, createBinaryD/1, createBinaryNS/1, getUTC/0]).

-define(NAME, lists:flatten(io_lib:format("messagegen@~p", [node()]))).
-define(LOGFILE, lists:flatten(io_lib:format("log/~p.log", [?NAME]))).
-define(DEBUG, false).
-define(SENDTIMEOFFSET, 10).
-define(SLOTLENGTH, 40).

start(SenderPID, StationClass) ->
	%Diese Methode benoetigt beim ersten Aufruf einige ms die das Senden
	%beeinflussen wuerde, daher wird sie hier einmal aufgerufen
	crypto:rand_uniform(1, 26),
	PIDList = waitForInitialValues([]),
	MessageGenPID = self(),
	PufferPID = spawn_link(fun() -> puffer(MessageGenPID, data) end),
	debug("puffer spawned", ?DEBUG),
	DataSourcePID = spawn_link(fun() -> getDataFromSource(PufferPID) end),
	debug("getdatafromsource spawned", ?DEBUG),
	
	{_TimeSyncKey, TimeSyncPID} = lists:keyfind(timesyncpid, 1, PIDList),
	{_SlotreservationKey, SlotReservationPID} = lists:keyfind(slotreservationpid, 1, PIDList),
	
	NewSlot = getInitialSlot(),
	
	loop(PufferPID, SenderPID, StationClass, TimeSyncPID, SlotReservationPID, 0, NewSlot, false),
	
	exit(PufferPID, "messagegen killed"),
	debug("puffer killed", ?DEBUG),
	exit(DataSourcePID, "messagegen killed"),
	debug("getdatafromsource killed", ?DEBUG),
	debug("messagegen terminated", ?DEBUG).
	
loop(PufferPID, SenderPID, StationClass, TimeSyncPID, SlotReservationPID, OldSlot, NewSlot, false) ->
	
	
	TimeSyncPID ! {getTime, self()},
	receive 
		{currentTime, CurrentTimestamp} ->
			debug("received currentTime", ?DEBUG),
			Killed = false;
		kill ->
			CurrentTimestamp = -1,
			Killed = true
	end,
	
	TimeToGetData = getUTC(),
	{{MessageClass, MessageData}, KilledNew} = prepareMessage(StationClass, PufferPID, Killed),
	TimeToGetDataNew = getUTC() - TimeToGetData,
	Sendtime = calcSendTime(NewSlot, OldSlot, CurrentTimestamp, TimeToGetDataNew),
	
	logging(?LOGFILE, lists:flatten(io_lib:format("Sendtime ~p~n", [Sendtime]))),
	
	Timestamp = getUTC(),
	waitSendtime(Sendtime),
	Time = getUTC() - Timestamp,
	logging(?LOGFILE, lists:flatten(io_lib:format("2sendtime ~p~n", [Time]))),
	
	%Sendtime expired?
	case Sendtime =< (Time + ((?SLOTLENGTH / 2) - ?SENDTIMEOFFSET)) of
		false ->
			debug("sendtime expired", ?DEBUG),
			logging(?LOGFILE, lists:flatten(io_lib:format("sendtime expired ~n", []))),
			{NextSlot, KilledNewNew} = getNextSlot(SlotReservationPID, KilledNew),
			SenderPID ! {nomessage};
		true ->
			Time1 = getUTC(),
			{NextSlot, KilledNewNew} = getNextSlot(SlotReservationPID, KilledNew),
			logging(?LOGFILE, lists:flatten(io_lib:format("waitingtime ~p~n", [getUTC() - Time1]))),
			Message = {MessageClass, createBinaryNS(NextSlot), MessageData},
			logging(?LOGFILE, lists:flatten(io_lib:format("sendtime: ~p currentslot ~p nextslot ~p~n", [Sendtime, NewSlot, NextSlot]))),
	
			SenderPID ! {message, Message, NewSlot, NextSlot}
	end,
	
	loop(PufferPID, SenderPID, StationClass, TimeSyncPID, SlotReservationPID, NewSlot, NextSlot, KilledNewNew);
loop(_PufferPID, _SenderPID, _StationClass, _TimeSyncPID, _SlotReservationPID, _OldSlot, _NewSlot, true) ->
	debug("killed", ?DEBUG).


puffer(MessageGenPID, Data) ->
	receive 
		{data, DataNew} ->
			ok,
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

getInitialSlot() ->
	receive 
		{initialSlot, NewSlot} ->
			debug("received initial slot", ?DEBUG);
		{nextSlot, NewSlot} ->
			debug("received next slot", ?DEBUG)
	end,
	NewSlot.
	
getNextSlot(SlotReservationPID, Killed) ->
	SlotReservationPID ! {getSlot, self()},
	receive 
		{nextSlot, NextSlot} ->
			KilledNew = Killed;
		kill ->
			NextSlot = -1,
			KilledNew = true
	end,
	{NextSlot, KilledNew}.

%Berechnet die Sendezeit, wenn ein Frame-lang kein Slot erhalten wurde.
calcSendTime(NewSlot, 0, _CurrentTimestamp, TimeToGetDataEnd) ->
	debug("calculate sendtime", ?DEBUG),
	(((NewSlot - 1) * ?SLOTLENGTH) + ?SENDTIMEOFFSET) - TimeToGetDataEnd;
calcSendTime(NewSlot, _OldSlot, CurrentTimestamp, TimeToGetDataEnd) ->
	debug("calculate sendtime", ?DEBUG),
	(1000 - getSecInMilli(CurrentTimestamp) + ?SENDTIMEOFFSET) + (((NewSlot - 1) * ?SLOTLENGTH) + ?SENDTIMEOFFSET) - TimeToGetDataEnd.

getSecInMilli(Timestamp) ->
	list_to_integer(string:substr(integer_to_list(Timestamp), 10, 4)) rem 1000.

waitSendtime(Sendtime) ->
	debug("wait sendtime", ?DEBUG),
	timer:sleep(Sendtime).
	
prepareMessage(StationClass, PufferPID, Killed) ->
	PufferPID ! getdata,
	receive 
		{newdata, Data} ->
			debug("received new data", ?DEBUG),
			KilledNew = Killed;
		kill ->
			Data = -1,
			KilledNew = true
	end,
	{{createBinaryS(StationClass), createBinaryD(Data)}, KilledNew}.


debug(Text, true) ->
	io:format("messagegen_module: ~p~n", [Text]);
debug(_Text, false) ->
	ok.
