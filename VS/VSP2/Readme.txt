Starten des Namensdienstes:
--------------------------
(w)erl -(s)name ns -setcookie kekse
1>nameservice:start( ).

ns_send:msg(State,NameServiceNode)
- State: einen der Nachrichten help, reset, list oder kill
- NameServiceNode: Name des Nodes, auf dem der Namensdienst gestartet wurde
% global:registered_names() zum nachschauen

Starten des Koordinators:
--------------------------
(w)erl -(s)name chef -setcookie kekse
2>Koordinator = koordinator:init().

Startet Koordinator und wartet auf Eingaben des Benutzers. Moegliche Eingabe:
Koordinator ! State
- State: eine der Nachrichten step, prompt, nudge, {calc, WggT}, toggle reset oder kill

% liest die koordinator.cfg ein:
% {arbeitszeit, 3}:	 	 simulierte Arbeitszeit fuer die ggT-Berechnung
% {termzeit, 3}:		 Wenn termzeit keine Berechnung dann wird Terminierungsabstimmung initiiert
% {ggtprozessnummer, 42}:	 Anzahl ggT Prozesse je Starter (und default ggT)
% {nameservicenode, 'ns@lab23'}: node des Namensdienstes
% {nameservicename, nameservice} Name des Namensdienstes
% {koordinatorname, chef}:	 Name des Koordinators
% {quote, 80}:			 Abstimmungsquote fuer Terminierung in Prozent
% {korrigieren, 0}:		 Korigiert falsche Terminierungsmeldungen (== 1)

Starten der ggT-Prozesse:
--------------------------
Wichtig ist, dass in dem Ordner, wo die erl-Dateien liegen auch ein Ordner 'ggt_log' angelegt wird.
In diesem Ordner werden die Log-Dateien der ggT-Prozesse abgelegt. Ist dieser Ordner nicht vorhanden, 
wird eine Exception geworfen.

(w)erl -(s)name starter -setcookie kekse
3>starter:start(Starternummer)

- interner aufruf von ggt:start(ArbeitsZeit, TermZeit, ProzessNummer, StarterNummer, PraktikumsGruppe, TeamNummer, Nameservice, Koordinatorname, Quota)

% starter liest die ggt.cfg ein:
% {praktikumsgruppe, 3}:	Nummer des Praktikums
% {teamnummer, 1}:		Nummer des Teams
% {nameservicenode, ns@lab23}	node des Namensdienstes
% {koordinatorname, chef}:	Name des Koordinators

Runterfahren:
-------------
2> Ctrl/Strg Shift G
-->q
