-module(koordinatorTest).
-export([testCommands/0, testPing/0]).
-import(ks, [startK/0, startN/0, startS/0]).

testPing() ->
	io:format("~p ~n", [net_adm:ping(chef)]),
	io:format("~p ~n", [net_adm:ping(ns)]),
	io:format("~p ~n", [net_adm:ping(starter)]),
	io:format("~p ~n", [net_adm:ping(test)]).
	
testCommands() ->
	spawn(ns, ks, startN, []),
	timer:sleep(1000),
	Pid = spawn(chef, ks, startK, []),
	timer:sleep(1000),	
	spawn(starter, ks, startS, [1]).
