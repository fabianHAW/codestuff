-module(messagegen).
-export([start/2]).
-import(werkzeug, [logging/2, createBinaryS/1, createBinaryD/1, createBinaryNS/1, getUTC/0]).

-define(NAME, lists:flatten(io_lib:format("messagegen@~p", [node()]))).
-define(LOGFILE, lists:flatten(io_lib:format("~p.log", [?NAME]))).
-define(DEBUG, false).
-define(SENDTIMEOFFSET, 0).
-define(SLOTLENGTH, 40).

start(SenderPID, StationClass) ->
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
	TimeToGetDataStart = getUTC(),
	{{MessageClass, MessageData}, Killed} = prepareMessage(StationClass, PufferPID, false),
	TimeToGetDataEnd = getUTC() - TimeToGetDataStart,
			
	Sendtime = calcSendTime(NewSlot, OldSlot, TimeToGetDataEnd),
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
			{NextSlot, KilledNew} = getNextSlot(SlotReservationPID, Killed),
			SenderPID ! {nomessage};
		true ->
			{NextSlot, KilledNew} = getNextSlot(SlotReservationPID, Killed),
			%{Message, KilledNew} = prepareMessage(NextSlot, StationClass, PufferPID, Killed),
			Message = {MessageClass, createBinaryNS(NextSlot), MessageData},
			%logging(?LOGFILE, lists:flatten(io_lib:format("created new message ~p ~n", [Message]))),
			SenderPID ! {message, Message, NewSlot}
	end,
	
	logging(?LOGFILE, lists:flatten(io_lib:format("sendtime: ~p newslot ~p oldslot ~p nextslot ~p~n", [Sendtime, NewSlot, OldSlot, NextSlot]))),
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
	
getNextSlot(SlotReservationPID, Killed) ->
	%Time1 = getUTC(),
	SlotReservationPID ! {getSlot, self()},
	receive 
		%zum debuggen noch drin gelassen
		{nextSlot, nok} ->
			debug("there is no next slot available", ?DEBUG),
			KilledNew = Killed,
			NextSlot = -1;
		{nextSlot, NextSlot} ->
			%debug("received next slot", ?DEBUG),
			KilledNew = Killed;
		kill ->
			NextSlot = -1,
			KilledNew = true
	end,
	%logging(?LOGFILE, lists:flatten(io_lib:format("Time1: ~p Time2: ~p~n", [Time1, getUTC() - Time1]))),
	{NextSlot, KilledNew}.

calcSendTime(NewSlot, 0, TimeToGetDataEnd) ->
	logging(?LOGFILE, lists:flatten(io_lib:format("1newslot ~p oldslot ~p~n", [NewSlot, 0]))),
	debug("calculate sendtime", ?DEBUG),
	%wenn Slot-Nummerieung bei 1 beginnt, muss einer abgezogen werden
	%io:format("oldslot: ~p newslot: ~p~n", [OldSlot, NewSlot]),
	%(((NewSlot - 1) * ?SLOTLENGTH) + ?SENDTIMEOFFSET) - TimeToGetDataEnd;
	((NewSlot - 1) * ?SLOTLENGTH) - TimeToGetDataEnd;
calcSendTime(NewSlot, OldSlot, TimeToGetDataEnd) ->
	debug("calculate sendtime", ?DEBUG),
	%wenn Slot-Nummerieung bei 1 beginnt, muss einer abgezogen werden
	%io:format("oldslot: ~p newslot: ~p~n", [OldSlot, NewSlot]),
	logging(?LOGFILE, lists:flatten(io_lib:format("2newslot ~p oldslot ~p~n", [NewSlot, OldSlot]))),
	%(((25 - OldSlot) * ?SLOTLENGTH) + ((NewSlot - 1) * ?SLOTLENGTH) + ?SENDTIMEOFFSET) - TimeToGetDataEnd.
	((25 - OldSlot) * ?SLOTLENGTH) + ((NewSlot - 1) * ?SLOTLENGTH) - TimeToGetDataEnd.

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
	io:format("starter_module: ~p~n", [Text]);
debug(_Text, false) ->
	ok.
