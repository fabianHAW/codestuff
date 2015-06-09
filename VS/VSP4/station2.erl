-module(station2).
-export([start/0]).

start() ->
	Socket = werkzeug:openRec({127,0,0,2}, {127,0,0,1}, 50001),
	gen_udp:controlling_process(Socket, self()),
	timer:apply_after(2000, gen_udp, close, [Socket]),
	gen_udp:recv(Socket, 0).
