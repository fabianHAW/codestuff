-module(messageSendTimer).
-export([changeSendInterval/1]).

%Bildet bei dem Aufruf von changeSendInterval/2 einen Random-Wert von 1 oder 2
%1 := soll SendInterval verkleinern
%2 := soll SendInterval vergroessern
changeSendInterval(SendIntervalOld) ->
	SendIntervalNew = changeSendInterval(SendIntervalOld, random:uniform(2)),
	if
		SendIntervalNew < 2 -> 
			2;
		SendIntervalNew >= 2 ->
			SendIntervalNew
	end.

changeSendInterval(SendIntervalOld, 1) ->
	round(SendIntervalOld - SendIntervalOld / 2);
changeSendInterval(SendIntervalOld, 2) ->
	round(SendIntervalOld + SendIntervalOld / 2).
