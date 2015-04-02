-module(dlq).
-export([initDLQ/2, expectedNr/1, push2DLQ/3, deliverMSG/4]).
-import(werkzeug, [timeMilliSecond/0, logging/2]).


%Initialisieren der DLQ
%return ist 2-Tupel mit 1. Element Size und 2. Element leere DLQ-Liste
initDLQ(Size, Datei) ->
	logging(Datei, lists:flatten(io_lib:format("DLQ >> initialisiert mit Kapazitaet: ~p~n", [Size]))),
	{Size, [[2,test,1,2,3],[3,hallo,1,2,3],[4,hallo,1,2,3]]}.
	
%Liefert die Nachrichtennummer, die als naechstes gespeichert werden kann
%ist die Liste leer, wird 1 zurueck gegeben
expectedNr({_Size, []}) ->
	1;
expectedNr({_Size, [Head | Tail]}) ->
	expectedNr(Head, Tail).

%Hilfsmethode die die DLQ  rekursiv durchlaeuft bis sie am Ende
%der Liste angekommen ist. Gibt dann die NNr + 1 zurueck.
expectedNr([NNr, _Msg, _TSclientout, _TShbqin, _TSdlqin], []) ->
	NNr + 1;
expectedNr(_HeadOld, [Head | Tail]) ->
	expectedNr(Head, Tail).
	
%Fuegt am Ende der DLQ die neue Nachricht ein
%Ein leserlicher Timestamp wird an die eigentliche Nachricht gehangen, sowie ein
%unleserlicher Timestamp als Element an das Listenelement 
push2DLQ([NNr, Msg, TSclientout, TShbqin], {Size, Queue}, Datei) ->
	TSdlqin = erlang:now(),
	MSGTimestamp = timeMilliSecond(),
	MsgNew = lists:flatten(io_lib:format("~p TSdlqin: " ++ MSGTimestamp, [Msg])),
	logging(Datei, lists:flatten(io_lib:format("DLQ >> Nachricht ~p in DLQ eingefuegt~n", [NNr]))),
	push2DLQ([NNr, MsgNew, TSclientout, TShbqin], TSdlqin, Size, length(Queue), Queue, Datei).

%Wurd die maximale Groesse der DLQ erreicht, wird das aelteste Element (vorne) geleoscht
push2DLQ(List, TSdlqin, Size, Length, [[NNr, _Msg, _TSclientout, _TShbqin, _TSdlqinOld] | Tail], Datei) when Length == Size -> 
	logging(Datei, lists:flatten(io_lib:format("DLQ >> Nachricht ~p aus DLQ geloescht~n", [NNr]))),
	push2DLQ(List, TSdlqin, Size, Length - 1, Tail, Datei);
push2DLQ(List, TSdlqin, Size, _Length, Queue, _Datei) ->
	ListNew = List ++ [TSdlqin],
	{Size, Queue ++ [ListNew]}.
	
	
%TODO
deliverMSG(MSGNr, CientPID, Queue, Datei) ->
	ok.
