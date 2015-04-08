--------------------
Compilieren der Dateien:
--------------------
Zu dem Paket gehören die Dateien
client.erl; lifetimeTimer.erl; messageSendTimer.erl cmem.erl; dlq.erl; hbq.erl; server.erl; werkzeug.erl;

sowie:
readme.txt; client.cfg; server.cfg

(w)erl -(s)name server -setcookie kekse
1> make:all().

--------------------
Starten des HBQ-Prozesses (muss vor dem Starten des Servers gestartet werden):
--------------------
(w)erl -(s)name hbqNode -setcookie kekse
1> hbq:start().

--------------------
Starten des Servers:
--------------------
(w)erl -(s)name server -setcookie kekse
1> lifetimeTimer:createServer().

% in der server.cfg:
% {latency, 42}. Zeit in Sekunden, die der Server bei Leerlauf wartet, bevor er sich beendet
% {clientlifetime,4}. Zeitspanne, in der sich an den Client erinnert wird
% {servername, wk}. Name des Servers als Atom
% {hbqname, hbq}. Name der HBQ als Atom
% {hbqnode, 'hbqNode@KI-VS'}. Name der Node der HBQ als Atom
% {dlqlimit, 15}. Größe der DLQ

Starten des Clients:
--------------------
(w)erl -(s)name client -setcookie kekse
1> lifetimeTimer:createClient().

% 'server@lab33.cpt.haw-hamburg.de': Name der Server Node, erhält man zB über node()
% ' wegen dem - bei haw-hamburg, da dies sonst als minus interpretiert wird.
% in der client.cfg:
% {clients, 3}.  Anzahl der Clients, die gestartet werden sollen
% {lifetime, 42}. Laufzeit der Clients
% {servername, wk}. Name des Servers
% {servernode, 'server@KI-VS'}. Node des Servers
% {sendeintervall, 3}. Zeitabstand der einzelnen Nachrichten

Runterfahren:
-------------
2> Ctrl/Strg Shift G
-->q

Informationen zu Prozessen:
-------------
2> pman:start().
2> process_info(PID).
