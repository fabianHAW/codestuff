-module(timesync).
-export([start/3]).
-import(werkzeug, [getUTC/0, logging/2]).
-define(NAME, "timesync").
-define(LOGFILE, lists:flatten(io_lib:format("log/~p.log", [?NAME]))).
-define(DEBUG, true).


start(StationClass, UtcOffsetMs, SenderPID) ->
	SenderPID ! {helloTime, self()},
	debug("send own pid to sender", ?DEBUG),
	getNewTime(UtcOffsetMs, string:equal(StationClass, "A"))
.

debug(Text, true) ->
	io:format("timesync_module: ~p~n", [Text]).

%Ist die eigene Station von der Klasse B (false),
%Bleibt die Zeit ungenau.
%Sonst soll die Zeit synchronisiert werden.
getNewTime(UtcOffsetMs, false) ->
	accurate(UtcOffsetMs, 0, 0);
getNewTime(UtcOffsetMs, true) ->
	accurate(UtcOffsetMs, 0, 0)
.

%Nicht mehr benötigt!
%Zeit wird nicht synchronisiert, da die eigene Station von der Klasse B ist.
%Erhält Anfragen von Sender u. MessageGen, ignoriert Zeitmitteilung vom Receiver.
inaccurate(UtcOffsetMs) ->
	receive 
		{times, StationClass, TimeInSlot} ->
			inaccurate(UtcOffsetMs);
		{getTime, SenderPID_MessageGenPID} ->
			SenderPID_MessageGenPID ! {currentTime, getUTC()},
			inaccurate(UtcOffsetMs);
		kill ->
			kill()
	end
.

%Zeit wird synchronisiert, da die eigene Staion von der Klasse A ist.
%Erhält Anfragen von Sender u. MessageGen, berücksichtigt Zeitmitteilungen vom Receiver.
accurate(UtcOffsetMs, SyncOffsetMs, TimesReceived) ->
	receive 
		{times, StationClass, TimeInSlot} ->
			SyncOffsetNew = berkley(string:equal(StationClass, "A"), TimeInSlot, SyncOffsetMs, TimesReceived + 1),
			accurate(UtcOffsetMs, SyncOffsetNew, TimesReceived + 1);
		{getTime, SenderPID_MessageGenPID} ->
			SenderPID_MessageGenPID ! {currentTime, SyncOffsetMs + UtcOffsetMs}, %TODO: SyncOffset Berechnen Berkley
			accurate(UtcOffsetMs, SyncOffsetMs, TimesReceived);
		kill ->
			kill()
	end
.

%Evtl. RTT, Jitter reinnehmen
berkley(false, _TimeInSlot, SyncOffsetMs, _TimesReceived) ->
	SyncOffsetMs;
berkley(true, TimeInSlot, SyncOffsetMs, TimesReceived) ->
	Diff = getUTC() - TimeInSlot,
	(SyncOffsetMs + Diff) / TimesReceived
.


kill() ->
	%TODO Loggen
	debug("Shutdown TimeSync ~n", ?DEBUG)
.
