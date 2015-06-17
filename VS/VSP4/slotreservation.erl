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
	receive
		{slot, reset, NextSlot} ->
			loop([NextSlot], SenderPID);
		{slot, NextSlot} ->
			loop(lists:append(FreeSlots, [NextSlot]), SenderPID);
		{getSlot, MessageGenPID} ->
			{FreeSlotsNew, NextSlot} = getNewSlot(FreeSlots),
			%io:format("free: ~p~n", [FreeSlotsNew]),
			%io:format("next: ~p~n", [NextSlot]),
			MessageGenPID ! {nextSlot, NextSlot},
			loop(FreeSlotsNew, SenderPID);
		{collision, Slot} ->
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
	Rand = crypto:rand_uniform(1, length(List)),
	%io:format("*************rand ~p~n", [Rand]),
	Next = lists:nth(Rand, List),
	ListNew = lists:delete(Next, List),
	{ListNew, Next}
.

%Sendet {collision, true} an den Sender, wenn der Slot in dem
%der Sender sende möchte, nicht in der Liste der freien Slots ist,
%sonst {collision, false}
sendCollisionAnswer([], _Slot, SenderPID) ->
	SenderPID ! {collision, true};
sendCollisionAnswer([First | _Rest], Slot, SenderPID) when First == Slot->
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
