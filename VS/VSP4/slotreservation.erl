-module(slotreservation).
-export([start/1]).

-define(NAME, "slotreservation").
-define(LOGFILE, lists:flatten(io_lib:format("log/~p.log", [?NAME]))).
-define(DEBUG, true).

start(SenderPID) ->
	SenderPID ! {helloSlot, self()},
	debug("send own pid to sender", ?DEBUG),
	loop([], SenderPID)
.

%Antwortet auf Anfragen von MessageGen und Sender
loop(FreeSlots, SenderPID) ->
	receive
		{slot, NextSlot} ->
			loop(lists:append(FreeSlots, [NextSlot]), SenderPID);
		{getSlot, MessageGenPID} ->
			{FreeSlotsNew, NextSlot} = getNewSlot(FreeSlots),
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
getNewSlot([Next | Rest]) ->
	{Rest, Next}
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
	io:format("slotreservation_module: ~p~n", [Text]).

%TODO: Log
kill() ->
	debug("Shutdown Slotreservation ~n", ?DEBUG)
.
