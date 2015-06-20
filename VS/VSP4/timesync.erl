-module(timesync).
-export([start/3]).
-import(werkzeug, [getUTC/0, logging/2]).

-define(NAME, lists:flatten(io_lib:format("timesync@~p", [node()]))).
-define(LOGFILE, lists:flatten(io_lib:format("log/~p.log", [?NAME]))).
-define(DEBUG, false).


start(StationClass, UtcOffsetMs, SenderPID) ->
	SenderPID ! {helloTime, self()},
	debug("send own pid to sender", ?DEBUG),
	getNewTime(UtcOffsetMs, string:equal(StationClass, "A"))
.

		
debug(Text, true) ->
	io:format("timesync_module: ~p~n", [Text]);
debug(_Text, false) ->
	ok.
	
%Anforderungs-Nr.: 4.0 
%Ist die eigene Station von der Klasse B (false),
%Bleibt die Zeit ungenau.
%Sonst soll die Zeit synchronisiert werden.
getNewTime(UtcOffsetMs, false) ->
	inaccurate(UtcOffsetMs, getUTC());
getNewTime(UtcOffsetMs, true) ->
	accurate(UtcOffsetMs, 0, 0, 0).

%Anforderungs-Nr.: 4.4; 4.5 
%Zeit wird nicht synchronisiert, da die eigene Station von der Klasse B ist.
%Erhält Anfragen von Sender u. MessageGen, ignoriert Zeitmitteilung vom Receiver.
inaccurate(UtcOffsetMs, Time) ->
	receive 
		{times, StationClass, TimeInSlot} ->
			case string:equal(StationClass, "A") of
				true ->
					inaccurate(UtcOffsetMs, TimeInSlot);
				false ->
					inaccurate(UtcOffsetMs, Time)
			end;
		{getTime, SenderPID_MessageGenPID} ->
			SenderPID_MessageGenPID ! {currentTime, Time},
			inaccurate(UtcOffsetMs, Time);
		kill ->
			kill();
		_Any ->
			inaccurate(UtcOffsetMs, Time)
	end.

%Anforderungs-Nr.: 4.1; 4.2 
%Zeit wird synchronisiert, da die eigene Staion von der Klasse A ist.
%Erhält Anfragen von Sender u. MessageGen, berücksichtigt Zeitmitteilungen vom Receiver.
accurate(UtcOffsetMs, SyncOffsetMs, TimesReceived, FrameCounter) ->
	receive 
		{times, StationClass, TimeInSlot} ->
			SyncOffsetNew = berkley(string:equal(StationClass, "A"), TimeInSlot, SyncOffsetMs, TimesReceived + 1),
			accurate(UtcOffsetMs, SyncOffsetNew, TimesReceived + 1, FrameCounter);
		{getTime, SenderPID_MessageGenPID} ->
			SenderPID_MessageGenPID ! {currentTime, getUTC() + SyncOffsetMs + UtcOffsetMs},
			accurate(UtcOffsetMs, SyncOffsetMs, TimesReceived, FrameCounter);
		{nextFrame} ->
			{SyncOffsetMsNew, TimesReceivedNew, FrameCounterNew} = resetIfNeccessary(SyncOffsetMs, TimesReceived, FrameCounter + 1),
			accurate(UtcOffsetMs, SyncOffsetMsNew, TimesReceivedNew, FrameCounterNew);
		kill ->
			kill()
	end.
	
resetIfNeccessary(_SyncOffsetMs, _TimesReceived, FrameCounter) when FrameCounter == 2 ->
	{0, 1, 0};
resetIfNeccessary(SyncOffsetMs, TimesReceived, FrameCounter) ->
	{SyncOffsetMs, TimesReceived, FrameCounter}.

berkley(false, _TimeInSlot, SyncOffsetMs, _TimesReceived) ->
	SyncOffsetMs;
berkley(true, TimeInSlot, SyncOffsetMs, TimesReceived) ->
	Diff = getUTC() - TimeInSlot,
	(SyncOffsetMs + Diff) / TimesReceived.


kill() ->
	debug("Shutdown TimeSync ~n", ?DEBUG).
