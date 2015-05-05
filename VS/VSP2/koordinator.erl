%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Auf Anforderungen wird im Code wie folgt Bezug genommen:
%Anforderungen, die an den Koordinator gestellt werden: Mod.Koor.
%Anforderungsnummer: Anf.-Nr. [Nr [- Nr]]
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%Nachrichten- und Befehlsreferenz%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%Aus der Aufgabenstellung von Prof. Klauck%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%
%%%Befehle:
%
%step: Der Koordinator beendet die Initialphase und bildet den Ring. Er wartet nun auf den Start einer ggT-Berechnung.
%reset: Der Koordinator sendet allen ggT-Prozessen das kill-Kommando und bringt sich selbst in den initialen Zustand, indem sich Starter wieder melden können.
%prompt: Der Koordinator erfragt bei allen ggT-Prozessen per tellmi deren aktuelles Mi ab und zeigt dies im log an.
%nudge: Der Koordinator erfragt bei allen ggT-Prozessen per pingGGT deren Lebenszustand ab und zeigt dies im log an.
%toggle: Der Koordinator verändert den Flag zur Korrektur bei falschen Terminierungsmeldungen.
%kill: Der Koordinator wird beendet und sendet allen ggT-Prozessen das kill-Kommando.
%
%%%%Nachrichten:
%
%{StarterPID, getsteeringval}: Die Anfrage nach den steuernden Werten durch den Starter Prozess.
%{hello, GGTName}:  Ein ggT-Prozess meldet sich beim Koordinator mit Namen Clientname an (GGTName ist der lokal registrierte Name, keine PID!).
%{briefmi, {MeinName, CMi, CZeit}}:  Ein ggT-Prozess mit Namen Clientname (keine PID!) informiert über sein neues Mi CMi um CZeit Uhr. 
%{GGTpid, briefterm, {MeinName, CMi, CZeit}}:  Ein ggT-Prozess mit Namen MeinName (keine PID!) und Absender GGTpid (ist PID) informiert über über die Terminierung der Berechnung mit Ergebnis CMi um CZeit Uhr.
%{voteYes, Name}: Von einem ggt-Prozess erhaltenes Abstimmungsergebnis, wobei Name der Name des Absenders ist (keine PID!).

%Mod.Koor. Anf.-Nr. 25 durch Implementierungssprache Erlang.	
-module(koordinator).
-import(werkzeug, [get_config_value/2, logging/2, timeMilliSecond/0, shuffle/1, bestimme_mis/2]).
-export([start/0, createRing/2]).
%Mod.Koor. Anf.-Nr. 26 - 28 durch Verwendung des Log-Files.
-define(LOGFILE, lists:flatten(io_lib:format("~p.log", [node()]))).

%In der Startfunktion wird die cfg-Datei des Koordinators ausgelesen,
%bei dem Erlang-Node und beim Nameservice registriert.
%Anschließend wird befindet sich der Koordinator im Zustand "Start" (Initialisierungsphase)
start() ->
	logging(?LOGFILE, lists:flatten(io_lib:format("~p: Startzeit ~p | mit PID ~p. ~n", [node(), timeMilliSecond(), self()]))),
	%Mod.Koor. Anf.-Nr. 1 - 9
	{{ok, Arbeitszeit}, 
	{ok, Termzeit}, 
	{ok, Ggtprozessnummer}, 
	{ok, Nameservicenode}, 
	{ok, Nameservicename}, 
	{ok, Koordinatorname}, 
	{ok, Quote}, 
	{ok, Korrigieren}} = readConfig(), %Config-Werte auslesen.
	logging(?LOGFILE, lists:flatten(io_lib:format("koordinator.cfg gelesen...~n", []))),
	
	Pong = net_adm:ping(Nameservicenode),  %Ping an Erlang-Node.
	timer:sleep(1000), %2 Sekunden warten um Nameservice Zeit zu geben.
	
	case Pong == pong of
		true ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Ping erfolgreich. ~n", [])));
		false ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Ping gescheitert!!!. ~n", [])))
	end,
	
	PIDns = global:whereis_name(Nameservicename), %Nameservice PID erfragen.
	
	case PIDns == undefined of
		true ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Nameservice bind gescheitert!!! ~n", [])));
		false ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Nameservice gebunden: ~p ~n", [PIDns])))
	end,
	
	%Mod.Koor. Anf.-Nr. 10
	Val = register(Koordinatorname, self()),
	io:format("Koordinator lokal registriert: ~p~n",[Val]), %Bei Erlang-Node registrieren.
	logging(?LOGFILE, lists:flatten(io_lib:format("lokal registriert. ~n", []))),
	PIDns ! {self(), {rebind, Koordinatorname, node()}}, %An Nameservice binden.
	
	receive
		ok ->
			logging(?LOGFILE, lists:flatten(io_lib:format("beim Nameservice registriert. ~n", [])))
	end,
	%spawn(koordinator, loop, [Arbeitszeit, Termzeit, Ggtprozessnummer, Nameservicenode, Nameservicename, Koordinatorname, Quote, Korrigieren, PIDns, [], [undef, undef], 1000000, -1, 0])
	%loop(Arbeitszeit, Termzeit, Ggtprozessnummer, Nameservicenode, Nameservicename, Koordinatorname, Quote, Korrigieren, PIDns, [], [undef, undef], 1000000, -1, 0)
	loop(Arbeitszeit, Termzeit, Ggtprozessnummer, Koordinatorname, Quote, Korrigieren, PIDns, [], [undef, undef], 1000000000000000000000, -1, 0)
	.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Mod.Koor. Anf.-Nr. 11
