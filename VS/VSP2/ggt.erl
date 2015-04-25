-module(ggt).
-export([start/10]).
-import(werkzeug, [logging/2, timeMilliSecond/0, reset_timer/3, getUTC/0]).

%Initialisierungsphase
start(ArbeitsZeit, TermZeit, ProzessNummer, StarterNummer, PraktikumsGruppe, TeamNummer, Nameservicename, Nameservicenode, Koordinatorname, Quota) ->
	MeinName = list_to_atom(lists:flatten(io_lib:format("~p~p~p~p", [PraktikumsGruppe, TeamNummer, ProzessNummer, StarterNummer]))),
	{ok, Hostname} = inet:gethostname(),
	LogFile = lists:flatten(io_lib:format("GGTP_~p@" ++ Hostname ++ ".log", [MeinName])),
	logging(LogFile, lists:flatten(io_lib:format("~p Startzeit: " ++ timeMilliSecond() ++ " mit PID ~p auf ~p~n", [MeinName, self(), node()]))),
	register(MeinName, self()),
	{Nameservicename, Nameservicenode} ! {self(), {rebind, MeinName, node()}},
	logging(LogFile, lists:flatten(io_lib:format("beim Namensdienst und auf Node lokal registriert.~n", []))),
	{Nameservicename, Nameservicenode} ! {self(), {lookup, Koordinatorname}},
	receive
		{pin, {KoordinatornameNeu, Koordinatornode}} ->
			logging(LogFile, lists:flatten(io_lib:format("Koordinatornode erhalten : ~p.~n", [Koordinatornode])));
		not_found ->
			%ggf. anders loesen
			logging(LogFile, lists:flatten(io_lib:format("*********ABBRUCH*********Koordinator nicht gefunden*********ABBRUCH*********~n", []))),
			KoordinatornameNeu = Koordinatorname,
			Koordinatornode = undefm,
			exit(self(), "Koordinator nicht gefunden. ABBRUCH")
	end,
	%Meldung beim Koordinator
	{KoordinatornameNeu, Koordinatornode} ! {hello, MeinName},
	logging(LogFile, lists:flatten(io_lib:format("beim Koordinator gemeldet.~n", []))),
	receive 
		{setneighbors, LeftN, RightN} ->
			logging(LogFile, lists:flatten(io_lib:format("Linker Nachbar ~p  gebunden.~n", [LeftN]))),
			logging(LogFile, lists:flatten(io_lib:format("Rechter Nachbar ~p gebunden.~n", [RightN])))
	end,
	%Node-Informationen über Nameservice des linken Nachbarn holen
	{Nameservicename, Nameservicenode} ! {self(), {lookup, LeftN}},
	receive
		{pin, {LeftNNeu, LeftNnode}} ->
			LeftNNeuNeu = LeftNNeu
	end,
	%Node-Informationen über Nameservice des rechten Nachbarn holen
	{Nameservicename, Nameservicenode} ! {self(), {lookup, RightN}},
	receive
		{pin, {RightNNeu, RightNnode}} ->
			RightNNeuNeu = RightNNeu
	end,
	loop(MeinName, LogFile, ArbeitsZeit, TermZeit, {Nameservicename, Nameservicenode}, {KoordinatornameNeu, Koordinatornode}, {LeftNNeuNeu, LeftNnode}, {RightNNeuNeu, RightNnode}, Quota).

%Arbeits-, Terminierungs- und Beendigungsphase
%es werden noch weitere Variablen benoetigt die hier angelegt und mitgegeben werden
loop(MeinName, LogFile, ArbeitsZeit, TermZeit, Nameservice, Koordinator, LeftN, RightN, Quota) ->
	loop(MeinName, LogFile, -1, ArbeitsZeit, TermZeit, Nameservice, Koordinator, LeftN, RightN, Quota, timer:start(), -1, false, false).

