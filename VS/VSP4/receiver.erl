-module(receiver).
-import(werkzeug, [getUTC/0, openSe/2, openSeA/2, openRec/3, openRecA/3, logging/2, reset_timer/3]).
-export([delivery/2, init/6, start/6]).

-define(NAME, lists:flatten(io_lib:format("receiver@~p", [node()]))).
-define(LOGFILE, lists:flatten(io_lib:format("log/~p.log", [?NAME]))).
-define(DEBUG, false).

start(InterfaceName, MulticastAddr, ReceivePort, SenderPID, StationClass, UtcOffsetMs) ->
	%Diese Methode benoetigt beim ersten Aufruf einige ms die das Senden
	%und wuerde beim ersten Aufruf von mehreren Prozessen zu eine fast 
	%identischen Zeit den selben Slot erzeugen
	crypto:rand_uniform(1, 26),
	TimeSyncPID = spawn(timesync, start, [StationClass, UtcOffsetMs, SenderPID]),
	debug("timesync spawned", ?DEBUG),
	SlotReservationPID = spawn(slotreservation, start, [SenderPID]),
	debug("slotreservation spawned", ?DEBUG),
	ReceiverDeliveryPID = spawn(receiver, delivery, [SlotReservationPID, TimeSyncPID]),
	spawn(receiver, init, [InterfaceName, MulticastAddr, string:to_integer(atom_to_list(ReceivePort)), ReceiverDeliveryPID, TimeSyncPID, SenderPID]).

debug(Text, true) ->
	io:format("receiver_module: ~p~n", [Text]);
debug(_Text, false) ->
	ok.

%Initialisiert den Socket und geht dann in die Schleife.
init(InterfaceName, MulticastAddr, {ReceivePort, _}, ReceiverDeliveryPID, TimeSyncPID, SenderPID) ->
	HostAddress = getHostAddress(InterfaceName),
	Socket = openRecA(MulticastAddr, HostAddress, ReceivePort),
	gen_udp:controlling_process(Socket, self()),
	SlotsUsed = initSlotPositions(24),
	
	SenderPID ! {getPID, self()},
	receive
		{pid, MessageGenPID} ->
			debug("MessageGenPID received", ?DEBUG)
	end,

	T = getUTC(),
	AdditionalTimeToWait =  1000 - (T rem 1000),
	timer:send_after(AdditionalTimeToWait, self(), startInitialListen),
	wait(),
	
	TimeSyncPID ! {getTime, self()},
	receive 
		{currentTime, CurrentTimestamp} ->
			debug("received currentTime", ?DEBUG)
	end,

	loopInitial(Socket, SlotsUsed, ReceiverDeliveryPID, TimeSyncPID, MessageGenPID, [], CurrentTimestamp, 0).
	
%Warten bis Zeitdifferenz zu 1 Sekunde abgelaufen ist.
wait() ->
	receive
		startInitialListen ->
			ok
	end.

%Initialisert die Liste mit den Slot-Positionen.
%Hierbei gilt: Slot-Position = Slot-Nr.
initSlotPositions(NumPos) ->
	initSlotPositions([], NumPos, 0).

%Initialisert die Liste mit den Slot-Positionen.
%Hierbei gilt: Slot-Position = Slot-Nr.
initSlotPositions(SlotsUsed, NumPos, Counter) when NumPos >= Counter ->
	initSlotPositions(lists:append(SlotsUsed, [0]), NumPos, Counter + 1);
initSlotPositions(SlotsUsed, _NumPos, _Counter) ->
	SlotsUsed.

%Anforderungs-Nr.: 1; 
%1 Sekunde lang zuhören, Slots sammeln, dann freie Slots an die Slotreservation senden.
loopInitial(Socket, SlotsUsed, ReceiverDeliveryPID, TimeSyncPID, MessageGenPID, PacketList, OldTimestamp, Collisions) ->
	receive
		{udp, _ReceiveSocket, _Address, _Port, Packet} ->
			TimeSyncPID ! {getTime, self()},
			receive 
			    {currentTime, CurrentTimestamp} ->
				debug("received currentTime", ?DEBUG)
			end,
			
			case (CurrentTimestamp - OldTimestamp) > 999 of
				true ->
					TimeSyncPID ! {nextFrame},
					ReceiverDeliveryPID ! totalResetSlotreservation,
					{SlotsUsedNew, PacketListNew} = listenAnalyse([], Packet, TimeSyncPID, ReceiverDeliveryPID, SlotsUsed, OldTimestamp),
					
					synchronize(PacketListNew, TimeSyncPID),
					CollisionsNew = synchronizeSlot(PacketListNew, ReceiverDeliveryPID, Collisions),
					
					ReceiverDeliveryPID ! {sendInitialSlot, MessageGenPID},
					
					CurrentTimestampNew = CurrentTimestamp - CurrentTimestamp rem 1000,
					loop(Socket, SlotsUsedNew, ReceiverDeliveryPID, TimeSyncPID, MessageGenPID, [], CurrentTimestampNew, CollisionsNew);
				false ->
					{SlotsUsedNew, PacketListNew} = listenAnalyse(PacketList, Packet, TimeSyncPID, ReceiverDeliveryPID, SlotsUsed, OldTimestamp),
					
					synchronize(PacketListNew, TimeSyncPID),
					CollisionsNew = synchronizeSlot(PacketListNew, ReceiverDeliveryPID, Collisions),
					loopInitial(Socket, SlotsUsedNew, ReceiverDeliveryPID, TimeSyncPID, MessageGenPID, PacketListNew, OldTimestamp, CollisionsNew)
			end;
		kill ->
			kill(Socket, ReceiverDeliveryPID, TimeSyncPID) 
	after
		1000 ->
			InitialSlot = crypto:rand_uniform(1, 26),
			
			ReceiverDeliveryPID ! totalReset,
			MessageGenPID ! {initialSlot, InitialSlot},
			
			TimeSyncPID ! {getTime, self()},
			receive 
			    {currentTime, CurrentTimestamp} ->
					debug("received currentTime", ?DEBUG)
			end,
			
			CurrentTimestampNew = CurrentTimestamp - CurrentTimestamp rem 1000,
			loop(Socket, SlotsUsed, ReceiverDeliveryPID, TimeSyncPID, MessageGenPID, [], CurrentTimestampNew, Collisions)
	end.

