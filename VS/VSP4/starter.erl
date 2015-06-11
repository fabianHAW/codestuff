-module(starter).
-export([start/6]).

-define(NAME, "starter").
-define(LOGFILE, lists:flatten(io_lib:format("log/~p.log", [?NAME]))).
-define(DEBUG, true).


start(InterfaceName, MulticastAddr, ReceivePort, StationClass, UtcOffsetMs, StationNumber) ->
	SenderPID = spawn(sender, start, [InterfaceName, MulticastAddr, ReceivePort, StationNumber]),
	debug("sender spawned", ?DEBUG),
	ReceiverPID = spawn(receiver, start, [InterfaceName, MulticastAddr, ReceivePort, SenderPID, StationClass, UtcOffsetMs]),
	debug("receiver spawned", ?DEBUG),
	Name = list_to_atom(?NAME ++ integer_to_list(StationNumber)),
	registerAtLocalNameservice(Name),
	debug("waiting for kill-command", ?DEBUG),
	%receive
	%	kill ->
	%		SenderPID ! kill,
	%		ReceiverPID ! kill
	%end.
	unregisterAtLocalNameservice(Name).
	

registerAtLocalNameservice(Name) ->
	register(Name, self()),
	debug("starter registered", ?DEBUG).
	
unregisterAtLocalNameservice(Name) ->
	unregister(Name),
	debug("starter unregistered", ?DEBUG).
		
debug(Text, true) ->
	io:format("starter_module: ~p~n", [Text]).
