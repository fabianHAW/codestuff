-module(sender).
-export([start/5]).
-import(werkzeug, [logging/2, openSe/2, openSeA/2, concatBinary/4, createBinaryT/1]).

-define(NAME, lists:flatten(io_lib:format("sender@~p", [node()]))).
-define(LOGFILE, lists:flatten(io_lib:format("~p.log", [?NAME]))).
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
	%passiv
	%Socket = openSe(HostAddress, SendPort),
	%aktiv
	Socket = openSeA(HostAddress, SendPort),
	%Socket = openSeA({127,0,0,2}, SendPort),
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
			Time1 = werkzeug:getUTC(),
			CheckedSlot = checkSlot(OldSlot, SlotReservationPID),
			Time2Temp = werkzeug:getUTC(),
			Time2 = Time2Temp - Time1,
			logging(?LOGFILE, lists:flatten(io_lib:format("Time2 ~p ~n", [Time2]))),
			case CheckedSlot of
				true ->
					debug("detected collision", ?DEBUG);
				false ->
					TimeSyncPID ! {getTime, self()},
					receive
						{currentTime, Timestamp} ->
							debug("received new timestamp", ?DEBUG)
					end,
					logging(?LOGFILE, lists:flatten(io_lib:format("Time3 ~p ~n", [werkzeug:getUTC() - Time2Temp]))),
					
					sendMulticast(Message, Socket, MulticastAddr, ReceivePort, Timestamp)
			end,
			Killed = false;
		{nomessage} ->
			debug("sendtime expired", ?DEBUG),
			%logging(?LOGFILE, lists:flatten(io_lib:format("sendtime expired~n", []))),
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
	logging(?LOGFILE, lists:flatten(io_lib:format("time at sender ~p ~n", [werkzeug:getUTC()]))),
	gen_udp:send(Socket, MulticastAddr, ReceivePort, concatBinary(StationClass, Data, Slot, createBinaryT(Timestamp))),
	logging(?LOGFILE, lists:flatten(io_lib:format("package send to multicast ~p ~p ~p ~p ~n", [StationClass, Slot, Data, Timestamp]))).

	
debug(Text, true) ->
	io:format("starter_module: ~p~n", [Text]);
debug(_Text, false) ->
	ok.
