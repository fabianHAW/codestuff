-module(station1).
-export([start/1, startA/1]).

-define(MULTICASTADDRESS, {127,0,0,1}).
-define(OWNADDRESS, {127,0,0,2}).
-define(MULTICASTPORT, 15001).
-define(OWNPORT, 15002).
-define(TIMETOSLEEP, 1000).

start(0) ->
	finished;
start(Counter) ->
	Socket = werkzeug:openSe(?OWNADDRESS, ?OWNPORT),
	%gen_udp:controlling_process(Socket, self()),
	%timer:apply_after(2000, gen_udp, close, [Socket]),
	gen_udp:send(Socket, ?MULTICASTADDRESS, ?MULTICASTPORT, "hallo"),
	timer:sleep(?TIMETOSLEEP),
	gen_udp:close(Socket),
	start(Counter - 1).

startA(0) ->
	finished;
startA(Counter) ->
	Socket = werkzeug:openSeA(?OWNADDRESS, ?OWNPORT),
	%gen_udp:controlling_process(Socket, self()),
	%timer:apply_after(2000, gen_udp, close, [Socket]),
	gen_udp:send(Socket, ?MULTICASTADDRESS, ?MULTICASTPORT, "hallo"),
	timer:sleep(?TIMETOSLEEP),
	gen_udp:close(Socket),
	startA(Counter - 1).