%Abkürzungen der Parameter stehen für:
%AZ := Arbeitszeit; TZ := Termzeit; GGTPNr := GGT-Prozessnummer; KN := Koordinatorname; QUO := Quote; KOR := Korrigieren
%PIDns := PID-Nameservice; CMD := Command; GGTL := GGT-Prozessliste; MiMin := Aktuelles minimales Mi; 
%SPZF := Spezialflag (-1 = false, 1 = true); AST := Anzahl Starter (für Abstimmungsquote);
%loop(AZ, TZ, GGTPNr, NameSno, NameSna, KN, QUO, KOR, PIDns, GGTL, CMD, MiMin, SPZF, AST) when hd(CMD) /= step, hd(CMD) /= kill ->
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Der Koordinator kann neue Starter und ggt-Prozesse annehmen.
%Der manuelle Befehl "step" versetzt den Koordinator in die Arbeitsphase,
%in der keine neuen Starter- und ggt-Prozesse mehr angenommen werden können.
loop(AZ, TZ, GGTPNr, KN, QUO, KOR, PIDns, GGTL, CMD, MiMin, SPZF, AST) when hd(CMD) /= step, hd(CMD) /= kill ->
	io:format("CMD: ~p~n", [CMD]), 
	receive
	%%%%********************************************************************************************************************%%%%
	%%%%*****************************************Initialiserungsphase*******************************************************%%%%
	%%%%********************************************************************************************************************%%%%
		%Nachricht von einem Starter: Fordert Konfigurationswerte an.
		%Mod.Koor. Anf.-Nr. 12
		{StarterPID, getsteeringval} -> 
			%Mod.Koor. Anf.-Nr. 13
			Quota = round((QUO / 100) * (GGTPNr * (AST +  1))), %AST + 1 da StarterPID neuer Starter ist.
			StarterPID ! {steeringval, AZ, TZ, Quota, GGTPNr},
			logging(?LOGFILE, lists:flatten(io_lib:format("getsteeringval von Starter ~p | Erwartete Prozesse ~p | (~p). ~n", [StarterPID, GGTPNr * (AST + 1), werkzeug:timeMilliSecond()]))),
			loop(AZ, TZ, GGTPNr, KN, QUO, KOR, PIDns, GGTL, CMD, MiMin, SPZF, AST + 1);
		%Nachricht von einem ggt-Prozess: Meldet sich beim Koordinator an, 
		%wird in die Liste der ggt-Prozesse aufgenommen.
		{hello, GGTName} ->
			logging(?LOGFILE, lists:flatten(io_lib:format("hello: ~p (~p). ~n", [GGTName, GGTPNr]))),
			loop(AZ, TZ, GGTPNr, KN, QUO, KOR, PIDns, GGTL ++ [GGTName], CMD, MiMin, SPZF, AST);
		%Mod.Koor. Anf.-Nr. 14 - 15 da er Guard dieses loop-Zweigs durch setzen des Step-Befehls im CMD Parameter nicht mehr true ist.
		step ->
			logging(?LOGFILE, lists:flatten(io_lib:format("step: um ~p Uhr. ~n", [werkzeug:timeMilliSecond()]))),
			logging(?LOGFILE, lists:flatten(io_lib:format("Erzeuge Ring von ~p ggt-Prozessen | (~p). ~n", [length(GGTL), werkzeug:timeMilliSecond()]))),
			%Mod.Koor. Anf.-Nr. 16 - 17 (createRing Rückgabewert als Parameter). 
			loop(AZ, TZ, GGTPNr, KN, QUO, KOR, PIDns, createRing(GGTL, PIDns), [step, undef], MiMin, SPZF, AST);
		reset ->
			logging(?LOGFILE, lists:flatten(io_lib:format("reset: ~p. ~n", [werkzeug:timeMilliSecond()]))),
			kill(PIDns, GGTL),
			loop(AZ, TZ, GGTPNr, KN, QUO, KOR, PIDns, [], [undef, undef], MiMin, SPZF, 0);
		prompt ->
			logging(?LOGFILE, lists:flatten(io_lib:format("prompt: ~p. ~n", [werkzeug:timeMilliSecond()]))),
			prompt(PIDns, GGTL),
			loop(AZ, TZ, GGTPNr, KN, QUO, KOR, PIDns, GGTL, CMD, MiMin, SPZF, AST);
		nudge ->
			logging(?LOGFILE, lists:flatten(io_lib:format("nudge: ~p. ~n", [werkzeug:timeMilliSecond()]))),
			nudge(PIDns, GGTL),
			loop(AZ, TZ, GGTPNr, KN, QUO, KOR, PIDns, GGTL, CMD, MiMin, SPZF, AST);
		toggle ->
			logging(?LOGFILE, lists:flatten(io_lib:format("toggle: ~p. ~n", [werkzeug:timeMilliSecond()]))),
			loop(AZ, TZ, GGTPNr, KN, QUO, KOR, PIDns, GGTL, CMD, MiMin, SPZF * -1, AST);
		kill ->
			logging(?LOGFILE, lists:flatten(io_lib:format("kill: ~p. ~n", [werkzeug:timeMilliSecond()]))),
			kill(PIDns, GGTL),
			loop(AZ, TZ, GGTPNr, KN, QUO, KOR, PIDns, GGTL, [kill, lists:nth(2, CMD)], MiMin, SPZF, AST);
		Any ->
			logging(?LOGFILE, lists:flatten(io_lib:format("1.Any-Block:  received anything: ~p ~p. ~n", [Any, werkzeug:timeMilliSecond()]))),
			loop(AZ, TZ, GGTPNr, KN, QUO, KOR, PIDns, GGTL, CMD, MiMin, SPZF, AST)
	end	
	;