loop(Socket, SlotsUsed, ReceiverDeliveryPID, TimeSyncPID, MessageGenPID, PacketList, OldTimestamp, Collisions) ->

	receive
		{udp, _ReceiveSocket, _Address, _Port, Packet} ->
			TimeSyncPID ! {getTime, self()},
			receive 
			    {currentTime, CurrentTimestamp} ->
					debug("received currentTime", ?DEBUG)
			end,
			case (CurrentTimestamp - OldTimestamp) > 999 of
				true ->
				    TimeSyncPID ! {nextFrame},
					ReceiverDeliveryPID ! totalResetSlotreservation,
					{SlotsUsedNew, PacketListNew} = listenAnalyse([], Packet, TimeSyncPID, ReceiverDeliveryPID, SlotsUsed, OldTimestamp),
					  
					synchronize(PacketListNew, TimeSyncPID),
					CollisionsNew = synchronizeSlot(PacketListNew, ReceiverDeliveryPID, Collisions),
					
					CurrentTimestampNew = CurrentTimestamp - CurrentTimestamp rem 1000,
					loop(Socket, SlotsUsedNew, ReceiverDeliveryPID, TimeSyncPID, MessageGenPID, [], CurrentTimestampNew, CollisionsNew);
				false ->
					{SlotsUsedNew, PacketListNew} = listenAnalyse(PacketList, Packet, TimeSyncPID, ReceiverDeliveryPID, SlotsUsed, OldTimestamp),
					
					synchronize(PacketListNew, TimeSyncPID),
					CollisionsNew = synchronizeSlot(PacketListNew, ReceiverDeliveryPID, Collisions),
					loop(Socket, SlotsUsedNew, ReceiverDeliveryPID, TimeSyncPID, MessageGenPID, PacketListNew, OldTimestamp, CollisionsNew)
			end;
		kill ->
			kill(Socket, ReceiverDeliveryPID, TimeSyncPID) 
	end.

listenAnalyse(PacketList, Packet, TimeSyncPID, ReceiverDeliveryPID, SlotsUsed, OldTimestamp) ->
	{StationTyp, _Paylod, Slot, Timestamp} = message_to_string(Packet),

	SlotCount = timeCalc(OldTimestamp, TimeSyncPID),
	ReceiverDeliveryPID ! {delete, SlotCount},
	
	SlotsUsedNew = insertInSlotsUsed(SlotsUsed, Slot),
	PacektListNew = lists:append(PacketList, [{SlotCount, StationTyp, Timestamp, Slot}]),
	{SlotsUsedNew, PacektListNew}.
	
timeCalc(OldTimestamp, TimeSyncPID) ->
	TimeSyncPID ! {getTime, self()},
	receive 
		{currentTime, CurrentTimestamp} ->
			debug("received currentTime", ?DEBUG)
	end,
	trunc(((( CurrentTimestamp - OldTimestamp) rem 1000) / 40) + 1).

synchronize([], _TimeSyncPID) ->
	ok;
synchronize([First | Rest], TimeSyncPID) ->
		{SendFlag, List} = synchronize(First, Rest, searchCollisions),
		synchronize(SendFlag, First, TimeSyncPID, send),
		synchronize(List, TimeSyncPID).

synchronize(First, [], searchCollisions) ->
	{true, []};
synchronize(First, [Head | Tail], searchCollisions) when element(1, Head) == element(1, First)->
		{false, skip(element(1, First), Tail)};
synchronize(First, [Head | Tail], searchCollisions) when element(1, Head) /= element(1, First)->		
		{true, lists:append([Head], Tail)}.

synchronize(true, First, TimeSyncPID, send) ->
	{_SlotCount, StationTyp, Timestamp, _Slot} = First,
	TimeSyncPID ! {times, StationTyp, Timestamp};
