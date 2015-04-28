-module(ks).
-export([startKoordinator/0]).
-import(koordinator, [start/0]).

startKoordinator() ->
	spawn(koordinator, start, [])
	.
