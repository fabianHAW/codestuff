-module(datasourcetest).
-export([start/0]).

start() ->
	Result = io:get_chars("", 24),
	%ResultAsString = binary_to_list(unicode:characters_to_binary(Result)),
	io:format("~nresult: ~s", [Result]).