%Fall, wenn noch KEIN kill vom Koordinator kam
loop(MeinName, LogFile, Mi, ArbeitsZeit, TermZeit, Nameservice, Koordinator, LeftN, RightN, Quota, Timer, Time, ErsteAnfrage, false) ->
	receive
		%MiNeu setzen und wieder in receive-Block springen
		{setpm, MiNeu} ->
			TimerNeu = reset_timer(Timer, TermZeit, {terminated, terminterrupt}),
			TimeNeu = getUTC(),
			ErsteAnfrageNeu = true,
			logging(LogFile, lists:flatten(io_lib:format("setpm: ~p. (~p)~n", [MiNeu, MeinName]))),
			loop(MeinName, LogFile, MiNeu, ArbeitsZeit, TermZeit, Nameservice, Koordinator, LeftN, RightN, Quota, TimerNeu, TimeNeu, ErsteAnfrageNeu, false);
		%moegliches neues Y und impliziete Aufforderung, dass ggT-Algorithmus gestartet wird
		{sendy, Y} ->
			TimerNeu = reset_timer(Timer, TermZeit, {terminated, terminterrupt}),
			TimeNeu = getUTC(),
			ErsteAnfrageNeu = true,
			{Reason, CMi} = ggtAlgorithmus(Mi, Y, LeftN, RightN, ArbeitsZeit, LogFile),
			%Nach Berechnung des ggT wird nun geprueft, ob und welche Nachricht an Koordinator gesendet werden muss
			case Reason of
				changed ->
					Koordinator ! {briefme, {MeinName, CMi, timeMilliSecond()}},
					logging(LogFile, lists:flatten(io_lib:format("~p: Mi: ~p geandert und an Koordinator (~p) gesendet~n", [MeinName, CMi, Koordinator])));
				terminated ->
					Koordinator ! {briefterm, {MeinName, CMi, timeMilliSecond()}},
					logging(LogFile, lists:flatten(io_lib:format("~p: Mi: ~p ggT erfolgreich berechnet und an Koordinator (~p) gesendet~n", [MeinName, CMi, Koordinator])));
				notchanged ->
					logging(LogFile, lists:flatten(io_lib:format("~p: keine Nachricht an Koordinator gesendet~n", [MeinName])))
			end,
			loop(MeinName, LogFile, CMi, ArbeitsZeit, TermZeit, Nameservice, Koordinator, LeftN, RightN, Quota, TimerNeu, TimeNeu, ErsteAnfrageNeu, false);			
		%moegliche Nachricht von Koordinator um aktuelles Mi zu erhalten
		{KoordinatorPID, tellmi} ->
			KoordinatorPID ! {mi, Mi},
			loop(MeinName, LogFile, Mi, ArbeitsZeit, TermZeit, Nameservice, Koordinator, LeftN, RightN, Quota, Timer, Time, ErsteAnfrage, false);
		%moegliches ping von Seiten des Koordinators
		{KoordinatorPID, pingGGT} ->
			KoordinatorPID ! {pongGGT, MeinName},
			loop(MeinName, LogFile, Mi, ArbeitsZeit, TermZeit, Nameservice, Koordinator, LeftN, RightN, Quota, Timer, Time, ErsteAnfrage, false);
		%der Timer schickt ein Interrupt, da nun die TermZeit abgelaufen ist une eine Terminierungsabstimmung durchgefuehrt werden muss
		{terminated, terminterrupt} ->
			%TODO
			ok;
		%bei kill wird die Schleife verlassen: letztes Flag im Methodenkopf = true
		kill ->
			loop(MeinName, LogFile, Mi, ArbeitsZeit, TermZeit, Nameservice, Koordinator, LeftN, RightN, Quota, Timer, Time, ErsteAnfrage, true)
	end;
%Fall, wenn EIN kill vom Koordinator kam -> Beendigungsphase
loop(MeinName, LogFile, _Mi, _ArbeitsZeit, _TermZeit, Nameservice, _Koordinator, _LeftN, _RightN, _Quota, Timer, _Time, _ErsteAnfrage, true) ->
	Nameservice ! {self(), {unbind, MeinName}},
	timer:cancel(Timer),
	logging(LogFile, lists:flatten(io_lib:format("Downtime: " ++ timeMilliSecond() ++ " vom Client ~p~n", [MeinName]))).
	
%verteilten ggT-Algorithmus anwenden
ggtAlgorithmus(MiNeu, Y, LeftN, RightN, ArbeitsZeit, LogFile) when Y < MiNeu ->
	%Arbeitszeit muss vor dem versenden an die Nachbarn verrichtet werden
	timer:sleep(ArbeitsZeit),
	CMi = ((MiNeu - 1) rem Y) + 1,
	LeftN ! {sendy, CMi},
	RightN ! {sendy, CMi},
	logging(LogFile, lists:flatten(io_lib:format("sendy: ~p (~p); berechnet als neues Mi ~p. " ++ timeMilliSecond() ++ " ~n", [Y, MiNeu, CMi]))),
	ggtAlgorithmusDecision(Y, CMi);
ggtAlgorithmus(MiNeu, Y, _LeftN, _RightN, _Arbeitszeit, LogFile) ->
	logging(LogFile, lists:flatten(io_lib:format("sendy: ~p (~p); keine Berechnung~n", [Y, MiNeu]))),
	{notchanged, -1}.

%Pruefung, ob der ggT berechnet wurde, damit Koordinator die Terminierung mitgeteilt werden kann
ggtAlgorithmusDecision(Y, CMi) when Y == CMi ->
	{terminated, CMi};
ggtAlgorithmusDecision(_Y, CMi) ->
	{changed, CMi}.









