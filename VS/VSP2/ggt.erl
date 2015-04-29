-module(ggt).
-export([start/10]).
-import(werkzeug, [logging/2, timeMilliSecond/0, reset_timer/3, getUTC/0]).

%Initialisierungsphase
start(ArbeitsZeit, TermZeit, ProzessNummer, StarterNummer, PraktikumsGruppe, TeamNummer, Nameservicename, Nameservicenode, Koordinatorname, Quota) ->
	MeinName = list_to_atom(lists:flatten(io_lib:format("~p~p~p~p", [PraktikumsGruppe, TeamNummer, ProzessNummer, StarterNummer]))),
	{ok, Hostname} = inet:gethostname(),
	LogFile = lists:flatten(io_lib:format("ggt_log/GGTP_~p@" ++ Hostname ++ ".log", [MeinName])),
	logging(LogFile, lists:flatten(io_lib:format("~p Startzeit: " ++ timeMilliSecond() ++ " mit PID ~p auf ~p~n", [MeinName, self(), node()]))),
	register(MeinName, self()),
	{Nameservicename, Nameservicenode} ! {self(), {rebind, MeinName, node()}},
	receive 
		ok ->
			logging(LogFile, lists:flatten(io_lib:format("beim Namensdienst und auf Node lokal registriert.~n", [])))
	end,
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
	
	%auf Nachbarn warten und ggf. andere befehle vom Koordinator empfangen
	{LeftN, RightN} = waitingForNeighbors(MeinName, LogFile, {Nameservicename, Nameservicenode}, undef),
	
	%wurde kill befehl in der initialisierungsphase empfangen, wird nichts getan,
	%sonst geht der normale ablauf weiter.
	case (LeftN == -1) and (RightN == -1) of
		true ->
			do_nothing;
		false ->
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
			loop(MeinName, LogFile, ArbeitsZeit, TermZeit, {Nameservicename, Nameservicenode}, {KoordinatornameNeu, Koordinatornode}, {LeftNNeuNeu, LeftNnode}, {RightNNeuNeu, RightNnode}, Quota)
	end.

%Arbeits-, Terminierungs- und Beendigungsphase
%es werden noch weitere Variablen benoetigt die hier angelegt und mitgegeben werden
loop(MeinName, LogFile, ArbeitsZeit, TermZeit, Nameservice, Koordinator, LeftN, RightN, Quota) ->
	%loop(MeinName, LogFile, Mi, ArbeitsZeit, TermZeit, Nameservice, Koordinator, LeftN, RightN, Quota, TermTimer, Time, ErsetAnfrage, Termmeldungen, VoteYesCounter, VoteTimer, Killed)
	loop(MeinName, LogFile, -1, ArbeitsZeit, TermZeit, Nameservice, Koordinator, LeftN, RightN, Quota, timer:start(), -1, false, 0, 0, timer:start(), false).