synchronize(false, _First, _TimeSyncPID, send) ->
	ok.

%Anforderungs-Nr.: 5.1; 
synchronizeSlot([], _ReceiverDeliveryPID, Collisions) ->
	Collisions;
synchronizeSlot([First | Rest], ReceiverDeliveryPID, Collisions) ->
	{SendFlag, List, CollisionsNew} = synchronizeSlot(First, Rest, searchCollisions, Collisions, check),
	synchronizeSlot(SendFlag, First, ReceiverDeliveryPID, send),
	synchronizeSlot(List, ReceiverDeliveryPID, CollisionsNew).
		
synchronizeSlot(First, [], searchCollisions, Collisions, check) ->
	{true, [], Collisions};
synchronizeSlot(First, [Head | Tail], searchCollisions, Collisions, check) when element(1, Head) == element(1, First)->
	{false, skip(element(1, First), Tail), Collisions};
synchronizeSlot(First, [Head | Tail], searchCollisions, Collisions, check) when element(1, Head) /= element(1, First)->		
	%Anforderungs-Nr.: 6.0; 
	CollisionsNew = Collisions + 1,
	logging(?LOGFILE, lists:flatten(io_lib:format("Kollision erkannt. Anzahl ist nun: ~p~n", [CollisionsNew]))),
	{true, lists:append([Head], Tail), CollisionsNew}.

synchronizeSlot(true, First, ReceiverDeliveryPID, send) ->
	{_SlotCount, StationTyp, _Timestamp, Slot} = First,
	ReceiverDeliveryPID ! {slotUsed, Slot};
synchronizeSlot(false, _First, _ReceiverDeliveryPID, send) ->
	ok.

skip(_Slot, []) ->
	[];
skip(Slot, [Head | Tail]) when element(1, Head) == Slot ->
	skip(Slot, Tail);
skip(Slot, [Head | Tail]) ->
	Tail.


insertInSlotsUsed(SlotsUsed, SlotNumber) ->
		insertInSlotsUsed(SlotsUsed, SlotNumber, 1).

insertInSlotsUsed([], _SlotNumber, _Counter) ->
	[];
insertInSlotsUsed([First | Rest], SlotNumber, Counter) when SlotNumber == Counter ->
	lists:append([First + 1], Rest);
insertInSlotsUsed([First | Rest], SlotNumber, Counter) ->
		lists:append([First], insertInSlotsUsed(Rest, SlotNumber, Counter + 1)).
	
kill(Socket, ReceiverDeliveryPID, TimeSyncPID) ->
	gen_udp:close(Socket),
	ReceiverDeliveryPID ! kill,
	debug("send kill to receiver services~n", ?DEBUG),
	TimeSyncPID ! kill,
	debug("send kill to timesync~n", ?DEBUG),
	debug("Shutdown Receiver~n", ?DEBUG).

message_to_string(Packet) ->
	List = binary:bin_to_list(Packet),
	StationTyp = lists:nth(1, List),
	Paylod = erlang:binary_to_list(Packet,2,25),
	Slot = lists:nth(1, erlang:binary_to_list(Packet,26,26)),
	Timestamp = erlang:binary_to_list(Packet,27,34),
	{StationTyp, Paylod, Slot, Timestamp}.

%%%%%%%%%%%%Receiver Services%%%%%%%%%%%%%
delivery(SlotReservationPID, TimeSyncPID) ->
	receive
		{slot, reset, NextSlot} ->
			SlotReservationPID ! {slot, reset, NextSlot},
			delivery(SlotReservationPID, TimeSyncPID);
		{slotUsed, NextSlot} ->
			SlotReservationPID ! {slotUsed, NextSlot},
			delivery(SlotReservationPID, TimeSyncPID);
		{slotUnUsed, NextSlot} ->
			SlotReservationPID ! {slotUnUsed, NextSlot},
			delivery(SlotReservationPID, TimeSyncPID);
		{times, StationClass, TimeInSlot} ->
			TimeSyncPID ! {times, StationClass, TimeInSlot},
			delivery(SlotReservationPID, TimeSyncPID);
		{sendInitialSlot, MessageGenPID} ->
			SlotReservationPID ! {getSlot, MessageGenPID},
			delivery(SlotReservationPID, TimeSyncPID);
		totalResetSlotreservation ->
			SlotReservationPID ! totalReset,
			delivery(SlotReservationPID, TimeSyncPID);
		{delete, Slot} ->
			 SlotReservationPID ! {delete, Slot},
			delivery(SlotReservationPID, TimeSyncPID);
		kill ->
			SlotReservationPID ! kill,
			debug("send kill to slotreservation~n",?DEBUG),
			debug("receiver services terminated~n",?DEBUG)
	end.

getHostAddress(InterfaceName) ->
	{ok, IfAddr} = inet:getifaddrs(),
	{_Interface, Addresses} = lists:keyfind(atom_to_list(InterfaceName), 1, IfAddr),
	{addr, HostAddress} = lists:keyfind(addr, 1, Addresses),
	HostAddress.
			
