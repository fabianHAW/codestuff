-module(send_kill).
-export([start/2]).


start(From, To) ->
	%{ok, Hostname} = inet:gethostname(),
	Hostname = net_adm:localhost(),	
	loop(From, To, Hostname, "station").
	
loop(To, To, Hostname, Name) ->
	NameNew = list_to_atom(Name ++ integer_to_list(To)),
	Node = list_to_atom(Name ++ integer_to_list(To) ++ "@" ++ Hostname),
	{NameNew, Node} ! kill,
	send_kill_to_all;
loop(From, To, Hostname, Name) ->
	NameNew = list_to_atom(Name ++ integer_to_list(From)),
	Node = list_to_atom(Name ++ integer_to_list(From) ++ "@" ++ Hostname),
	{NameNew, Node} ! kill,
	start(From + 1, To).
