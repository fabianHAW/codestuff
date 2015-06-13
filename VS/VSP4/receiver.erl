-module(receiver).
-import(werkzeug, [getUTC/0, openSe/2, openRec/3, logging/2]).
-export([delivery/3, init/4, start/6]).

-define(NAME, "receiver").
-define(LOGFILE, lists:flatten(io_lib:format("log/~p.log", [?NAME]))).
-define(DEBUG, true).

%%%%%%%%%%%OLD&%%%%%%%%%%%%%%%%%%%%%
start(InterfaceName, MulticastAddr, ReceivePort, SenderPID, StationClass, UtcOffsetMs) ->
	TimeSyncPID = spawn(timesync, start, [StationClass, UtcOffsetMs, SenderPID]),
	debug("timesync spawned", ?DEBUG),
	SlotReservationPID = spawn(slotreservation, start, [SenderPID]),
	debug("slotreservation spawned", ?DEBUG),
	ReceiverDeliveryPID = spawn(receiver, delivery, [stationAlive, SlotReservationPID, TimeSyncPID]),
	spawn(receiver, init, [InterfaceName, ReceivePort, ReceiverDeliveryPID, TimeSyncPID])
.
	
getDataFromMulticast() ->
	ok.

debug(Text, true) ->
	io:format("receiver_module: ~p~n", [Text]).

%%%%%%%%%%%%%%%%%%%%%NEW%%%%%%%%%%%%%%%%%%%%%
%Initialisiert den Socket und geht dann in die Schleife.
init(ReceivePort, InterfaceName, ReceiverDeliveryPID, TimeSyncPID) ->
	{ok, Addr} = inet:getaddr(net_adm:localhost(), inet),
	HostAddress = getHostAddress(InterfaceName),
	Socket = openSe(HostAddress, ReceivePort),
	SlotsUsed = initSlotPositions(25),
	TimeSyncPID ! {getTime, self()},
	receive
		{currentTime, TimeStamp} ->
			spawn(receiver, loop, [0,0, SlotsUsed, Socket, ReceiverDeliveryPID, TimeSyncPID, TimeStamp, stationAlive])
	end
.

%Initialisert die Liste mit den Slot-Positionen.
%Hierbei gilt: Slot-Position = Slot-Nr.
initSlotPositions(NumPos) ->
	initSlotPositions([], NumPos, 0)
.

%Initialisert die Liste mit den Slot-Positionen.
%Hierbei gilt: Slot-Position = Slot-Nr.
initSlotPositions(SlotsUsed, NumPos, Counter) when NumPos =< Counter ->
	initSlotPositions(lists:append(SlotsUsed, [0]), NumPos, Counter + 1);
initSlotPositions(SlotsUsed, _NumPos, _Counter) ->
	SlotsUsed
.

%Stellt die Schleife dar.
%In jedem Durchlauf wird ein neues Nachrichtenpaket empfangen,
%Die Slot-Nr. extrahiert,
%entschieden ob es eine Kollision gab,
%und protokolliert.
loop(Collisions, Received, SlotsUsed, Socket, ReceiverDeliveryPID, TimeSyncPID, OldTime, stationAlive) ->
	{ok, {Address, Port, Packet}} = gen_udp:recv(Socket, 34),
	{CollisionDetected, SlotsUsedNew} = getSlotNumber(SlotsUsed, Packet),
	{CollisionsNew, ReceivdNew} = loop(CollisionDetected, Collisions, Received, Packet, ReceiverDeliveryPID, TimeSyncPID),
	TimeSyncPID ! {getTime, self()},
	receive
		{currentTime, CurrentTime} ->
			{SlotsUsedNew, NewTime} = isFrameFinished(CurrentTime, OldTime, SlotsUsed, TimeSyncPID, ReceiverDeliveryPID),
			loop(Collisions, Received, SlotsUsedNew, Socket, ReceiverDeliveryPID, TimeSyncPID, NewTime, stationAlive)
	end
.	
	
%Stellt den Teil der Schleife dar, in dem geloggt wird.
loop(corrupt, Collisions, Received, _Packet, _ReceiverDeliveryPID, TimeSyncPID) ->
	logging(?LOGFILE, lists:flatten(io_lib:format("Received: corrupted Package!~n", []))),
	{Collisions, Received};
loop(true, Collisions, Received, _Packet, _ReceiverDeliveryPID, TimeSyncPID) ->
	logging(?LOGFILE, lists:flatten(io_lib:format("Collision detected! Collisions ~p ~n", [Collisions]))),
	{Collisions + 1, Received};
loop(false, Collisions, Received, Packet, ReceiverDeliveryPID, TimeSyncPID) ->
	logging(?LOGFILE, lists:flatten(io_lib:format("Package successfully received! Received ~p ~n", [Received]))),
	analyse(Packet, ReceiverDeliveryPID, TimeSyncPID),
	{Collisions, Received + 1}.

%Berechnung korrigieren
isFrameFinished(CurrentTime, OldTime, SlotsUsed, TimeSyncPID, ReceiverDeliveryPID) when ((CurrentTime - OldTime)*1000) >= 1 ->
	TimeSyncPID ! {getTime, self()},
	sendFreeSlots(SlotsUsed, ReceiverDeliveryPID),
	
	receive
		{currentTime, CurrentTimeNew} ->
			ok
	end,
	{[], CurrentTimeNew}
.

