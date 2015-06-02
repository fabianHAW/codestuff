package mware_lib;

/**
 * Verweis zum Entwurf:
 * <Klassendiagramm> : Implementierung durch vorgegebene Methoden in mware_lib - ObjectBroker
 * <Entwurfsdokument> : Implementierung durch Vorgabe der in 3d definierten Methoden.
 * <Sequenzdiagramm vsp3_sequ_server> : Realiserung der Sequenznummer 2, 3, 3.1, 3.2
 * <Sequenzdiagramm vsp3_client_request> : Realiserung der Sequenznummer 1, 2, 2.1, 3
 * 
 * @author Fabian
 * 
 *         Zentraler Einstiegspunkt der Middleware fuer Client- und Server-Seite
 */
public class ObjectBroker { // - Front-End der Middleware -

	private CommunicationModule comModule;
	private NameService nameservice;
	private String serviceHost;
	private int listenPort;
	public static boolean DEBUG;
	private static ObjectBroker broker = null;

	/**
	 * @param h
	 * @param p
	 * @param d
	 */
	private ObjectBroker(String h, int p, boolean d) {
		this.comModule = new CommunicationModule();
		this.comModule.start();
		this.serviceHost = h;
		this.listenPort = p;
		this.nameservice = null;
		DEBUG = d;
	}

	/**
	 * Verweis zum Entwurf
	 * <Sequenzdiagramm vsp3_client_request> : Realiserung der Sequenznummern 1
	 * <Sequenzdiagramm vsp3_sequ_server> : Realiserung der Sequenznummern 2
	 * 
	 * Das hier zurückgelieferte Objekt soll der zentrale Einstiegspunkt der
	 * Middleware aus Applikationssicht sein. Parameter: Host und Port, bei dem
	 * die Dienste (hier: Namensdienst) kontaktiert werden sollen. Mit debug
	 * sollen Testausgaben der Middleware ein- oder ausgeschaltet werden können.
	 * 
	 * @param serviceHost
	 *            Host des Namensdienstes
	 * @param listenPort
	 *            Port auf dem der Namensdienst lauscht
	 * @param debug
	 *            Debug-Flag fuer Ausgaben
	 * @return neues ObjektBroker-Objekt
	 */
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

	/**
	 * 	Verweis zum Entwurf
	 * <Sequenzdiagramm vsp3_client_request> : Realiserung der Sequenznummern 2
	 * <Sequenzdiagramm vsp3_sequ_server> : Realiserung der Sequenznummern 3
	 * 
	 * Liefert den Namensdienst (Stellvetreterobjekt).
	 * 
	 * @return neues oder ein bereits vorhandenes Stellvertreter-Objekt des
	 *         Namensdienstes
	 */
	public NameService getNameService() {
		/* 
		* 	Verweis zum Entwurf
		 * <Sequenzdiagramm vsp3_client_request> : Realiserung der Sequenznummern 2.1
		 * <Sequenzdiagramm vsp3_sequ_server> : Realiserung der Sequenznummern 3.1
		 * */
		if (this.nameservice == null) {
			CommunicationModule
					.debugPrint("mware_lib.ObjectBroker: new NameService initialized and returned");
			this.nameservice = new NameServiceProxy(this.serviceHost,
					this.listenPort);
			return this.nameservice;
		}
		CommunicationModule
				.debugPrint("mware_lib.ObjectBroker: NameService just initialized and returned");
		/* 
		* 	Verweis zum Entwurf
		 * <Sequenzdiagramm vsp3_client_request> : Realiserung der Sequenznummern 3
		 * <Sequenzdiagramm vsp3_sequ_server> : Realiserung der Sequenznummern 3.2
		 * */
		return this.nameservice;
	}

	/**
	 * Beendet die Benutzung der Middleware in dieser Anwendung.
	 */
	public void shutDown() {
		CommunicationModule.debugPrint("mware_lib.ObjectBroker: shutDown");
		this.comModule.communicationModuleShutdown();
	}
}
