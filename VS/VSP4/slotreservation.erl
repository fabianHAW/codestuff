-module(slotreservation).
-export([start/1]).

-define(NAME, lists:flatten(io_lib:format("slotreservation@~p", [node()]))).
-define(LOGFILE, lists:flatten(io_lib:format("~p.log", [?NAME]))).
-define(DEBUG, false).

start(SenderPID) ->
	SenderPID ! {helloSlot, self()},
	debug("send own pid to sender", ?DEBUG),
	loop([], SenderPID)
.

%Antwortet auf Anfragen von MessageGen und Sender
loop(FreeSlots, SenderPID) ->
	%Time1 = werkzeug:getUTC(),
	%werkzeug:logging("\"messagegen@'station1@hildes-stube'\".log", lists:flatten(io_lib:format("time1: ~p~n", [Time1]))),
	receive
		{slot, reset, NextSlot} ->
			loop([NextSlot], SenderPID);
		totalReset ->
			loop([], SenderPID);
		{slot, NextSlot} ->
			loop(lists:append(FreeSlots, [NextSlot]), SenderPID);
		{getSlot, MessageGenPID} ->
			%Time2Temp = werkzeug:getUTC(),
			%Time2 = Time2Temp - Time1,
			%werkzeug:logging("\"messagegen@'station1@hildes-stube'\".log", lists:flatten(io_lib:format("time2: ~p~n", [Time2]))),
			{FreeSlotsNew, NextSlot} = getNewSlot(FreeSlots),
			MessageGenPID ! {nextSlot, NextSlot},
			%werkzeug:logging("\"messagegen@'station1@hildes-stube'\".log", lists:flatten(io_lib:format("time3: ~p~n", [werkzeug:getUTC() - Time2Temp]))),
			loop(FreeSlotsNew, SenderPID);
		{collision, Slot} ->
			%werkzeug:logging("\"messagegen@'station1@hildes-stube'\".log", lists:flatten(io_lib:format("FreeSlots: ~p Slot: ~p~n", [FreeSlots, Slot]))),
			sendCollisionAnswer(FreeSlots, Slot, SenderPID),
			loop(FreeSlots, SenderPID);
		kill ->
			kill()
	end
.

%Sendet den nächsten freien Slot an MessageGen oder {nextSlot, nok},
%wenn kein freier Slot zur Verfügung steht.
getNewSlot([]) ->
	{[], nok};
getNewSlot(List) ->
	%Rand = random:uniform(length(List)),
	%io:format("*************rand ~p~n", [Rand]),
	%Time1 = werkzeug:getUTC(),
	Next = lists:nth(crypto:rand_uniform(1, length(List)), List),
	%Time2Temp = werkzeug:getUTC(),
	%Time2 = Time2Temp - Time1,
	%werkzeug:logging("\"messagegen@'station1@hildes-stube'\".log", lists:flatten(io_lib:format("time22: ~p~n", [Time2]))),
	ListNew = lists:delete(Next, List),
	%werkzeug:logging("\"messagegen@'station1@hildes-stube'\".log", lists:flatten(io_lib:format("time33: ~p~n", [werkzeug:getUTC() - Time2Temp]))),
	{ListNew, Next}
.

%Sendet {collision, true} an den Sender, wenn der Slot in dem
%der Sender sende möchte, nicht in der Liste der freien Slots ist,
%sonst {collision, false}
sendCollisionAnswer([], _Slot, SenderPID) ->
	%werkzeug:logging("\"messagegen@'station1@hildes-stube'\".log", lists:flatten(io_lib:format("true~n", []))),
	SenderPID ! {collision, true};
sendCollisionAnswer([First | _Rest], Slot, SenderPID) when First == Slot->
	%werkzeug:logging("\"messagegen@'station1@hildes-stube'\".log", lists:flatten(io_lib:format("false~n", []))),
	SenderPID ! {collision, false};
sendCollisionAnswer([_First | Rest], Slot, SenderPID) ->
	sendCollisionAnswer(Rest, Slot, SenderPID)
.	

		
debug(Text, true) ->
	io:format("starter_module: ~p~n", [Text]);
debug(_Text, false) ->
	ok.
%TODO: Log
kill() ->
	debug("Shutdown Slotreservation ~n", ?DEBUG)
.