loop(AZ, TZ, GGTPNr, KN, QUO, KOR, PIDns, GGTL, CMD, MiMin, SPZF, AST) when hd(CMD) /= kill, tl(CMD) /= reset ->
	receive
	%%%%********************************************************************************************************************%%%%
	%%%%*****************************************Arbeitsphase***************************************************************%%%%
	%%%%********************************************************************************************************************%%%%
		%Mod.Koor. Anf.-Nr. 18 - 20
		{calc,WggT} ->
			Mis1 = bestimme_mis(WggT, length(GGTL)), %Mod.Koor. Anf.-Nr. 18 - 19
			sendeMis(PIDns, GGTL, Mis1),
			GewaehlteProzesse = startProzesseErmitteln(GGTL), %Mod.Koor. Anf.-Nr. 20
			Mis2 = bestimme_mis(WggT, length(GewaehlteProzesse)),
			sendeY(PIDns, GewaehlteProzesse, Mis2),
			logging(?LOGFILE, lists:flatten(io_lib:format("Beginne eine neue ggt Berechnung mit Ziel ~p. ~n", [WggT]))),
			loop(AZ, TZ, GGTPNr, KN, QUO, KOR, PIDns, GGTL, CMD, MiMin, SPZF, AST);
		{briefmi, {MeinName, CMi, CZeit}} ->
			logging(?LOGFILE, lists:flatten(io_lib:format("~p meldet neues Mi ~p um ~p|  (~p).~n", [MeinName,CMi, CZeit, werkzeug:timeMilliSecond()]))),
			loop(AZ, TZ, GGTPNr, KN, QUO, KOR, PIDns, GGTL, CMD, MiMin, SPZF, AST);
		{GGTpid, briefterm, {MeinName, CMi, CZeit}} ->
			case MiMin < CMi of
				true ->
					%Mod.Koor. Anf.-Nr. 21
					logging(?LOGFILE, lists:flatten(io_lib:format("Fehlernachricht um ~p Uhr | ~p terminiert mit CMi ~p > MiMin ~p um ~p Uhr.~n", [werkzeug:timeMilliSecond(), MeinName, CMi, MiMin, CZeit])));
				%false-zweig hinzugefuegt, da fehlermeldung ausgegeben wurde
				false ->
					logging(?LOGFILE, lists:flatten(io_lib:format("ggt ~p meldet Terminierung der Berechnung mit ggt ~p um ~p Uhr  | (~p).~n", [MeinName, CMi, CZeit, werkzeug:timeMilliSecond()])))
			end,
			%Mod.Koor. Anf.-Nr. 22
			case SPZF == 1 of
					true ->
						GGTpid ! {sendy, MiMin},
					%false-zweig hinzugefuegt, da fehlermeldung ausgegeben wurde
						logging(?LOGFILE, lists:flatten(io_lib:format("Sende kleinste Zahl ~p an ~p | (~p) Uhr ~n", [MiMin, MeinName, werkzeug:timeMilliSecond()])));
					false ->
						do_nothing
			end,
			loop(AZ, TZ, GGTPNr, KN, QUO, KOR, PIDns, GGTL, CMD, MiMin, SPZF, AST);
		{voteYes, Name} ->
			logging(?LOGFILE, lists:flatten(io_lib:format("voteYes von ~p | (~p). ~n", [Name, werkzeug:timeMilliSecond()]))),
			loop(AZ, TZ, GGTPNr, KN, QUO, KOR, PIDns, GGTL, CMD, MiMin, SPZF, AST);
		reset ->
			logging(?LOGFILE, lists:flatten(io_lib:format("reset: ~p. ~n", [werkzeug:timeMilliSecond()]))),
			kill(PIDns, GGTL),
			loop(AZ, TZ, GGTPNr, KN, QUO, KOR, PIDns, [], [undef, undef], MiMin, -1, 0);
		prompt ->
			logging(?LOGFILE, lists:flatten(io_lib:format("prompt: ~p. ~n", [werkzeug:timeMilliSecond()]))),
			prompt(PIDns, GGTL),
			loop(AZ, TZ, GGTPNr, KN, QUO, KOR, PIDns, GGTL, CMD, MiMin, SPZF, AST);
		nudge ->
			logging(?LOGFILE, lists:flatten(io_lib:format("nudge: ~p. ~n", [werkzeug:timeMilliSecond()]))),
			nudge(PIDns, GGTL),
			loop(AZ, TZ, GGTPNr, KN, QUO, KOR, PIDns, GGTL, CMD, MiMin, SPZF, AST);
		toggle ->
			logging(?LOGFILE, lists:flatten(io_lib:format("toggle: ~p. ~n", [werkzeug:timeMilliSecond()]))),
			loop(AZ, TZ, GGTPNr, KN, QUO, KOR, PIDns, GGTL, CMD, MiMin, SPZF * -1, AST);
		%Mod.Koor. Anf.-Nr. 23 - 24
		kill ->
			logging(?LOGFILE, lists:flatten(io_lib:format("kill: ~p. ~n", [werkzeug:timeMilliSecond()]))),
			kill(PIDns, GGTL),
			loop(AZ, TZ, GGTPNr, KN, QUO, KOR, PIDns, GGTL, [kill, lists:nth(2, CMD)], MiMin, SPZF, AST);
		Any ->
			logging(?LOGFILE, lists:flatten(io_lib:format("2.Any-Block:  received anything: ~p ~p. ~n", [Any, werkzeug:timeMilliSecond()]))),
			loop(AZ, TZ, GGTPNr, KN, QUO, KOR, PIDns, GGTL, CMD, MiMin, SPZF, AST)
	end	
	;
