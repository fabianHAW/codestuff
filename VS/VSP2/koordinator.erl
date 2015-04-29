-module(koordinator).
-import(werkzeug, [get_config_value/2, logging/2, timeMilliSecond/0, shuffle/1, bestimme_mis/2]).
-export([start/0, createRing/2]).
-define(LOGFILE, lists:flatten(io_lib:format("~p.log", [node()]))).

%Umsetzung der Anforderungen "Modul: Koordinator" Anf.-Nr. 1
start() ->
	logging(?LOGFILE, lists:flatten(io_lib:format("~p: Startzeit ~p | mit PID ~p. ~n", [node(), timeMilliSecond(), self()]))),
	{{ok, Arbeitszeit}, 
	{ok, Termzeit}, 
	{ok, Ggtprozessnummer}, 
	{ok, Nameservicenode}, 
	{ok, Nameservicename}, 
	{ok, Koordinatorname}, 
	{ok, Quote}, 
	{ok, Korrigieren}} = readConfig(), %Config-Werte auslesen.
	logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: koordinator.cfg gelesen...~n", []))),
	
	Pong = net_adm:ping(Nameservicenode),  %Ping an Erlang-Node.
	timer:sleep(1000), %2 Sekunden warten um Nameservice Zeit zu geben.
	
	%Pang erhalten? Loggen!
	case Pong == pong of
		true ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: Ping erfolgreich. ~n", [])));
		false ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: Ping gescheitert!!!. ~n", [])))
	end,
	
	PIDns = global:whereis_name(Nameservicename), %Nameservice PID erfragen.
	
	%Nameservice PID erhalten? Loggen!
	case PIDns == undefined of
		true ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: Nameservice bind gescheitert!!! ~n", [])));
		false ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: Nameservice gebunden: ~p ~n", [PIDns])))
	end,
	
	%io:format("~p",[register(Koordinatorname, self())]), %Bei Erlang-Node registrieren.
	Val = register(Koordinatorname, self()),
	io:format("Koordinator lokal registriert: ~p~n",[Val]), %Bei Erlang-Node registrieren.
	logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: lokal registriert. ~n", []))),
	PIDns ! {self(), {rebind, Koordinatorname, node()}}, %An Nameservice binden.
	
	receive
		ok ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: beim Nameservice registriert. ~n", [])))
	end,
	%spawn(koordinator, loop, [Arbeitszeit, Termzeit, Ggtprozessnummer, Nameservicenode, Nameservicename, Koordinatorname, Quote, Korrigieren, PIDns, [], [undef, undef], 1000000, -1, 0])
	loop(Arbeitszeit, Termzeit, Ggtprozessnummer, Nameservicenode, Nameservicename, Koordinatorname, Quote, Korrigieren, PIDns, [], [undef, undef], 1000000, -1, 0)
	.