%Fall, wenn noch KEIN kill vom Koordinator kam
loop(MeinName, LogFile, Mi, ArbeitsZeit, TermZeit, Nameservice, Koordinator, LeftN, RightN, Quota, TermTimer, Time, ErsteAnfrage, Termmeldungen, VoteYesCounter, VoteTimer, false) ->
	receive
		%MiNeu setzen und wieder in receive-Block springen
		{setpm, MiNeu} ->
			TermTimerNeu = reset_timer(TermTimer, TermZeit, {terminated, terminterrupt}),
			TimeNeu = getUTC(),
			ErsteAnfrageNeu = true,
			logging(LogFile, lists:flatten(io_lib:format("setpm: ~p. (~p)~n", [MiNeu, MeinName]))),
			loop(MeinName, LogFile, MiNeu, ArbeitsZeit, TermZeit, Nameservice, Koordinator, LeftN, RightN, Quota, TermTimerNeu, TimeNeu, ErsteAnfrageNeu, Termmeldungen, VoteYesCounter, VoteTimer, false);
		%moegliches neues Y und impliziete Aufforderung, dass ggT-Algorithmus gestartet wird
		{sendy, Y} ->
			TermTimerNeu = reset_timer(TermTimer, TermZeit, {terminated, terminterrupt}),
			TimeNeu = getUTC(),
			ErsteAnfrageNeu = true,
			{Reason, CMi} = ggtAlgorithmus(Mi, Y, LeftN, RightN, ArbeitsZeit, LogFile),
			%Nach Berechnung des ggT wird nun geprueft, ob und welche Nachricht an Koordinator gesendet werden muss
			%changed := Mi wurde neu berechnet -> briefmi an Koordinator
			%terminated := ggT wurde berechnet -> briefterm an Koordinator
			%notchanged := der ggT-Algorithmus wurde nicht angewendet -> nichts an Koordinator senden
			case Reason of
				changed ->
					Koordinator ! {briefmi, {MeinName, CMi, timeMilliSecond()}},
					logging(LogFile, lists:flatten(io_lib:format("~p: Mi: ~p geandert und an Koordinator (~p) gesendet~n", [MeinName, CMi, Koordinator])));
				terminated ->
					Koordinator ! {self(), briefterm, {MeinName, CMi, timeMilliSecond()}},
					logging(LogFile, lists:flatten(io_lib:format("~p: Mi: ~p ggT erfolgreich berechnet und an Koordinator (~p) gesendet~n", [MeinName, CMi, Koordinator])));
				notchanged ->
					logging(LogFile, lists:flatten(io_lib:format("~p: keine Nachricht an Koordinator gesendet~n", [MeinName])))
			end,
			loop(MeinName, LogFile, CMi, ArbeitsZeit, TermZeit, Nameservice, Koordinator, LeftN, RightN, Quota, TermTimerNeu, TimeNeu, ErsteAnfrageNeu, Termmeldungen, VoteYesCounter, VoteTimer, false);			
		%moegliche Nachricht von Koordinator um aktuelles Mi zu erhalten
		{KoordinatorPID, tellmi} ->
			KoordinatorPID ! {mi, Mi},
			loop(MeinName, LogFile, Mi, ArbeitsZeit, TermZeit, Nameservice, Koordinator, LeftN, RightN, Quota, TermTimer, Time, ErsteAnfrage, Termmeldungen, VoteYesCounter, VoteTimer, false);
		%moegliches ping von Seiten des Koordinators
		{KoordinatorPID, pingGGT} ->
				KoordinatorPID ! {pongGGT, MeinName},
				loop(MeinName, LogFile, Mi, ArbeitsZeit, TermZeit, Nameservice, Koordinator, LeftN, RightN, Quota, TermTimer, Time, ErsteAnfrage, Termmeldungen, VoteYesCounter, VoteTimer, false);
		%der TermTimer schickt ein Interrupt, da nun die TermZeit abgelaufen ist une eine Terminierungsabstimmung durchgefuehrt werden muss
		{terminated, terminterrupt} ->
			case ErsteAnfrage of
				true ->
					logging(LogFile, lists:flatten(io_lib:format("~p: initiiere die ~pte Terminierungsabstimmung (~p). " ++ timeMilliSecond() ++ "~n", [MeinName, Termmeldungen, VoteYesCounter]))),
					Nameservice ! {self(), {multicast, vote, MeinName}},
					%neuen VoteTimer starten, da es vorkommen kann, dass 2 Votierungen stattfinden, daher darf der schon existierende Votetimer
					%nicht unterbrochen werden
					VoteTimerNeu = reset_timer(timer:start(), 500, {terminated, voteinterrupt}),
					TermTimerNeu = reset_timer(timer:start(), TermZeit, {terminated, terminterrupt}),
					loop(MeinName, LogFile, Mi, ArbeitsZeit, TermZeit, Nameservice, Koordinator, LeftN, RightN, Quota, TermTimerNeu, Time, false, Termmeldungen, VoteYesCounter, VoteTimerNeu, false);
				false ->
					%neuen Timer starten
					logging(LogFile, lists:flatten(io_lib:format("~p: eine weitere Terminierungsabstimmung ist nicht moeglich, da noch keine neue Zahl eingetroffen ist. " ++ timeMilliSecond() ++ "~n", [MeinName]))),
					TermTimerNeu = reset_timer(timer:start(), TermZeit, {terminated, terminterrupt}),
					loop(MeinName, LogFile, Mi, ArbeitsZeit, TermZeit, Nameservice, Koordinator, LeftN, RightN, Quota, TermTimerNeu, Time, ErsteAnfrage, Termmeldungen, VoteYesCounter, VoteTimer, false)
			end;
		%Interrupt des VoteTimer ist eingetreten -> Voting wird abgebrochen
		{terminated, voteinterrupt} ->
			logging(LogFile, lists:flatten(io_lib:format("~p: Voting wurde abgebrochen, da Zeit zur Rueckmeldung vorbei ist.~n", [MeinName]))),
			loop(MeinName, LogFile, Mi, ArbeitsZeit, TermZeit, Nameservice, Koordinator, LeftN, RightN, Quota, TermTimer, Time, ErsteAnfrage, Termmeldungen, 0, timer:start(), false);
		{_InitiatorPID, {vote, Initiator}} ->
			TimeNeu = getUTC(),
			TimeDiff = (TimeNeu - Time) div 1000,		
			case TimeDiff > round(TermZeit / 2) of
				true -> 
					%ein lookup ist noetig, da der Initiator auf einer ganz anderen Node laufen kann
					Nameservice ! {self(), {lookup, Initiator}},
					receive
						{pin, {InitiatorNeu, Initiatornode}} ->
							{InitiatorNeu, Initiatornode} ! {voteYes, MeinName}
					end
			end,
			loop(MeinName, LogFile, Mi, ArbeitsZeit, TermZeit, Nameservice, Koordinator, LeftN, RightN, Quota, TermTimer, Time, ErsteAnfrage, Termmeldungen, VoteYesCounter, VoteTimer, false);
		%Zustimmungen zum Voting treffen ein und werden behandelt
		{voteYes, AbsenderName} ->
			logging(LogFile, lists:flatten(io_lib:format("~p: stimme ab (~p): mit >JA< gestimmt. " ++ timeMilliSecond() ++ "~n", [MeinName, AbsenderName]))),
			VoteYesCounterNeu = VoteYesCounter + 1,
			io:format("Voteyes vor case: ~p Quota: ~p~n", [VoteYesCounterNeu, Quota]),
			case VoteYesCounterNeu == Quota of
				true ->
					%VoteTimer muss beendet werden, da sonst faelschlicherweise ein Interrupt eintrifft
					timer:cancel(VoteTimer),
					Koordinator ! {self(), briefterm, {MeinName, Mi, timeMilliSecond()}},
					TermmeldungenNeu = Termmeldungen + 1,
					io:format("Voteyes in case: ~p~n", [VoteYesCounterNeu]),
					io:format("Termmeldungenneu in case: ~p~n", [TermmeldungenNeu]),
					logging(LogFile, lists:flatten(io_lib:format("~p: Koordinator ~pte Terminierung gemeldet mit ~p " ++ timeMilliSecond() ++ "~n", [MeinName, TermmeldungenNeu, VoteYesCounterNeu]))),
					loop(MeinName, LogFile, Mi, ArbeitsZeit, TermZeit, Nameservice, Koordinator, LeftN, RightN, Quota, TermTimer, Time, ErsteAnfrage, TermmeldungenNeu, 0, timer:start(), false);
				false ->
					loop(MeinName, LogFile, Mi, ArbeitsZeit, TermZeit, Nameservice, Koordinator, LeftN, RightN, Quota, TermTimer, Time, ErsteAnfrage, Termmeldungen, VoteYesCounterNeu, VoteTimer, false)
			end;
		%bei kill wird die Schleife verlassen: letztes Flag im Methodenkopf = true
		kill ->
			loop(MeinName, LogFile, Mi, ArbeitsZeit, TermZeit, Nameservice, Koordinator, LeftN, RightN, Quota, TermTimer, Time, ErsteAnfrage, Termmeldungen, VoteYesCounter, VoteTimer, true);
		%falls irgendwelche anderen, nicht zutreffenden, Nachrichten eintreffen werden diese geloggt
		Any ->
			logging(LogFile, lists:flatten(io_lib:format("~p: Es wurde irgendetwas anderes empfangen: ~p " ++ timeMilliSecond() ++ "~n", [MeinName, Any]))),
			loop(MeinName, LogFile, Mi, ArbeitsZeit, TermZeit, Nameservice, Koordinator, LeftN, RightN, Quota, TermTimer, Time, ErsteAnfrage, Termmeldungen, VoteYesCounter, VoteTimer, false)	
	end;
