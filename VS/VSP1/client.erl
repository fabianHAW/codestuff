-module(client).
-import(werkzeug, [get_config_value/2, timeMilliSecond/0, logging/2]).
-import(messageSendTimer, [changeSendInterval/1]).
-export([loop/3]).

-define(HOSTNAME, inet:gethostname()).
-define(GROUP, 3).
-define(TEAM, 01).
-define(LOGFILE, lists:flatten(io_lib:format("client_~p~p.log", [?TEAM, node()]))).


loop(Servername, Servernode, Sendinterval) ->
	%Rolle des Redakteur-Clients
	SendintervalNew = loopEditor(Servername, Servernode, Sendinterval),
	logging(?LOGFILE, lists:flatten(io_lib:format("dropmessages..Done~n", []))),
	%Rolle des Leser-Clients
	loopReader(Servername, Servernode, [], false),
	logging(?LOGFILE, lists:flatten(io_lib:format("getmessages..Done~n", []))),
	loop(Servername, Servernode, SendintervalNew).

loopEditor(Servername, Servernode, Sendinterval) ->
	io:format("in loopEditor~n", []),
	loopEditor(Servername, Servernode, Sendinterval, 0).
	
loopEditor(Servername, Servernode, Sendinterval, Counter) when Counter < 5 ->	
	%1. neue Nachrichten Nummer besorgen
	Number = getMSGNum(Servername),
	%3.1. neue Nachricht generieren 
	{ok, Client} = ?HOSTNAME, 
	%Msg = lists:flatten(io_lib:format("~p-~p-client@" ++ Client ++ "-~p-~pte_Nachricht. C Out: ", [?GROUP, ?TEAM, self(), Number])),
	Msg = lists:flatten(io_lib:format("~p-~p-client@~s-~p-~pte_Nachricht. C Out: ", [?GROUP, ?TEAM, Client, self(), Number])),
	%4. einen bestimmten Zeitabstand warten
	timer:sleep(Sendinterval * 1000),
	%3.2. der Timestamp darf erst nach dem abwarten des Sendeintervalls erzeugt
	%und der Nachricht angehangen werden
	Timestamp = timeMilliSecond(),
	MsgNew = Msg ++ Timestamp,
	%5. generierte Nachricht senden
	Servername ! {dropmessage, [Number, MsgNew, Timestamp]},
	logging(?LOGFILE, lists:flatten(io_lib:format("~p gesendet~n", [MsgNew]))),
	loopEditor(Servername, Servernode, Sendinterval, Counter + 1);
%6. wurden alle 5 Nachrichten gesendet, wird Zeitabstand neu berechnet
%ebenfalls wird neue Nachrichtennummer angefordert und diese geloggt aber nicht verschickt
loopEditor(Servername, _Servernode, Sendinterval, _Counter) ->
	%7. Zeitabstand aendern
	SendintervalNew = changeSendInterval(Sendinterval),
	%8. neue Nachrichten Nummer besorgen
	ForgetNumber = getMSGNum(Servername),
	io:format("forget to send msg~n", []),
	logging(?LOGFILE, lists:flatten(io_lib:format("~pte_Nachricht um " ++ timeMilliSecond() ++ " vergessen zu senden~n", [ForgetNumber]))),
	logging(?LOGFILE, lists:flatten(io_lib:format("neues Sendeintervall: ~p Sekunden (~p)~n", [SendintervalNew, Sendinterval]))),
	SendintervalNew.

%13. Gibt es weitere Nachricht? -> dies wird hier mit dem Terminated-Flag
%signalisiert, welches bei false anzeigt, dass es mind. eine weitere Nachrichricht gibt
loopReader(Servername, Servernode, NumberList, false) ->
	io:format("in loopReader~n", []),
	%10. Nachrichten abfragen
	Servername ! {self(), getmessages},
	receive
		%11. auf Nachricht warten
		{reply, [NNr, Msg, _TSclientout, _TShbqin, _TSdlqin, _TSdlqout], Terminated} ->
			%12. eigene Nachricht markieren
			logging(?LOGFILE, lists:flatten(io_lib:format("~p received new message; C In: " ++  timeMilliSecond() ++ "~n", [Msg]))),
			loopReader(Servername, Servernode, NumberList ++ NNr, Terminated);
		{interrupt, timeout} ->
			io:format("reader-client interrupted~n", []),
			logging(?LOGFILE, "reader-client interruted: timeout " ++ timeMilliSecond() ++ "~n"),
			exit(self(), "reader-client interrupted: timeout~n");
		Any ->
			io:format("reader-client received anything else: ~p~n", [Any])
	end.

%besorgt sich vom Server die naechste Nachrichtennummer
%wurde etwas unbekanntes vom Server empfangen wird -1 als Fehlermeldung zurueck gegeben
getMSGNum(Servername) ->
	Servername ! {self(), getmsgid},
	%2/9 auf neue Nachrichten Nummer warten
	receive
		{nid, Number} ->
			io:format("got new number~n" ,[]),
			Number;
		Any ->
			io:format("an error occured, while waiting for new message number: ~p~n" ,[Any]),
			-1			
	end.