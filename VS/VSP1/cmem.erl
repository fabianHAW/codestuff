-module(cmem).
-import(werkzeug, [getUTC/0, logging/2, timeMilliSecond/0]).
-export([deleteExpired/2, initCMEM/2, getClientNNr/2, updateClient/4]).

%Die Nummern in den Kommentaren beziehen sich auf:
%Das Diagramm "Serverkomponente"

%ToDo: Loggen.
initCMEM(RemTime, Datei) ->
	[RemTime, []].

%10.) Ist Client neu?  Wenn Client nicht in Liste enthalten ist, dann 1. zurückgeben.
%12.) Älteste Nachrichtennummer zum absenden markieren = NNr 1.
getClientNNr([_RemTime, []], _ClientID) ->
	1;
getClientNNr([RemTime, [[Pid, _NNr, _LastVisit] | Rest]], ClientID) when Pid /= ClientID ->
	getClientNNr([RemTime, Rest], ClientID);
%11.) Nächste Nachrichtennummer zum absenden markieren = NNr + 1 zurückgeben.
getClientNNr([_RemTime, [[_Pid, NNr, _LastVisit] | _Rest]], _ClientID) ->
	NNr + 1.

%Update des Clients.
%RemTime = Clientlifetime.
updateClient([RemTime, []], ClientID, _NNr, Datei) -> %Client neu & erster Client überhaupt.-> speichern.
	[RemTime, [[ClientID, 1, getUTC()]]];
updateClient([RemTime, Clients], ClientID, NNr, Datei) -> % Client suchen.
	Result = updateClient(Clients, ClientID, NNr, Datei, search),
	[RemTime] ++ [Result].

%Hilfsfunktion zur Suche des Clients.
updateClient([], ClientID, NNr, Datei, search) -> %Client neu -> speichern.
	[[ClientID, NNr, getUTC()]];	
updateClient([[Pid, _NNr2, _Timestamp] | Rest], ClientID, NNr, Datei, search) when Pid == ClientID-> %Client gefunden.
	[[Pid, NNr, getUTC()]] ++ Rest;
updateClient([[Pid, NNr2, Timestamp] | Rest], ClientID, NNr, Datei, search) -> %Client noch nicht gefunden -> weitersuchen.
	[[Pid, NNr2, Timestamp]] ++ updateClient(Rest, ClientID, NNr, Datei, search).
	%[[Pid, NNr, getUTC()]] ++ updateClient(Rest, ClientID, NNr, Datei, search).

%Wird vom Server aufgerufen, entfernt Clients aus CMEM die sich eine gewisse Zeit (RemTime) nicht gemeldet haben.
deleteExpired([RemTime, Cmem], Datei) ->
	deleteExpired(RemTime, Cmem, Datei).
deleteExpired(_RemTime, [], _Datei) ->
	[];
deleteExpired(RemTime, [[ClientPid, NNr, LastVisit] | Rest], Datei) ->
	case (getUTC() - LastVisit)/1000 > RemTime of
		true ->
			logging(Datei, lists:flatten(io_lib:format("CMEM: Client: ~p expired" ++ timeMilliSecond() ++ "~n", [ClientPid]))),
			deleteExpired(RemTime, Rest, Datei);
		false ->	
			[[ClientPid, NNr, LastVisit]] ++ deleteExpired(RemTime, Rest, Datei)
	end.
