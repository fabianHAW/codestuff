package mware_lib;

import nameservice.NameService;
import nameservice.NameServiceImpl;

public class ObjectBroker { // - Front-End der Middleware -

	private NameService nameservice;

	// Das hier zurückgelieferte Objekt soll der zentrale Einstiegspunkt
	// der Middleware aus Applikationssicht sein.
	// Parameter: Host und Port, bei dem die Dienste (hier: Namensdienst)
	// kontaktiert werden sollen. Mit debug sollen Testausgaben der
	// Middleware ein- oder ausgeschaltet werden können.
	public static ObjectBroker init(String serviceHost, int listenPort,
			boolean debug) {
		return null;
	}

	// Liefert den Namensdienst (Stellvetreterobjekt).
	public NameService getNameService() {
		if(this.nameservice == null)
			return new NameServiceImpl();
		
		return this.nameservice;
	}

	// Beendet die Benutzung der Middleware in dieser Anwendung.
	public void shutDown() {

	}
}
