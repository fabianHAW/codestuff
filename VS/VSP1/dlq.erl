-module(dlq).
-export([initDLQ/2, expectedNr/1, push2DLQ/3, deliverMSG/4]).
-import(werkzeug, [timeMilliSecond/0, logging/2]).

%Die Nummern in den Kommentaren beziehen sich auf:
%Das Diagramm "HBQ-DLQ-Algorithmus"

%Initialisieren der DLQ
%return ist 2-Tupel mit 1. Element Size und 2. Element leere DLQ-Liste
initDLQ(Size, Datei) ->
	logging(Datei, lists:flatten(io_lib:format("DLQ >> initialisiert mit Kapazitaet: ~p~n", [Size]))),
%laut Entwurf wird noch ein Atom queue vor dem eigentlichen ADT gesetzt
	{queue, {Size, []}}.
	
%Liefert die Nachrichtennummer, die als naechstes gespeichert werden kann
%ist die Liste leer, wird 1 zurueck gegeben
expectedNr({_Size, []}) ->
%laut Entwurf wird noch ein Atom reply vor dem eigentlichen ADT gesetzt
	{reply, 1};
expectedNr({_Size, [Head | Tail]}) ->
	expectedNr(Head, Tail).
%Hilfsmethode die die DLQ  rekursiv durchlaeuft bis sie am Ende
%der Liste angekommen ist. Gibt dann die NNr + 1 zurueck.
expectedNr([NNr, _Msg, _TSclientout, _TShbqin, _TSdlqin], []) ->
%laut Entwurf wird noch ein Atom reply vor dem eigentlichen ADT gesetzt
	{reply, NNr + 1};
expectedNr(_HeadOld, [Head | Tail]) ->
	expectedNr(Head, Tail).
	
%Fuegt am Ende der DLQ die neue Nachricht ein
%Ein leserlicher Timestamp wird an die eigentliche Nachricht gehangen, sowie ein
%unleserlicher Timestamp als Element an das Listenelement 
push2DLQ([NNr, Msg, TSclientout, TShbqin], {Size, Queue}, Datei) ->
	TSdlqin = erlang:now(),
	MSGTimestamp = timeMilliSecond(),
	MsgNew = lists:flatten(io_lib:format("~p TSdlqin: " ++ MSGTimestamp, [Msg])),
	logging(Datei, lists:flatten(io_lib:format("DLQ >> Nachricht ~p in DLQ eingefuegt um: "  ++ timeMilliSecond() ++ "~n", [NNr]))),
	push2DLQ([NNr, MsgNew, TSclientout, TShbqin], TSdlqin, Size, length(Queue), Queue, Datei).
%7.) DLQ voll? -> Ja-Zweig
%Wurde die maximale Groesse der DLQ erreicht, wird das aelteste Element (vorne) geleoscht
push2DLQ(List, TSdlqin, Size, Length, [[NNr, _Msg, _TSclientout, _TShbqin, _TSdlqinOld] | Tail], Datei) when Length == Size -> 
	logging(Datei, lists:flatten(io_lib:format("DLQ >> Nachricht ~p aus DLQ geloescht um: " ++ timeMilliSecond() ++ "~n", [NNr]))),
	%8.) aelteste Nachricht in DLQ entfernen
	push2DLQ(List, TSdlqin, Size, Length - 1, Tail, Datei);
%7.) DLQ voll? -> Nein-Zweig
push2DLQ(List, TSdlqin, Size, _Length, Queue, _Datei) ->
	ListNew = List ++ [TSdlqin],
%laut Entwurf wird noch ein Atom queue vor dem eigentlichen ADT gesetzt
	{queue, {Size, Queue ++ [ListNew]}}.
	
%liefert eine Nachricht an den Client aus und gibt der HBQ die gesendete Nachrichtennummer zurueck
%hier wurde das Ende der zur Verfuegung stehenden Nachrichten erreicht und dem
%Client signalisiert, dass es keine weiteren Nachrichten mehr gibt, Terminated = true
deliverMSG(MSGNr, ClientPID, {_Size, []}, Datei) -> 
	ClientPID ! {reply, [MSGNr, "Dummy Nachricht", 0, 0, 0] ++ [erlang:now()], true},
	logging(Datei, lists:flatten(io_lib:format("DLQ >> Dummy an Client ~p ausgeliefert um: " ++ timeMilliSecond() ++ "~n", [ClientPID]))),
	{reply, -1};
deliverMSG(MSGNr, ClientPID, {_Size, [[NNr, Msg, TSclientout, TShbqin, TSdlqin] | []]}, Datei) when MSGNr =< NNr ->
	ClientPID ! {reply, [NNr, Msg, TSclientout, TShbqin, TSdlqin] ++ [erlang:now()], true},
	logging(Datei, lists:flatten(io_lib:format("DLQ >> Nachricht ~p an Client ~p ausgeliefert um: " ++ timeMilliSecond() ++ "~n", [NNr, ClientPID]))),
%laut Entwurf wird aber noch ein Atom reply vor dem eigentlichen ADT gesetzt
	{reply, NNr};
%es gibt noch weitere Nachrichten die dem Client auf Anfrage zugestellt werden koennen, Terminated = false
deliverMSG(MSGNr, ClientPID, {_Size, [[NNr, Msg, TSclientout, TShbqin, TSdlqin] | _Tail]}, Datei) when MSGNr =< NNr ->
	ClientPID ! {reply, [NNr, Msg, TSclientout, TShbqin, TSdlqin] ++ [erlang:now()], false},
	logging(Datei, lists:flatten(io_lib:format("DLQ >> Nachricht ~p an Client ~p ausgeliefert um: " ++ timeMilliSecond() ++ "~n", [NNr, ClientPID]))),
%laut Entwurf wird noch ein Atom reply vor dem eigentlichen ADT gesetzt
	{reply, NNr};
%rekursiver Durchlauf durch die Liste
deliverMSG(MSGNr, ClientPID, {Size, [[_NNr, _Msg, _TSclientout, _TShbqin, _TSdlqin] | Tail]}, Datei) ->
	deliverMSG(MSGNr, ClientPID, {Size, Tail}, Datei).