%Fall, wenn EIN kill vom Koordinator kam -> Beendigungsphase
loop(MeinName, LogFile, _Mi, _ArbeitsZeit, _TermZeit, Nameservice, _Koordinator, _LeftN, _RightN, _Quota, TermTimer, _Time, __ErsteAnfrage, _Termmeldungen, _VoteYesCounter, _VoteTimer, true) ->
	unbind(MeinName, LogFile, Nameservice, TermTimer).
	
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
	
waitingForNeighbors(MeinName, LogFile, Nameservice, TermTimer) ->
	receive 
		{setneighbors, LeftN, RightN} ->
			logging(LogFile, lists:flatten(io_lib:format("Linker Nachbar ~p  gebunden.~n", [LeftN]))),
			logging(LogFile, lists:flatten(io_lib:format("Rechter Nachbar ~p gebunden.~n", [RightN]))),
			{LeftN, RightN};
		{KoordinatorPID, tellmi} ->
			KoordinatorPID ! {mi, undef},
			waitingForNeighbors(MeinName, LogFile, Nameservice, TermTimer);
		{KoordinatorPID, pingGGT} ->
			KoordinatorPID ! {pongGGT, MeinName},
			waitingForNeighbors(MeinName, LogFile, Nameservice, TermTimer);
		kill ->
			unbind(MeinName, LogFile, Nameservice, TermTimer),
			{-1, -1}
	end.

unbind(MeinName, LogFile, Nameservice, TermTimer) ->
	Nameservice ! {self(), {unbind, MeinName}},
	case TermTimer == undef of
		true ->
			no_timer_to_cancel;
		false ->
			timer:cancel(TermTimer)
	end,
	receive ok ->
		unregister(MeinName),
		logging(LogFile, lists:flatten(io_lib:format("Downtime: " ++ timeMilliSecond() ++ " vom Client ~p~n", [MeinName])))
	end.
