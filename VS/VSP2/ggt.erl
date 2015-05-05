-module(ggt).
-export([start/9]).
-import(werkzeug, [logging/2, timeMilliSecond/0, reset_timer/3, getUTC/0]).

%***************In den Kommentaren wird teilweise Bezug auf unseren Entwurf genommen.***************
%***************Die angegebenen Anforderungsnummern entsprechen unseren, im Entwurf,***************
%***************eigenen Anforderungsnummern für das Modul ggT-Prozess (Anf.-Nr.) (siehe 1.3.Konventionen im Entwurf).***************

%Initialisierungsphase
start(ArbeitsZeit, TermZeit, ProzessNummer, StarterNummer, PraktikumsGruppe, TeamNummer, Nameservice, Koordinatorname, Quota) ->
	%Anf.-Nr. 1, 2) Name des ggT-Prozesses definieren
	MeinName = list_to_atom(lists:flatten(io_lib:format("~p~p~p~p", [PraktikumsGruppe, TeamNummer, ProzessNummer, StarterNummer]))),
	{ok, Hostname} = inet:gethostname(),
	%Anf.-Nr. 19) Name der Log-Datei erzeugen
	LogFile = lists:flatten(io_lib:format("ggt_log/GGTP_~p@" ++ Hostname ++ ".log", [MeinName])),
	logging(LogFile, lists:flatten(io_lib:format("~p Startzeit: " ++ timeMilliSecond() ++ " mit PID ~p auf ~p~n", [MeinName, self(), node()]))),
	%Anf.-Nr. 5) lokal registrieren
	register(MeinName, self()),
	%Anf.-Nr. 4) An Namensdiesnt binden 
	Nameservice ! {self(), {rebind, MeinName, node()}},
	receive 
		ok ->
			logging(LogFile, lists:flatten(io_lib:format("beim Namensdienst und auf Node lokal registriert.~n", [])))
	end,
	
	%Beim Nameservice die Node des Koordinators erfragen
	Nameservice ! {self(), {lookup, Koordinatorname}},
	receive
		{pin, {KoordinatornameNeu, Koordinatornode}} ->
			logging(LogFile, lists:flatten(io_lib:format("Koordinatornode erhalten : ~p.~n", [Koordinatornode])));
		not_found ->
			logging(LogFile, lists:flatten(io_lib:format("*********ABBRUCH*********Koordinator nicht gefunden*********~n", []))),
			KoordinatornameNeu = Koordinatorname,
			Koordinatornode = undef,
			exit(self(), "Koordinator nicht gefunden. *********ABBRUCH*********")
	end,
	%Anf.-Nr. 3) Meldung beim Koordinator
	{KoordinatornameNeu, Koordinatornode} ! {hello, MeinName},
	logging(LogFile, lists:flatten(io_lib:format("beim Koordinator gemeldet.~n", []))),
	
	%Anf.-Nr. 6) auf Nachbarn warten und ggf. andere befehle vom Koordinator empfangen
	{LeftN, RightN} = waitingForNeighbors(MeinName, LogFile),
	
	%wurde kill-Befehl in der Initialisierungsphase empfangen, wird der Prozess
	%beendet, sonst geht der normale Ablauf weiter.
	case (LeftN == -1) and (RightN == -1) of
		true ->
			unbind(MeinName, LogFile, Nameservice, undef);
		false ->
			%Node-Informationen über Nameservice des linken Nachbarn holen
			Nameservice ! {self(), {lookup, LeftN}},
			receive
				{pin, {LeftNNeu, LeftNnode}} ->
					LeftNNeuNeu = LeftNNeu
			end,
			%Node-Informationen über Nameservice des rechten Nachbarn holen
			Nameservice ! {self(), {lookup, RightN}},
			receive
				{pin, {RightNNeu, RightNnode}} ->
					RightNNeuNeu = RightNNeu
			end,
			loop(MeinName, LogFile, ArbeitsZeit, TermZeit, Nameservice, {KoordinatornameNeu, Koordinatornode}, {LeftNNeuNeu, LeftNnode}, {RightNNeuNeu, RightNnode}, Quota)
	end.