%AZ := Arbeitszeit; TZ := Termzeit; GGTPNr := GGT-Prozessnummer; NameSno := NameserviceNode
%NameSna := NameserviceName; KN := Koordinatorname; QUO := Quote; KOR := Korrigieren
%PIDns := PID-Nameservice; CMD := Command; GGTL := GGT-Prozessliste; MiMin := Aktuelles minimales Mi; 
%SPZF := Spezialflag (-1 = false, 1 = true); AST := Anzahl Starter (für Abstimmungsquote);
loop(AZ, TZ, GGTPNr, NameSno, NameSna, KN, QUO, KOR, PIDns, GGTL, CMD, MiMin, SPZF, AST) when hd(CMD) /= step, hd(CMD) /= kill ->
	io:format("CMD: ~p~n", [CMD]), 
	receive
	%%%%********************************************************************************************************************%%%%
	%%%%*****************************************Initialiserungsphase*******************************************************%%%%
	%%%%********************************************************************************************************************%%%%
		{StarterPID, getsteeringval} ->
			%Quota = round(QUO / 100) * (AST +  1), %AST + 1 da StarterPID neuer Starter ist.
			Quota = round((QUO / 100) * (GGTPNr * (AST +  1))), %AST + 1 da StarterPID neuer Starter ist.
			StarterPID ! {steeringval, AZ, TZ, Quota, GGTPNr},
			logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: getsteeringval: ~p (~p). ~n", [StarterPID, GGTPNr * (AST + 1)]))),
			loop(AZ, TZ, GGTPNr, NameSno, NameSna, KN, QUO, KOR, PIDns, GGTL, CMD, MiMin, SPZF, AST + 1);
		{hello, GGTName} ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: hello: ~p (~p). ~n", [GGTName, GGTPNr]))),
			loop(AZ, TZ, GGTPNr, NameSno, NameSna, KN, QUO, KOR, PIDns, GGTL ++ [GGTName], CMD, MiMin, SPZF, AST);
		step ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: step: um ~p Uhr. ~n", [werkzeug:timeMilliSecond()]))),
			loop(AZ, TZ, GGTPNr, NameSno, NameSna, KN, QUO, KOR, PIDns, createRing(GGTL, PIDns), [step, undef], MiMin, SPZF, AST);
		reset ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: reset: ~p. ~n", [werkzeug:timeMilliSecond()]))),
			kill(PIDns, GGTL),
			loop(AZ, TZ, GGTPNr, NameSno, NameSna, KN, QUO, KOR, PIDns, GGTL, [undef,reset], MiMin, SPZF, 0);
		prompt ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: prompt: ~p. ~n", [werkzeug:timeMilliSecond()]))),
			prompt(PIDns, GGTL),
			loop(AZ, TZ, GGTPNr, NameSno, NameSna, KN, QUO, KOR, PIDns, GGTL, [prompt, lists:nth(2, CMD)], MiMin, SPZF, AST);
		nudge ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: nudge: ~p. ~n", [werkzeug:timeMilliSecond()]))),
			nudge(PIDns, GGTL),
			loop(AZ, TZ, GGTPNr, NameSno, NameSna, KN, QUO, KOR, PIDns, GGTL, [nudge,lists:nth(2, CMD)], MiMin, SPZF, AST);
		toggle ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: toggle: ~p. ~n", [werkzeug:timeMilliSecond()]))),
			loop(AZ, TZ, GGTPNr, NameSno, NameSna, KN, QUO, KOR, PIDns, GGTL, CMD, MiMin, SPZF * -1, AST);
		kill ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: kill: ~p. ~n", [werkzeug:timeMilliSecond()]))),
			kill(PIDns, GGTL),
			loop(AZ, TZ, GGTPNr, NameSno, NameSna, KN, QUO, KOR, PIDns, GGTL, [kill, lists:nth(2, CMD)], MiMin, SPZF, AST);
		Any ->
			logging(?LOGFILE, lists:flatten(io_lib:format("1.Any-Block: Koordinator: received anything: ~p ~p. ~n", [Any, werkzeug:timeMilliSecond()]))),
			loop(AZ, TZ, GGTPNr, NameSno, NameSna, KN, QUO, KOR, PIDns, GGTL, CMD, MiMin, SPZF, AST)
	end	
	;
