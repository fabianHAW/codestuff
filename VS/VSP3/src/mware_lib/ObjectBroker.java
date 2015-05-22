package mware_lib;

public class ObjectBroker { // - Front-End der Middleware -

	private CommunicationModule comModule;
	private NameService nameservice;
	private String serviceHost;
	private int listenPort;
	public static boolean DEBUG;

	private ObjectBroker(String h, int p, boolean d) {
		this.comModule = new CommunicationModule();
		this.comModule.waitingForMessages();
		this.serviceHost = h;
		this.listenPort = p;
		DEBUG = d;
	}

	// Das hier zurückgelieferte Objekt soll der zentrale Einstiegspunkt
	// der Middleware aus Applikationssicht sein.
	// Parameter: Host und Port, bei dem die Dienste (hier: Namensdienst)
	// kontaktiert werden sollen. Mit debug sollen Testausgaben der
	// Middleware ein- oder ausgeschaltet werden können.
	public static ObjectBroker init(String serviceHost, int listenPort,
			boolean debug) {
		return new ObjectBroker(serviceHost, listenPort, debug);
	}

	// Liefert den Namensdienst (Stellvetreterobjekt).
	public NameService getNameService() {
		if (this.nameservice == null) {
			this.nameservice = new NameServiceProxy(this.serviceHost,
					this.listenPort);
		}
		return this.nameservice;
	}

	// Beendet die Benutzung der Middleware in dieser Anwendung.
	public void shutDown() {
		this.comModule.setAlive(false);
	}
}
