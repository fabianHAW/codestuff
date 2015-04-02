-module(dlq).
-import(werkzeug, [logging/2, timeMilliSecond/0, get_config_value/2]).
-export([start/0, push2DLQ/3, expectedNr/1, initDLQ/2, deliverMSG/4]).

start() ->
	ok.
	
push2DLQ(D1, D2, D3) ->
	ok.

expectedNr(D1) ->
	ok.
	
initDLQ(D1, D2) ->
	ok.
	
deliverMSG(D1, D2, D3, D4) ->
	ok.