%Arbeits-, Terminierungs- und Beendigungsphase
%es werden noch weitere Variablen benoetigt die hier angelegt und mitgegeben werden
loop(MeinName, LogFile, ArbeitsZeit, TermZeit, Nameservice, Koordinator, LeftN, RightN, Quota) ->
%Uebersicht zu den uebergebenen Parametern:
%MeinName := Name des ggT-Prozesses
%LogFile := Bezeichnung der Log-Datei
%Mi := aktuelles Mi (zu Beginn -1)
%ArbeitsZeit := Arbeitszeit (in Sekunden)
%TermZeit := Terminierungszeit (in Sekunden)
%Nameservice := PID des Namensdienst
%Koordinator := Koordinatorkontakt ({Koordinatorname, Koordinatornode})
%LeftN := Linker Nachbar
%RightN := rechter Nachbar
%Quota := vom Koordinator berechnete Quote
%TermTimer := Timer zum Signalisieren, ob eine Terminierungsabstimmung durchgefuehrt werden muss
%Time := Wird nach dem Erhalt einer Zahl (sendy, setpm) gesetzt um die Zeitdifferenz zu berechnen um zu entscheiden, ob eine voteYes-Nachricht versandt wird (zu Beginn -1)
%ErsetAnfrage := Flag zur Signalisierung, ob eine weitere Terminierungsabstimmung gestartet werden darf (zu Beginn false)
%Termmeldungen := Counter zur Terminierungsmeldungen (zu Beginn 0)
%VoteYesCounter := Counter zum Zaehlen der eingetroffenen voteYes-Nachrichten pro Terminierungsmeldung (zu Beginn 0)
%VoteTimer := Timer zur Terminierung einer Terminierungsabstimmung, sofern eine gewisse Zeit ueberschritten wurde und die Quota noch nicht erreicht wurde
%Killed := Flag zur Signalisierungs, ob ein Kill-Befehl seitens des Koordinators eingetroffen ist (zu Beginn false)
	loop(MeinName, LogFile, -1, ArbeitsZeit, TermZeit, Nameservice, Koordinator, LeftN, RightN, Quota, timer:start(), -1, false, 0, 0, timer:start(), false).