sendFreeSlots([], _ReceiverDeliveryPID) ->
	ok;
sendFreeSlots([First | Rest], ReceiverDeliveryPID) when First == 0 ->
	ReceiverDeliveryPID ! {slot, First},
	sendFreeSlots(Rest, ReceiverDeliveryPID);
sendFreeSlots([First | Rest], ReceiverDeliveryPID) ->
	sendFreeSlots(Rest, ReceiverDeliveryPID)
.


%Extrahiert die Daten aus dem Paket und loggt den Inhalt.
analyse(Packet, ReceiverDeliveryPID, TimeSyncPID) ->
	Binary = binary:bin_to_list(Packet),
	StationClass = binary:decode_unsigned(lists:nth(1, Binary)),
	Payload = extractIntervall(Binary, 2, 25),
	NextSlot = binary:decode_unsigned(lists:nth(26, Binary)),
	TimeInSlot = extractIntervall(Binary, 27, 34),
	
	ReceiverDeliveryPID ! {slot, NextSlot},
	TimeSyncPID ! {times, StationClass, TimeInSlot},
	
	logging(?LOGFILE, lists:flatten(io_lib:format("Received Package: StationClass ~p  | Payload ~p | NextSlot ~p | Sendtime ~p ~n", [StationClass, Payload, NextSlot, TimeInSlot])))
.

%Extrahiert die Daten aus dem Paket von From bis To.	
extractIntervall(Binary, From, To) ->
	binary:decode_unsigned(extractIntervall(Binary, From, To, 1))
.

%Extrahiert die Daten aus dem Paket von From bis To.
extractIntervall([First | Rest], From, To, Counter) when Counter =< To, Counter < From ->
	extractIntervall(Rest, From, To, Counter + 1);
extractIntervall([First | Rest], From, To, Counter) when Counter =< To, Counter >= From ->
	[First] ++ extractIntervall(Rest, From, To, Counter + 1);
extractIntervall(_Rest, _From, To, Counter) when Counter > To ->
	[]
.
	
%Extrahiert die Slot-Nr. aus dem Paket und prüft
%Ob der Slot im nächsten Frame schon von einer anderen Station
%in Gebrauch sein wird.
getSlotNumber(SlotsUsed, Packet) ->
	Binary = binary:bin_to_list(Packet),
	SlotNumber = binary:decode_unsigned(lists:nth(26, Binary)),
	{willSlotBeInUse(SlotsUsed, SlotNumber), countSlotNumberUsed(SlotsUsed, SlotNumber)}
.

%Liefert true oder false zurück, je nachdem ob der Slot
%im nächsten Frame von einer anderen Station in Gebrauch sein wird.
%Im Ausnahmefall wird corrupt zurückgeliefert, wenn die Slot-Nr. aus dem Paket > 25 war.
willSlotBeInUse(SlotsUsed, SlotNumber) ->
	willSlotBeInUse(SlotsUsed, SlotNumber, 0)
.

%Liefert true oder false zurück, je nachdem ob der Slot
%im nächsten Frame von einer anderen Station in Gebrauch sein wird.
%Im Ausnahmefall wird corrupt zurückgeliefert, wenn die Slot-Nr. aus dem Paket > 25 war.
willSlotBeInUse([_First | Rest], SlotNumber, Counter) when SlotNumber < Counter ->
	willSlotBeInUse(Rest, SlotNumber, Counter + 1);
willSlotBeInUse([First | _Rest], SlotNumber, Counter) when SlotNumber == Counter, First /= 0->
	true;
willSlotBeInUse([First | _Rest], SlotNumber, Counter) when SlotNumber == Counter, First == 0->
	false;
willSlotBeInUse([], _SlotNumber, _Counter) ->
	corrupt
.

%Zählt die Anzahl der Stationen hoch, die den Slot SlotNumber im
%nächsten Frame benutzen werden.
countSlotNumberUsed(SlotsUsed, SlotNumber) ->
	countSlotNumberUsed(SlotsUsed, SlotNumber, 0)
.

%Zählt die Anzahl der Stationen hoch, die den Slot SlotNumber im
%nächsten Frame benutzen werden.
countSlotNumberUsed([First | Rest], SlotNumber, Counter) when SlotNumber < Counter ->
	[First] ++ countSlotNumberUsed(Rest, SlotNumber, Counter + 1);
countSlotNumberUsed([First | Rest], SlotNumber, Counter) when SlotNumber == Counter ->
	[First + 1] ++ Rest;
countSlotNumberUsed(Rest, _SlotNumber, _Counter) ->
	Rest
.

%%%%%%%%%%%%Receiver Services%%%%%%%%%%%%%
delivery(stationAlive, SlotReservationPID, TimeSyncPID) ->
	receive
		{slot, NextSlot} ->
			SlotReservationPID ! {slot, NextSlot},
			delivery(stationAlive, SlotReservationPID, TimeSyncPID);
		{times, StationClass, TimeInSlot} ->
			TimeSyncPID ! {times, StationClass, TimeInSlot},
			delivery(stationAlive, SlotReservationPID, TimeSyncPID)
	end
.

getHostAddress(InterfaceName) ->
	{ok, IfAddr} = inet:getifaddrs(),
	{_Interface, Addresses} = lists:keyfind(InterfaceName, 1, IfAddr),
	{addr, HostAddress} = lists:keyfind(addr, 1, Addresses),
	HostAddress.
			
