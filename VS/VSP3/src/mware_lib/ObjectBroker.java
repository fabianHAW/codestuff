package mware_lib;

public class ObjectBroker { // - Front-End der Middleware -

	private CommunicationModule comModule;
	private NameService nameservice;
	private String serviceHost;
	private int listenPort;
	public static boolean DEBUG;
	private static ObjectBroker broker = null;

	private ObjectBroker(String h, int p, boolean d) {
		this.comModule = new CommunicationModule();
		this.comModule.start();
		this.serviceHost = h;
		this.listenPort = p;
		this.nameservice = null;
		DEBUG = d;
	}

	// Das hier zurückgelieferte Objekt soll der zentrale Einstiegspunkt
	// der Middleware aus Applikationssicht sein.
	// Parameter: Host und Port, bei dem die Dienste (hier: Namensdienst)
	// kontaktiert werden sollen. Mit debug sollen Testausgaben der
	// Middleware ein- oder ausgeschaltet werden können.
	public static ObjectBroker init(String serviceHost, int listenPort,
			boolean debug) {
		if(broker == null){
			CommunicationModule.debugPrint("mware_lib.ObjectBroker: initialized");
			return new ObjectBroker(serviceHost, listenPort, debug);
		}else{
			CommunicationModule.debugPrint("mware_lib.ObjectBroker: returned Singleton Objectbroker");
			return broker;
		}
	}

	// Liefert den Namensdienst (Stellvetreterobjekt).
	public NameService getNameService() {
		if (this.nameservice == null) {
			CommunicationModule
					.debugPrint("mware_lib.ObjectBroker: new NameService initialized and returned");
			this.nameservice = new NameServiceProxy(this.serviceHost,
					this.listenPort);
			return this.nameservice;
		}
		CommunicationModule
				.debugPrint("mware_lib.ObjectBroker: NameService just initialized and returned");
		return this.nameservice;
	}

	// Beendet die Benutzung der Middleware in dieser Anwendung.
	public void shutDown() {
		CommunicationModule.debugPrint("mware_lib.ObjectBroker: shutDown");
		this.comModule.setAlive(false);
	}
}