%In diesem receive-Block steht der ggT-Prozess fuer alle moeglichen Nachrichten seitens des Koordinators
%und anderer ggT-Prozessen bereit. Ueber den letzten Parameter wird angegeben, ob ein kill-Befehl 
%vom Koordinator empfangen wurde. In diesem Fall, wurde noch KEIN kill vom Koordinator empfangen
%In dieser Methode werden u.a. folgende Anforderungen realisiert:
%Anf.-Nr. 8) ggT ist jederzeit bereit fuer neue Berechnungen
%Anf.-Nr. 18) es wird in eine Log-Datei geloggt
%Weitere realisierte Anforderungen sind ggf. an den Nachrichtenempfangsblöcken angegeben
loop(MeinName, LogFile, Mi, ArbeitsZeit, TermZeit, Nameservice, Koordinator, LeftN, RightN, Quota, TermTimer, Time, ErsteAnfrage, Termmeldungen, VoteYesCounter, VoteTimer, false) ->
	receive
		%Anf.-Nr. 7) MiNeu setzen und wieder in receive-Block springen
		{setpm, MiNeu} ->
			TermTimerNeu = reset_timer(TermTimer, TermZeit, {terminated, terminterrupt}),
			TimeNeu = getUTC(),
			%Anf.-Nr. 13) Eine neue Terminierungsabstimmung ist nun moeglich
			ErsteAnfrageNeu = true,
			logging(LogFile, lists:flatten(io_lib:format("setpm: ~p. (~p)~n", [MiNeu, MeinName]))),
			loop(MeinName, LogFile, MiNeu, ArbeitsZeit, TermZeit, Nameservice, Koordinator, LeftN, RightN, Quota, TermTimerNeu, TimeNeu, ErsteAnfrageNeu, Termmeldungen, VoteYesCounter, VoteTimer, false);
		%Anf.-Nr. 9) moegliches neues Y und impliziete Aufforderung, dass ggT-Algorithmus gestartet wird
		{sendy, Y} ->
			TermTimerNeu = reset_timer(TermTimer, TermZeit, {terminated, terminterrupt}),
			TimeNeu = getUTC(),
			%Anf.-Nr. 13) Eine neue Terminierungsabstimmung ist nun moeglich
			ErsteAnfrageNeu = true,
			{Reason, CMi} = ggtAlgorithmus(Mi, Y, LeftN, RightN, ArbeitsZeit, LogFile),
			%Anf.-Nr. 10) Nach Berechnung des ggT wird nun geprueft, ob und welche Nachricht an Koordinator gesendet werden muss
			%changed := Mi wurde neu berechnet -> briefmi an Koordinator
			%terminated := ggT wurde berechnet -> briefterm an Koordinator
			%notchanged := der ggT-Algorithmus wurde nicht angewendet -> nichts an Koordinator senden
			case Reason of
				changed ->
					Koordinator ! {briefmi, {MeinName, CMi, timeMilliSecond()}},
					logging(LogFile, lists:flatten(io_lib:format("~p: Mi: ~p geandert und an Koordinator gesendet~n", [MeinName, CMi])));
				terminated ->
					Koordinator ! {self(), briefterm, {MeinName, CMi, timeMilliSecond()}},
					logging(LogFile, lists:flatten(io_lib:format("~p: Mi: ~p ggT erfolgreich berechnet und an Koordinator gesendet~n", [MeinName, CMi])));
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
		%Anf.-Nr. 12) der TermTimer signalisiert, dass eine bestimmte Zeit (TermZeit) abgelaufen ist und seit dem Start dieses Timers keine
		%neue Zahl (sendy, setpm) empfangen wurde. Daher wird ein Interrupt als Nachricht geschickt, da nun die TermZeit abgelaufen ist
		%und eine Terminierungsabstimmung durchgefuehrt werden muss
		{terminated, terminterrupt} ->
			%Anf.-Nr. 13) Sofern dieses Flag nicht gesetzt ist (false) darf auch keine weitere Terminierungsabstimmung erfolgen
			case ErsteAnfrage of
				true ->
					logging(LogFile, lists:flatten(io_lib:format("~p: initiiere eine Terminierungsabstimmung (~p). " ++ timeMilliSecond() ++ "~n", [MeinName, Mi]))),
					%Anf.-Nr. 12) Multicast durchfuehren
					Nameservice ! {self(), {multicast, vote, MeinName}},
					%neuen VoteTimer starten, da es vorkommen kann, dass 2 Votierungen stattfinden, daher darf der schon existierende Votetimer
					%nicht unterbrochen werden
					VoteTimerNeu = reset_timer(timer:start(), 200, {terminated, voteinterrupt}),
					TermTimerNeu = reset_timer(timer:start(), TermZeit, {terminated, terminterrupt}),
					%VoteYesCounter auf 0 setzen, damit die "alten" voteYes-Nachrichten unberücksichtig bleiben 
					loop(MeinName, LogFile, Mi, ArbeitsZeit, TermZeit, Nameservice, Koordinator, LeftN, RightN, Quota, TermTimerNeu, Time, false, Termmeldungen, 0, VoteTimerNeu, false);
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
		%Anf.-Nr. 16) Ein Multicast durch den Initiator wurde durchgefuehrt. Wurde die haelfte der Zeit nach dem letzten Empfangen
		%einer Zahl (sendy, setpm) ueberschritten, wird dem Initiator eine voteYes-Nachricht geschickt
		{_InitiatorPID, {vote, Initiator}} ->
			TimeNeu = getUTC(),
			TimeDiff = (TimeNeu - Time) / 1000,		
			case TimeDiff > TermZeit / 2 of
				true -> 
					%ein lookup ist noetig, da der Initiator auf einer ganz anderen Node laufen kann
					Nameservice ! {self(), {lookup, Initiator}},
					receive
						{pin, {InitiatorNeu, Initiatornode}} ->
							{InitiatorNeu, Initiatornode} ! {voteYes, MeinName};
					not_found ->
						logging(LogFile, lists:flatten(io_lib:format("Initiator wurde beim Namensdienst nicht gefunden~n", [])))
					end;
				false ->
					%in dem Log des Initiators die "Nein-Abstimmung" eintragen
					{ok, Hostname} = inet:gethostname(),
					logging(lists:flatten(io_lib:format("ggt_log/GGTP_~p@" ++ Hostname ++ ".log", [Initiator])), lists:flatten(io_lib:format("~p: stimme ab (~p): mit >NEIN< gestimmt und ignoriert. " ++ timeMilliSecond() ++ "~n", [Initiator, MeinName])))
			end,
			loop(MeinName, LogFile, Mi, ArbeitsZeit, TermZeit, Nameservice, Koordinator, LeftN, RightN, Quota, TermTimer, Time, ErsteAnfrage, Termmeldungen, VoteYesCounter, VoteTimer, false);
		%%Anf.-Nr. 14) Zustimmungen zum Voting treffen ein und werden behandelt: Wurde die Quota erreich wird dem Koordinator 
		%eine briefterm-Nachricht geschickt
		{voteYes, AbsenderName} ->
			logging(LogFile, lists:flatten(io_lib:format("~p: stimme ab (~p): mit >JA< gestimmt. " ++ timeMilliSecond() ++ "~n", [MeinName, AbsenderName]))),
			VoteYesCounterNeu = VoteYesCounter + 1,
			case VoteYesCounterNeu == Quota of
				true ->
					%VoteTimer muss beendet werden, da sonst faelschlicherweise ein Interrupt eintrifft
					timer:cancel(VoteTimer),
					%Anf.-Nr. 14) Terminierung der Berechnung: Name, aktuelle Mi und aktuelle Systemzeit
					Koordinator ! {self(), briefterm, {MeinName, Mi, timeMilliSecond()}},
					%Anf.-Nr. 15) Terminierungsmeldungen hochzaehlen und loggen
					TermmeldungenNeu = Termmeldungen + 1,
					logging(LogFile, lists:flatten(io_lib:format("~p: Koordinator ~pte Terminierung gemeldet mit ~p " ++ timeMilliSecond() ++ "~n", [MeinName, TermmeldungenNeu, Mi]))),
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
%Wurde seitens des Koordinators ein kill-Befehl geschickt, wird die Beendigungsphase eingeleitet
loop(MeinName, LogFile, _Mi, _ArbeitsZeit, _TermZeit, Nameservice, _Koordinator, _LeftN, _RightN, _Quota, TermTimer, _Time, __ErsteAnfrage, _Termmeldungen, _VoteYesCounter, _VoteTimer, true) ->
	unbind(MeinName, LogFile, Nameservice, TermTimer).
	
