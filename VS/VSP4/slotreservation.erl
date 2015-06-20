-module(slotreservation).
-export([start/1]).

-define(NAME, lists:flatten(io_lib:format("slotreservation@~p", [node()]))).
-define(LOGFILE, lists:flatten(io_lib:format("log/~p.log", [?NAME]))).
-define(DEBUG, false).

start(SenderPID) ->
	SenderPID ! {helloSlot, self()},
	debug("send own pid to sender", ?DEBUG),
	loop([], [], SenderPID, 0).

%Anforderungs-Nr.: 3.0; 3.1; 3.2
%Antwortet auf Anfragen von MessageGen und Sender
loop(FreeSlots, SlotsUsed, SenderPID, OwnNextSlot) ->
	receive
		{slot, reset, NextSlot} ->
			loop(getInverseList([NextSlot]), [], SenderPID, OwnNextSlot);
		totalReset ->
			loop(lists:delete(OwnNextSlot, getInverseList(SlotsUsed)), [], SenderPID, OwnNextSlot);
		{slotUsed, NextSlot} ->
			case lists:member(NextSlot, SlotsUsed) of
				true ->
					loop(FreeSlots, SlotsUsed, SenderPID, OwnNextSlot);
				false ->
					loop(FreeSlots, lists:append(SlotsUsed, [NextSlot]), SenderPID, OwnNextSlot)
			end;
		{slotUnUsed, NextSlot} ->
			case lists:member(NextSlot, FreeSlots) of
				true ->
					loop(FreeSlots, SlotsUsed, SenderPID, OwnNextSlot);
				false ->
					loop(lists:append(FreeSlots, [NextSlot]), SlotsUsed, SenderPID, OwnNextSlot)
			end;
		{getSlot, MessageGenPID} ->
			NextSlot = getNewSlot(getInverseList(SlotsUsed)),
			MessageGenPID ! {nextSlot, NextSlot},
			loop(FreeSlots, lists:append(SlotsUsed, [NextSlot]), SenderPID, NextSlot);
		{collision, Slot, NextSlot} ->
			sendCollisionAnswer(lists:member(Slot, FreeSlots), SenderPID),
			case lists:min(SlotsUsed) == NextSlot of
				true ->
					loop(lists:delete(OwnNextSlot, getInverseList(SlotsUsed)), [], SenderPID, OwnNextSlot);
				false ->	
					loop(FreeSlots, SlotsUsed, SenderPID, OwnNextSlot)
			end;
		{delete, Slot} ->
			loop(lists:delete(Slot, FreeSlots), SlotsUsed, SenderPID, OwnNextSlot);
		kill ->
			kill()
	end.
	
getInverseList(SlotsUsed) ->
    lists:subtract(lists:seq(1,25), SlotsUsed).


%Sendet den nächsten freien Slot an MessageGen oder {nextSlot, nok},
%wenn kein freier Slot zur Verfügung steht.
getNewSlot(List) ->
	lists:nth(crypto:rand_uniform(1, length(List)), List).

%Sendet {collision, true} an den Sender, wenn der Slot in dem
%der Sender sende möchte, nicht in der Liste der freien Slots ist,
%sonst {collision, false}
sendCollisionAnswer(true, SenderPID) ->
	SenderPID ! {collision, true};
sendCollisionAnswer(false, SenderPID) ->
	SenderPID ! {collision, false}.	

		
debug(Text, true) ->
	io:format("slotreservation_module: ~p~n", [Text]);
debug(_Text, false) ->
	ok.
	
kill() ->
	debug("Shutdown Slotreservation ~n", ?DEBUG).
