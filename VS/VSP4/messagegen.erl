-module(messagegen).
-export([start/2]).
-import(werkzeug, [logging/2, createBinaryS/1, createBinaryD/1, createBinaryNS/1, getUTC/0]).

-define(NAME, lists:flatten(io_lib:format("messagegen@~p", [node()]))).
-define(LOGFILE, lists:flatten(io_lib:format("log/~p.log", [?NAME]))).
-define(DEBUG, false).
-define(SENDTIMEOFFSET, 10).
%Anforderungs-Nr.: 9.1
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
	
%Hauptschleife in der Berechnet wird, wie lange eine Station warten muss, bis sie senden darf.
%Ebenso die Nachricht soweit vorbereiten, dass sie kurz vor dem Senden an den Sender übermittelt werden kann
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
	
	Timestamp = getUTC(),
	waitSendtime(Sendtime),
	Time = getUTC() - Timestamp,
	
	%Sendtime expired?
	case Sendtime =< (Time + ((?SLOTLENGTH / 2) - ?SENDTIMEOFFSET)) of
		false ->
			debug("sendtime expired", ?DEBUG),
			{NextSlot, KilledNewNew} = getNextSlot(SlotReservationPID, KilledNew),
			SenderPID ! {nomessage};
		true ->
			{NextSlot, KilledNewNew} = getNextSlot(SlotReservationPID, KilledNew),
			Message = {MessageClass, createBinaryNS(NextSlot), MessageData},
			debug("transfer message to sender", ?DEBUG),
			SenderPID ! {message, Message, NewSlot, NextSlot}
	end,
	
	loop(PufferPID, SenderPID, StationClass, TimeSyncPID, SlotReservationPID, NewSlot, NextSlot, KilledNewNew);
loop(_PufferPID, _SenderPID, _StationClass, _TimeSyncPID, _SlotReservationPID, _OldSlot, _NewSlot, true) ->
	debug("killed", ?DEBUG).

%Realisiert den Puffer zwischen Datenquelle und der Messagegen. Puffer enthält immer die aktuellen Nutzdaten.
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

%Anforderungs-Nr.: 8.0
%Erhält die Nutzdaten aus der Datenquelle und legt sie in den Puffer.
getDataFromSource(PufferPID) ->
	Data = io:get_chars("", 24),
	PufferPID ! {data, Data},
	debug("send new data to puffer", ?DEBUG),
	getDataFromSource(PufferPID).

%Wartet auf die PIDs des TimeSync-Prozesses und des SlotReservation-Prozesses.
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

%Holt den ersten initialen Slot vom SlotReservation-Prozess.
getInitialSlot() ->
	receive 
		{initialSlot, NewSlot} ->
			debug("received initial slot", ?DEBUG);
		{nextSlot, NewSlot} ->
			debug("received next slot", ?DEBUG)
	end,
	NewSlot.

%Holt den nächsten Slot in dem im nächsten Frame gesendet werden soll.
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

%Errechnet die Sendezeit, wann der Sender-Prozess geweckt werden muss.
calcSendTime(NewSlot, 0, _CurrentTimestamp, TimeToGetDataEnd) ->
	debug("calculate sendtime", ?DEBUG),
	(((NewSlot - 1) * ?SLOTLENGTH) + ?SENDTIMEOFFSET) - TimeToGetDataEnd;
calcSendTime(NewSlot, _OldSlot, CurrentTimestamp, TimeToGetDataEnd) ->
	debug("calculate sendtime", ?DEBUG),
	%Anforderungs-Nr.: 2.1
	(1000 - getSecInMilli(CurrentTimestamp) + ?SENDTIMEOFFSET) + (((NewSlot - 1) * ?SLOTLENGTH) + ?SENDTIMEOFFSET) - TimeToGetDataEnd.

getSecInMilli(Timestamp) ->
	list_to_integer(string:substr(integer_to_list(Timestamp), 10, 4)) rem 1000.

%Legt sich solange schlafen bis die Sendezeit vorüber ist.
waitSendtime(Sendtime) ->
	debug("wait sendtime", ?DEBUG),
	timer:sleep(Sendtime).
	
%Bereitet die zu sendende Nachricht vor.
prepareMessage(StationClass, PufferPID, Killed) ->
	PufferPID ! getdata,
	receive 
		%Anforderungs-Nr.: 8.1
		%liest die aktuellen Daten aus dem Puffer
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
