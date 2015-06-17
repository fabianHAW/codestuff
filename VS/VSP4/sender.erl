-module(sender).
-export([start/5]).
-import(werkzeug, [openSe/2, openSeA/2, concatBinary/4, createBinaryT/1]).

-define(NAME, "sender").
-define(LOGFILE, lists:flatten(io_lib:format("log/~p.log", [?NAME]))).
-define(DEBUG, true).

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
	%passiv
	%Socket = openSe(HostAddress, SendPort),
	%aktiv
	Socket = openSeA(HostAddress, SendPort),
	gen_udp:controlling_process(Socket, self()),
	
	%auf Receiver-Anfrage warten
	receive 
		{getPID, ReceiverPID} ->
			ReceiverPID ! {pid, MessageGenPID},
			debug("send messagegenpid to receiver", ?DEBUG)
	end,
	
	loop(Socket, HostAddress, MulticastAddr, ReceivePortNew, TimeSyncPID, SlotReservationPID, false),
	gen_udp:close(Socket),
	MessageGenPID ! kill,
	debug("messagegen killed", ?DEBUG),
	debug("sender terminated", ?DEBUG).

loop(Socket, HostAddress, MulticastAddr, ReceivePort, TimeSyncPID, SlotReservationPID, false) ->
	receive 
		{message, Message, OldSlot} ->		
			debug("received new message", ?DEBUG),
			%{_StationClass, Slot, _Data} = Message,
			%CheckedSlot = checkSlot(lists:nth(1, binary_to_list(Slot)), SlotReservationPID),
			CheckedSlot = checkSlot(OldSlot, SlotReservationPID),
			case CheckedSlot of
				true ->
					debug("detected collision", ?DEBUG);
				false ->
					TimeSyncPID ! {getTime, self()},
					receive
						{currentTime, Timestamp} ->
							debug("received new timestamp", ?DEBUG)
					end,
					
					sendMulticast(Message, Socket, MulticastAddr, ReceivePort, Timestamp)
			end,
			Killed = false;
		{nomessage} ->
			debug("sendtime expired", ?DEBUG),
			Killed = false;
		kill ->
			Killed = true
	end,
	loop(Socket, HostAddress, MulticastAddr, ReceivePort, TimeSyncPID, SlotReservationPID, Killed);
loop(_Socket, _HostAddress, _MulticastAddr, _ReceivePort, _TimeSyncPID, _SlotReservationPID, true) ->
	debug("killed", ?DEBUG).

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


getHostAddress(InterfaceName) ->
	{ok, IfAddr} = inet:getifaddrs(),
	{_Interface, Addresses} = lists:keyfind(atom_to_list(InterfaceName), 1, IfAddr),
	{addr, HostAddress} = lists:keyfind(addr, 1, Addresses),
	HostAddress.
	
checkSlot(Slot, SlotReservationPID) ->
	SlotReservationPID ! {collision, Slot},
	receive
		{collision, CheckedSlot} ->
			debug("received collisiondetection", ?DEBUG)
	end,
	CheckedSlot.
	
sendMulticast({StationClass, Slot, Data}, Socket, MulticastAddr, ReceivePort, Timestamp) ->
	debug("send multicast", ?DEBUG),
	io:format("~p ~p ~p~n", [Socket, MulticastAddr, ReceivePort]),
	Result = gen_udp:send(Socket, MulticastAddr, ReceivePort, concatBinary(StationClass, Data, Slot, createBinaryT(Timestamp))),
	io:format("~p~n", [Result]).

	
debug(Text, true) ->
	io:format("sender_module: ~p~n", [Text]).