%%%%********************************************************************************************************************%%%%
%%%%*****************************************Beendigungsphase***********************************************************%%%%
%%%%********************************************************************************************************************%%%%
loop(_AZ, _TZ, _GGTPNr, KN, _QUO, _KOR, PIDns, _GGTL, _CMD, _MiMin, _SPZF, _AST) ->
	PIDns ! {self(),{unbind,KN}},
	receive 
		ok ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Erfolgreich vom Nameservice abgemeldet um ~p Uhr. ~n", [werkzeug:timeMilliSecond()])))
	end,
	unregister(KN),
	logging(?LOGFILE, lists:flatten(io_lib:format("Downtime ~p Uhr | vom Koordinator ~p ~n", [werkzeug:timeMilliSecond(), KN])))
	.

%Mod.Koor. Anf.-Nr. 16
%Erzeugt einen Ring aus den bekannten ggt Prozessen indem jedem ggt Prozess
%der linke und rechte Nachbar mitgeteilt werden.
%Der linke Nachbar des Prozesses an Position 0 der Liste ist der Prozess am Ende der Liste,
%der rechte Nachbar des Prozesses am Ende der Liste ist der Prozess an der Position 0 der Liste.
createRing(GGTL, PIDns) when length(GGTL) >= 1 ->
	createRing(werkzeug:shuffle(GGTL), 1, PIDns),
	GGTL;
