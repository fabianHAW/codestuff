-module(cmem).
-import(werkzeug, [getUTC/0]).
-export([deleteExpired/1, initCMEM/2, lastVisit/2, getClientNNr/2, updateClient/4]).


%ToDo: Loggen.
initCMEM(RemTime, Datei) ->
	[RemTime, []].


%wofuer ist diese Methode?
lastVisit([RemTime, []], ClientID) ->
	{nok, ClientID};
lastVisit([RemTime, [[Pid, _NNr, _LastVisit] | Rest]], ClientID) when Pid /= ClientID ->
	lastVisit([RemTime, Rest], ClientID);
lastVisit([[Pid, _NNr, _LastVisit] | Rest], ClientID) when Pid /= ClientID ->
	lastVisit(Rest, ClientID);
lastVisit([_RemTime, [[_Pid, _NNr, LastVisit] | _Rest]], _ClientID) ->
	LastVisit.
	
getClientNNr([RemTime, []], _ClientID) ->
	1;
getClientNNr([RemTime, [[Pid, _NNr, _LastVisit] | Rest]], ClientID) when Pid /= ClientID ->
	getClientNNr([RemTime, Rest], ClientID);
getClientNNr([RemTime, [[_Pid, NNr, _LastVisit] | _Rest]], _ClientID) ->
	NNr + 1.


updateClient([RemTime, []], ClientID, NNr, Datei) ->
	[RemTime, [[ClientID, 1, getUTC()]]];
updateClient([RemTime, Clients], ClientID, NNr, Datei) ->
	io:format("Clients: ~p ~n", [Clients]),
	Result = updateClient(Clients, ClientID, NNr, Datei, search),
	io:format("Result: ~p ~n", [Result]),
	[RemTime] ++ [Result].
updateClient([], ClientID, NNr, Datei, search) ->
	[[ClientID, NNr, getUTC()]];	
updateClient([[Pid, NNr2, Timestamp] | Rest], ClientID, NNr, Datei, search) when Pid == ClientID->
	io:format("HIER!!!!!!!!, Rest: ~p ~n", [Rest]),
	[[Pid, NNr, getUTC()]] ++ Rest;
updateClient([[Pid, NNr2, Timestamp] | Rest], ClientID, NNr, Datei, search) ->
	io:format("DORT!!!!!!!!! ~n"),
	[[Pid, NNr, getUTC()]] ++ updateClient(Rest, ClientID, NNr, Datei, search).

%ToDo: Loggen	
%updateClient([RemTime, [[]]],ClientID, _NNr,_Datei) ->
%	[RemTime, [[ClientID, 1, werkzeug:getUTC()]]];
%updateClient([RemTime, [[Pid, NNr2, LastVisit] | Rest]], ClientID, NNr, Datei) when Pid /= ClientID ->
%	[RemTime] ++ [[Pid, NNr2, LastVisit]] ++ updateClient(Rest, ClientID, NNr, Datei);
%updateClient([RemTime, [[Pid, NNr2, LastVisit] | Rest]], ClientID, NNr, Datei) when Pid == ClientID ->
%	[RemTime, [[Pid, NNr, werkzeug:getUTC()]] ++ Rest];
%updateClient([[Pid, NNr2, LastVisit] | Rest], ClientID, NNr, Datei) when Pid /= ClientID ->
%	[[Pid, NNr2, LastVisit]] ++ updateClient(Rest, ClientID, NNr, Datei);
%updateClient([[Pid, _NNrOld, _LastVisit] | Rest], _ClientID, NNr, Datei)  ->
%	[[Pid, NNr, werkzeug:getUTC()]] ++ Rest;
%updateClient([], ClientID, NNr, _Datei)  ->
%	[[ClientID, NNr, werkzeug:getUTC()]].

deleteExpired([RemTime, Cmem]) ->
	deleteExpired(RemTime, Cmem).
deleteExpired(_RemTime, []) ->
	[];
deleteExpired(RemTime, [[ClientPid, NNr, LastVisit] | Rest]) ->
	case (werkzeug:getUTC() - LastVisit)/1000 > RemTime of
		true ->
			deleteExpired(RemTime, Rest);
		false ->	
		[[ClientPid, NNr, LastVisit]] ++ deleteExpired(RemTime, Rest)
	end.