%verteilten ggT-Algorithmus anwenden
ggtAlgorithmus(MiNeu, Y, LeftN, RightN, ArbeitsZeit, LogFile) when Y < MiNeu ->
	%Anf.-Nr. 11) Arbeitszeit muss vor dem versenden an die Nachbarn verrichtet werden
	timer:sleep(ArbeitsZeit),
	CMi = ((MiNeu - 1) rem Y) + 1,
	LeftN ! {sendy, CMi},
	RightN ! {sendy, CMi},
	logging(LogFile, lists:flatten(io_lib:format("sendy: ~p (~p); berechnet als neues Mi ~p. " ++ timeMilliSecond() ++ " ~n", [Y, MiNeu, CMi]))),
	ggtAlgorithmusDecision(Y, CMi);
ggtAlgorithmus(MiNeu, Y, _LeftN, _RightN, _Arbeitszeit, LogFile) ->
	logging(LogFile, lists:flatten(io_lib:format("sendy: ~p (~p); keine Berechnung~n", [Y, MiNeu]))),
	{notchanged, MiNeu}.

%Pruefung, ob der ggT berechnet wurde, damit Koordinator die Terminierung mitgeteilt werden kann
ggtAlgorithmusDecision(Y, CMi) when Y == CMi ->
	{terminated, CMi};
ggtAlgorithmusDecision(_Y, CMi) ->
	{changed, CMi}.
	
%Waehrend des Wartens auf die Nachbarn, koennen weitere Befehle seitens des Koordinators
%eintreffen. Wurden die Nachbarn geschickt, werden diese als ein 2-Tupel zurueck geliefert.
%Wurde "kill-Befehl" empfangen wird fuer die Nachbarn jeweils ein -1 zurueck
%gegeben, um zu signalisieren, dass der Prozess zu beenden ist
waitingForNeighbors(MeinName, LogFile) ->
	receive 
		{setneighbors, LeftN, RightN} ->
			logging(LogFile, lists:flatten(io_lib:format("Linker Nachbar ~p  gebunden.~n", [LeftN]))),
			logging(LogFile, lists:flatten(io_lib:format("Rechter Nachbar ~p gebunden.~n", [RightN]))),
			{LeftN, RightN};
		%moegliche Nachricht von Koordinator um aktuelles Mi zu erhalten
		{KoordinatorPID, tellmi} ->
			KoordinatorPID ! {mi, undef},
			waitingForNeighbors(MeinName, LogFile);
		%moegliches ping von Seiten des Koordinators
		{KoordinatorPID, pingGGT} ->
			KoordinatorPID ! {pongGGT, MeinName},
			waitingForNeighbors(MeinName, LogFile);
		kill ->
			{-1, -1}
	end.

%Beendigungsphase
%Beim Namensdienst abmelden, ggf. Timer abbrechen und lokal von Erlang-Node abmelden
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