createRing(GGTL, _PIDns) ->
	GGTL.
createRing(GGTL, Idx, PIDns) when Idx =< length(GGTL)->
	PIDns ! {self(), {lookup, lists:nth(Idx, GGTL)}},
	receive
		{pin, {GGTName, GGTNode}} ->
			Lneighbour = getLneighbor(Idx, PIDns, GGTL),
			Rneighbour = getRneighbor(Idx, PIDns, GGTL),
			{GGTName, GGTNode} ! {setneighbors, Lneighbour, Rneighbour},
			logging(?LOGFILE, lists:flatten(io_lib:format("ggt-Prozess ~p ~p ueber linken (~p) und rechten (~p) Nachbarn informiert ~n", [GGTName, GGTNode, Lneighbour, Rneighbour])))
	end,
	createRing(GGTL, Idx + 1, PIDns);
createRing(_GGTL, _Idx, _PIDns) ->
	ok
	.

%Hilfsfunktion zur Ermittlung des rechten
%Nachbarn eines ggt Prozesses.
getRneighbor(Idx, PIDns, GGTL) when Idx == length(GGTL) ->
	PIDns ! {self(), {lookup, lists:nth(1, GGTL)}},
	receive
		{pin, {GGTName, _GGTNode}} ->
		%Rneighbor = {GGTName, GGTNode}
		Rneighbor = GGTName
	end,
	Rneighbor
	;
getRneighbor(Idx, PIDns, GGTL) ->
	PIDns ! {self(), {lookup, lists:nth(Idx + 1, GGTL)}},
	receive
		{pin, {GGTName, _GGTNode}} ->
		%Rneighbor = {GGTName, GGTNode}
		Rneighbor = GGTName
	end,
	Rneighbor
	.
	
%Hilfsfunktion zur Ermittlung des linken
%Nachbarn eines ggt Prozesses.
getLneighbor(1, PIDns, GGTL) when  length(GGTL) > 1 ->
	PIDns ! {self(), {lookup, lists:nth(length(GGTL), GGTL)}},
	receive
		{pin, {GGTName, _GGTNode}} ->
		%Lneighbor = {GGTName, GGTNode}
		Lneighbor = GGTName
	end,
	Lneighbor
	;
getLneighbor(Idx, PIDns, GGTL) ->
	PIDns ! {self(), {lookup, lists:nth(Idx - 1, GGTL)}},
	receive
		{pin, {GGTName, _GGTNode}} ->
		%Lneighbor = {GGTName, GGTNode}
		Lneighbor = GGTName
	end,
	Lneighbor
	.

%Sendet allen ggt Prozessen initiale Mi.
sendeMis(_PIDns, [], []) ->
	logging(?LOGFILE, lists:flatten(io_lib:format("Allen ggt Prozessen ein initiales Mi gesendet.~n", []))),
	ok;
sendeMis(PIDns, [H | T], [MiNeu | Rest]) ->
	PIDns ! {self(), {lookup, H}},
	receive
		{pin, {GGTName, GGTNode}} ->
		GGT = {GGTName, GGTNode}
	end,
	GGT ! {setpm, MiNeu},
	logging(?LOGFILE, lists:flatten(io_lib:format("ggt-Prozess ~p (~p) initiales Mi ~p gesendet.~n", [GGTName, GGTNode, MiNeu]))),
	sendeMis(PIDns, T, Rest)
	.

%Sendet den mit startProzesseErmitteln(GGTL) ermittelten Prozessen
%intiale y mit dem die Berechnung begonnen werden soll.
sendeY(_PIDns, [], []) ->
	logging(?LOGFILE, lists:flatten(io_lib:format("Allen ausgewaehlten ggt Prozessen ein y gesendet.~n", []))),
	ok;
