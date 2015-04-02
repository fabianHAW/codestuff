-module(cmem).
-import(werkzeug, [getUTC/0]).
-export([deleteExpired/1, initCMEM/2, lastVisit/2, getClientNNR/2, updateClient/4]).


%ToDo: Loggen.
initCMEM(RemTime, Datei) ->
	[RemTime, []].

lastVisit([[]], ClientID) ->
	{nok, ClientID};
lastVisit([[Pid, _NNr, _LastVisit] | Rest], ClientID) when Pid /= ClientID ->
	lastVisit(Rest, ClientID);
lastVisit([[_Pid, _NNr, LastVisit] | _Rest], _ClientID)  ->
	LastVisit.
	
getClientNNR([[]], _ClientID) ->
	1;
getClientNNR([[Pid, _NNr, _LastVisit] | Rest], ClientID) when Pid /= ClientID ->
	getClientNNR(Rest, ClientID);
getClientNNR([[_Pid, NNr, _LastVisit] | _Rest], _ClientID)  ->
	NNr + 1.
	
%ToDo: Loggen	
updateClient([[]],ClientID, _NNr,_Datei) ->
	[[ClientID, 1, werkzeug:getUTC()]];
updateClient([[Pid, NNr2, LastVisit] | Rest], ClientID, NNr, Datei) when Pid /= ClientID ->
	[Pid, NNr2, LastVisit] ++ updateClient(Rest, ClientID, NNr, Datei);
updateClient([[Pid, _NNrOld, _LastVisit] | Rest], _ClientID, NNr, Datei)  ->
	[[Pid, NNr, werkzeug:getUTC()]] ++ Rest.

deleteExpired([RemTime, Cmem]) ->
	deleteExpired(RemTime, Cmem).
deleteExpired(_RemTime, []) ->
	[];
deleteExpired(RemTime, [[ClientPid, NNr, LastVisit] | Rest]) ->
	case (werkzeug:getUTC() - LastVisit)/1000 > RemTime of
		true ->
			deleteExpired(RemTime, Rest);
		false ->	
		[ClientPid, NNr, LastVisit] ++ deleteExpired(RemTime, Rest)
	end.
