-module(sender).
-export([start/5]).
-import(werkzeug, [logging/2, openSe/2, openSeA/2, concatBinary/4, createBinaryT/1]).

-define(NAME, lists:flatten(io_lib:format("sender@~p", [node()]))).
-define(LOGFILE, lists:flatten(io_lib:format("log/~p.log", [?NAME]))).
-define(DEBUG, false).

start(InterfaceName, MulticastAddr, ReceivePort, StationClass, StationNumber) ->
	MessageGenPID = spawn(messagegen, start, [self(), StationClass]),
	debug("messagegen spawned", ?DEBUG),
	ReceivePortNew = list_to_integer(atom_to_list(ReceivePort)),
	StationNumberNew = list_to_integer(atom_to_list(StationNumber)),
	SendPort = ReceivePortNew + StationNumberNew,
	PIDList = waitForInitialValues(MessageGenPID, []),
	
	{_TimeSyncKey, TimeSyncPID} = lists:keyfind(timesyncpid, 1, PIDList),
	{_SlotreservationKey, SlotReservationPID} = lists:keyfind(slotreservationpid, 1, PIDList),
	
	HostAddress = getHostAddress(InterfaceName),
	%aktiv
	Socket = openSeA(HostAddress, SendPort),
	gen_udp:controlling_process(Socket, self()),
	%auf Receiver-Anfrage warten
	receive 
		{getPID, ReceiverPID} ->
			ReceiverPID ! {pid, MessageGenPID},
			debug("send messagegenpid to receiver", ?DEBUG)
	end,
	
	loop(Socket, HostAddress, MulticastAddr, ReceivePortNew, TimeSyncPID, SlotReservationPID, 0, false),
	gen_udp:close(Socket),
	MessageGenPID ! kill,
	debug("messagegen killed", ?DEBUG),
	debug("sender terminated", ?DEBUG).

%Anforderungs-Nr.: 1.0
%Hauptschleife die die Nachrichten von der MessageGen annimmt und dann über den Multicast leitet,
%sofern keine weitere Station in dem jeweiligen Slot gesendet hat.
loop(Socket, HostAddress, MulticastAddr, ReceivePort, TimeSyncPID, SlotReservationPID, SendCounter, false) ->
	receive 
		{message, Message, OldSlot, NextSlot} ->		
			debug("received new message", ?DEBUG),
			CheckedSlot = checkSlot(OldSlot, NextSlot, SlotReservationPID),
			case CheckedSlot of
				true ->
					debug("detected collision", ?DEBUG),
					SendCounterNew = SendCounter,
					%Anforderungs-Nr.: 6.3
					logging(?LOGFILE, lists:flatten(io_lib:format("Es wurde in folgenden Frame NICHT gesendet: ~p~n", [SendCounterNew])));
				false ->
					TimeSyncPID ! {getTime, self()},
					receive
						{currentTime, Timestamp} ->
							debug("received new timestamp", ?DEBUG)
					end,
					sendMulticast(Message, Socket, MulticastAddr, ReceivePort, Timestamp),
					SendCounterNew = SendCounter + 1,
					%Anforderungs-Nr.: 6.2
					logging(?LOGFILE, lists:flatten(io_lib:format("Es wurde in folgenden Frame gesendet: ~p~n", [SendCounterNew])))
			end,
			Killed = false;
		{nomessage} ->
			debug("sendtime expired", ?DEBUG),
			SendCounterNew = SendCounter,
			Killed = false;
		kill ->
			SendCounterNew = SendCounter,
			Killed = true
	end,
	loop(Socket, HostAddress, MulticastAddr, ReceivePort, TimeSyncPID, SlotReservationPID, SendCounterNew, Killed);
loop(_Socket, _HostAddress, _MulticastAddr, _ReceivePort, _TimeSyncPID, _SlotReservationPID, _SendCounter, true) ->
	debug("killed", ?DEBUG).

%Wartet auf die PIDs des TimeSync-Prozesses und des SlotReservation-Prozesses.
waitForInitialValues(_MessageGenPID, PIDList) when length(PIDList) == 2 ->
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

%Ermittelt die Hostaddress des Hosts.
getHostAddress(InterfaceName) ->
	{ok, IfAddr} = inet:getifaddrs(),
	{_Interface, Addresses} = lists:keyfind(atom_to_list(InterfaceName), 1, IfAddr),
	{addr, HostAddress} = lists:keyfind(addr, 1, Addresses),
	HostAddress.

%Fragt beim SlotReservation-Prozess an, ob der jeweilige Slot frei ist, damit keine Kollision auftritt.
checkSlot(Slot, NextSlot, SlotReservationPID) ->
	SlotReservationPID ! {collision, Slot, NextSlot},
	receive
		{collision, CheckedSlot} ->
			debug("received collisiondetection", ?DEBUG)
	end,
	CheckedSlot.

%Sendet die Nachricht über den Multicast.
sendMulticast({StationClass, Slot, Data}, Socket, MulticastAddr, ReceivePort, Timestamp) ->
	debug("send multicast", ?DEBUG),
	gen_udp:send(Socket, MulticastAddr, ReceivePort, concatBinary(StationClass, Data, Slot, createBinaryT(Timestamp))).

	
debug(Text, true) ->
	io:format("sender_module: ~p~n", [Text]);
debug(_Text, false) ->
	ok.