sendeY(PIDns, [H | T], [Mi | MiRest]) ->
	PIDns ! {self(), {lookup, H}},
	receive
		{pin, {GGTName, GGTNode}} ->
		GGT = {GGTName, GGTNode}
	end,
	GGT ! {sendy, Mi},
	logging(?LOGFILE, lists:flatten(io_lib:format("ggt-Prozess ~p (~p) startendes Y ~p gesendet.~n", [GGTName, GGTNode, Mi]))),
	sendeY(PIDns, T, MiRest)
	.
	
%Loggt von allen ggt Prozessen das aktuelle Mi.
prompt(_PIDns, []) ->
	ok;
prompt(PIDns, [H | T]) ->
	PIDns ! {self(), {lookup, H}},
	receive
		{pin, {GGTName, GGTNode}} ->
			GGT = {GGTName, GGTNode};
		not_found ->
			GGT = PIDns %??? tellmi an Nameservice macht keinen Sinn.
	end,
	GGT ! {self(), tellmi},
	receive
		{mi, Mi} ->
			logging(?LOGFILE, lists:flatten(io_lib:format("ggT-Prozess ~p ~p aktuelles Mi ~p (~p).~n", [H, element(2, GGT), Mi, werkzeug:timeMilliSecond()])))
	end,
	prompt(PIDns, T)
	.
	
%Prüft alle ggt Prozesse auf Lebendigkeit,
%indem allen ein "pingGGT" gesendet wird und ein "pongGGT" erwartet wird.
nudge(_PIDns, []) ->
	ok;
nudge(PIDns, [H | T]) ->
	PIDns ! {self(), {lookup, H}},
	receive
		{pin, {GGTName, GGTNode}} ->
		GGT = {GGTName, GGTNode}
	end,
	GGT ! {self(), pingGGT},
	receive
		{pongGGT, GGTname} ->
			logging(?LOGFILE, lists:flatten(io_lib:format("ggT-Prozess ~p ist lebendig (~p).~n", [GGTname, werkzeug:timeMilliSecond()])))
	end,
	nudge(PIDns, T)
	.

%Erfüllt Mod.Koor. Anf.-Nr. 24
%Schaltet den Koordinator aus, welcher vor dem Shutdown allen ggt Prozessen ein kill sendet.
kill(_PIDns, []) ->
	logging(?LOGFILE, lists:flatten(io_lib:format("Allen ggt Prozessen ein kill gesendet.~n", []))),
	ok;
kill(PIDns, [H | T]) ->
	PIDns ! {self(), {lookup, H}},
	receive
		{pin, {GGTName, GGTNode}} ->
		GGT = {GGTName, GGTNode}
	end,
	GGT ! kill,
	kill(PIDns, T)
	.

%Erfüllt Mod.Koor. Anf.-Nr. 20
%Ermittelt 20% der ggt-Prozesse die mit einem initialen y die Berechnung beginnen sollen.
startProzesseErmitteln(GGTL) -> startProzesseErmitteln(werkzeug:shuffle(GGTL), [], length(GGTL)).
startProzesseErmitteln([H | T], GewaehlteProzesse, LengthGGTL) when length(GewaehlteProzesse) < ((LengthGGTL / 100) * 20)->
	%startProzesseErmitteln(T, GewaehlteProzesse ++ H, LengthGGTL);
	startProzesseErmitteln(T, GewaehlteProzesse ++ [H], LengthGGTL);
startProzesseErmitteln(GGTL, GewaehlteProzesse, _LGGTL) when length(GewaehlteProzesse) == 1 ->
	%GewaehlteProzesse ++ hd(GGTL);
	GewaehlteProzesse ++ [hd(GGTL)];
startProzesseErmitteln(_GGTL, GewaehlteProzesse, _LGGTL) ->
	GewaehlteProzesse
	.
	
%Liest die Konfigurationsdatei koordinator.cfg aus.
readConfig() ->
	{ok, CfgList} = file:consult("koordinator.cfg"),
	logging(?LOGFILE, lists:flatten(io_lib:format("koordinator.cfg geoeffnet...~n", []))),
	{get_config_value(arbeitszeit, CfgList),
	get_config_value(termzeit, CfgList),
	get_config_value(ggtprozessnummer, CfgList),
	get_config_value(nameservicenode, CfgList),
	get_config_value(nameservicename, CfgList),
	get_config_value(koordinatorname, CfgList),
	get_config_value(quote, CfgList),
	get_config_value(korrigieren, CfgList)}
	.