loop(AZ, TZ, GGTPNr, NameSno, NameSna, KN, QUO, KOR, PIDns, GGTL, CMD, MiMin, SPZF, AST) when hd(CMD) /= kill, tl(CMD) /= reset ->
	io:format("CMD: ~p~n", [CMD]), 
	receive
	%%%%********************************************************************************************************************%%%%
	%%%%*****************************************Arbeitsphase***************************************************************%%%%
	%%%%********************************************************************************************************************%%%%
		{calc,WggT} ->
			Mis1 = bestimme_mis(WggT, length(GGTL)),
			sendeMis(PIDns, GGTL, Mis1),
			GewaehlteProzesse = startProzesseErmitteln(GGTL),
			io:format("gewaehlteprozesse von mis2 bestimmung: ~p~n", [GewaehlteProzesse]),
			Mis2 = bestimme_mis(WggT, length(GewaehlteProzesse)),
			sendeY(PIDns, GewaehlteProzesse, Mis2),
			loop(AZ, TZ, GGTPNr, NameSno, NameSna, KN, QUO, KOR, PIDns, GGTL, CMD, MiMin, SPZF, AST);
		{briefmi, {MeinName, CMi, CZeit}} ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: ~p meldet neues Mi ~p um ~p|  (~p).~n", [MeinName,CMi, CZeit, werkzeug:timeMilliSecond()]))),
			loop(AZ, TZ, GGTPNr, NameSno, NameSna, KN, QUO, KOR, PIDns, GGTL, CMD, MiMin, SPZF, AST);
		{GGTpid, briefterm, {MeinName, CMi, CZeit}} ->
			case MiMin < CMi of
				true ->
					logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: Fehlernachricht um ~p Uhr | ~p terminiert mit CMi ~p > MiMin ~p um ~p Uhr.~n", [werkzeug:timeMilliSecond(), MeinName, CMi, MiMin, CZeit])));
				%false-zweig hinzugefuegt, da fehlermeldung ausgegeben wurde
				false ->
					do_nothing
			end,
			case SPZF == 1 of
					true ->
						GGTpid ! {sendy, MiMin};
					%false-zweig hinzugefuegt, da fehlermeldung ausgegeben wurde
					false ->
						do_nothing
			end,
			loop(AZ, TZ, GGTPNr, NameSno, NameSna, KN, QUO, KOR, PIDns, GGTL, CMD, MiMin, SPZF, AST);
		reset ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: reset: ~p. ~n", [werkzeug:timeMilliSecond()]))),
			kill(PIDns, GGTL),
			loop(AZ, TZ, GGTPNr, NameSno, NameSna, KN, QUO, KOR, PIDns, GGTL, [lists:nth(1, CMD),reset], MiMin, SPZF, AST);
		prompt ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: prompt: ~p. ~n", [werkzeug:timeMilliSecond()]))),
			prompt(PIDns, GGTL),
			loop(AZ, TZ, GGTPNr, NameSno, NameSna, KN, QUO, KOR, PIDns, GGTL, [prompt,lists:nth(2, CMD)], MiMin, SPZF, AST);
		nudge ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: nudge: ~p. ~n", [werkzeug:timeMilliSecond()]))),
			nudge(PIDns, GGTL),
			loop(AZ, TZ, GGTPNr, NameSno, NameSna, KN, QUO, KOR, PIDns, GGTL, [nudge,lists:nth(2, CMD)], MiMin, SPZF, AST);
		toggle ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: toggle: ~p. ~n", [werkzeug:timeMilliSecond()]))),
			loop(AZ, TZ, GGTPNr, NameSno, NameSna, KN, QUO, KOR, PIDns, GGTL, CMD, MiMin, SPZF * -1, AST);
		kill ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: kill: ~p. ~n", [werkzeug:timeMilliSecond()]))),
			kill(PIDns, GGTL),
			loop(AZ, TZ, GGTPNr, NameSno, NameSna, KN, QUO, KOR, PIDns, GGTL, [kill, lists:nth(2, CMD)], MiMin, SPZF, AST);
		Any ->
			logging(?LOGFILE, lists:flatten(io_lib:format("2.Any-Block: Koordinator: received anything: ~p ~p. ~n", [Any, werkzeug:timeMilliSecond()]))),
			loop(AZ, TZ, GGTPNr, NameSno, NameSna, KN, QUO, KOR, PIDns, GGTL, CMD, MiMin, SPZF, AST)
	end	
	;
loop(_AZ, _TZ, _GGTPNr, _NameSno, _NameSna, KN, _QUO, _KOR, PIDns, _GGTL, _CMD, _MiMin, _SPZF, _AST) ->
	PIDns ! {self(),{unbind,meindienst}},
	receive 
		ok ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: Erfolgreich vom Nameservice abgemeldet um ~p Uhr. ~n", [werkzeug:timeMilliSecond()])))
	end,
	unregister(KN),
	logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: Downtime ~p Uhr | vom Koordinator ~p ~n", [werkzeug:timeMilliSecond(), KN])))
	.
	
createRing(GGTL, PIDns) when length(GGTL) >= 1 ->
	io:format("GGTL: ~p ggtllaenge: ~p~n", [GGTL, length(GGTL)]),
	createRing(werkzeug:shuffle(GGTL), 1, PIDns),
	GGTL;
createRing(GGTL, _PIDns) ->
	GGTL.
