-module(station1).
-export([start/0]).

start() ->
	Socket = werkzeug:openSe({127,0,0,1}, 50000),
	gen_udp:controlling_process(Socket, self()),
	timer:apply_after(2000, gen_udp, close, [Socket]),
	gen_udp:send(Socket, localhost, 50001, "hallo").
