-module(ks).
-export([startK/0, startN/0, startS/0]).


startK() ->
	spawn(koordinator, start, [])
	.
	
startN() ->
	spawn(nameservice, start, [])
	.
	
startS() ->
	spawn(starter, start, [1])
	.		