createRing(GGTL, Idx, PIDns) when Idx =< length(GGTL)->
	io:format("Idx: ~p~n", [Idx]),
	PIDns ! {self(), {lookup, lists:nth(Idx, GGTL)}},
	receive
		{pin, {GGTName, GGTNode}} ->
			{GGTName, GGTNode} ! {setneighbors,getLneighbor(Idx, PIDns, GGTL), getRneighbor(Idx, PIDns, GGTL)}
	end,
	createRing(GGTL, Idx + 1, PIDns);
createRing(_GGTL, _Idx, _PIDns) ->
	ok
	.

getRneighbor(Idx, PIDns, GGTL) when Idx == length(GGTL) ->
	PIDns ! {self(), {lookup, lists:nth(1, GGTL)}},
	receive
		{pin, {GGTName, GGTNode}} ->
		%Rneighbor = {GGTName, GGTNode}
		Rneighbor = GGTName
	end,
	Rneighbor
	;
getRneighbor(Idx, PIDns, GGTL) ->
	PIDns ! {self(), {lookup, lists:nth(Idx + 1, GGTL)}},
	receive
		{pin, {GGTName, GGTNode}} ->
		%Rneighbor = {GGTName, GGTNode}
		Rneighbor = GGTName
	end,
	Rneighbor
	.
	
getLneighbor(1, PIDns, GGTL) when  length(GGTL) > 1 ->
	PIDns ! {self(), {lookup, lists:nth(length(GGTL), GGTL)}},
	receive
		{pin, {GGTName, GGTNode}} ->
		%Lneighbor = {GGTName, GGTNode}
		Lneighbor = GGTName
	end,
	Lneighbor
	;
getLneighbor(Idx, PIDns, GGTL) ->
	PIDns ! {self(), {lookup, lists:nth(Idx - 1, GGTL)}},
	receive
		{pin, {GGTName, GGTNode}} ->
		%Lneighbor = {GGTName, GGTNode}
		Lneighbor = GGTName
	end,
	Lneighbor
	.
sendeMis(_PIDns, [], []) ->
	ok;
sendeMis(PIDns, [H | T], [MiNeu | Rest]) ->
	PIDns ! {self(), {lookup, H}},
	receive
		{pin, {GGTName, GGTNode}} ->
		GGT = {GGTName, GGTNode}
	end,
	GGT ! {setpm, MiNeu},
	sendeMis(PIDns, T, Rest)
	.

sendeY(_PIDns, [], []) ->
	ok;
sendeY(PIDns, [H | T], [Mi | MiRest]) ->
	PIDns ! {self(), {lookup, H}},
	receive
		{pin, {GGTName, GGTNode}} ->
		GGT = {GGTName, GGTNode}
	end,
	GGT ! {sendy, Mi},
	sendeY(PIDns, T, MiRest)
	.
	
prompt(_PIDns, []) ->
	ok;
prompt(PIDns, [H | T]) ->
	PIDns ! {self(), {lookup, H}},
	receive
		{pin, {GGTName, GGTNode}} ->
		GGT = {GGTName, GGTNode}
	end,
	GGT ! {self(), tellmi},
	receive
		{mi, Mi} ->
			logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: ggT-Prozess ~p ~p aktuelles Mi ~p (~p).~n", [H, element(2, GGT), Mi, werkzeug:timeMilliSecond()])))
	end,
	prompt(PIDns, T)
	.
	
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
			logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: ggT-Prozess ~p ist lebendig (~p).~n", [GGTname, werkzeug:timeMilliSecond()])))
	end,
	nudge(PIDns, T)
	.
	
kill(_PIDns, []) ->
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
	
readConfig() ->
	{ok, CfgList} = file:consult("koordinator.cfg"),
	logging(?LOGFILE, lists:flatten(io_lib:format("Koordinator: koordinator.cfg geoeffnet...~n", []))),
	{get_config_value(arbeitszeit, CfgList),
	get_config_value(termzeit, CfgList),
	get_config_value(ggtprozessnummer, CfgList),
	get_config_value(nameservicenode, CfgList),
	get_config_value(nameservicename, CfgList),
	get_config_value(koordinatorname, CfgList),
	get_config_value(quote, CfgList),
	get_config_value(korrigieren, CfgList)}
	.
	
print(String) ->
	_ok = io:format(String)
	.
