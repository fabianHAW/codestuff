-module(station2).
-export([start/1, startA/1]).

-define(MULTICASTADDRESS, {127,0,0,1}).
-define(OWNADDRESS, {127,0,0,2}).
-define(MULTICASTPORT, 15001).

start(0) ->
	finished;
start(Counter) ->
	Socket = werkzeug:openRec(?MULTICASTADDRESS, ?OWNADDRESS, ?MULTICASTPORT),
	gen_udp:controlling_process(Socket, self()),
	%timer:apply_after(2000, gen_udp, close, [Socket]),
	Result = gen_udp:recv(Socket, 0),
	io:format("Result: ~p~n", [Result]),
	gen_udp:close(Socket),
	start(Counter - 1).

startA(0) ->
	finished;
startA(Counter) ->
	Socket = werkzeug:openRecA(?MULTICASTADDRESS, ?OWNADDRESS, ?MULTICASTPORT),
	gen_udp:controlling_process(Socket, self()),
	%timer:apply_after(2000, gen_udp, close, [Socket]),
	receive
	 Result ->
		io:format("Result: ~p~n", [Result])
	end,
	gen_udp:close(Socket),
	startA(Counter - 1).
