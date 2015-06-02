***********NameService***********
Der Nameservice kann über folgenden Befehl gestartet werden:

java nameservice/NameServiceMain <port>
*********************************

***********1. Test Applikation***********
Der Server kann über folgenden Befehl gestartet werden:

java test/MiddlewareTest 0 <nameservice-host> <nameservice-port>
Alternativ aus dem Ordner VSP3 heraus:
java -cp bin test.MiddlewareTest 0 <nameservice-host> <nameservice-port>

Der Server legt sich eine zeitlang schlafen und beendet sich dann erst, sodass die Konsole blockiert ist.

Der Client kann über folgenden Befehl gestartet werden:

java test/MiddlewareTest 1 <nameservice-host> <nameservice-port>
Alternativ aus dem Ordner VSP3 heraus:
java -cp bin test.MiddlewareTest 1 <nameservice-host> <nameservice-port>

****************************************

***********2. Test Applikation***********
Eine weitere Test-Anwendung kann lokal aus einer Konsole wie folgt gestartet werden:

java test/SmallConcurrencyTest <nameservice-port> <nameservice-host> <Anzahl Server> <Anzahl Clients>
Alternativ aus dem Ordner heraus VSP3:
java -cp bin test.SmallConcurrencyTest <nameservice-port> <nameservice-host> <Anzahl Server> <Anzahl Clients>

Auch hier legt sich der Server eine zeitlang schlafen und beendet sich dann erst, sodass die Konsole blockiert ist.

****************************************
