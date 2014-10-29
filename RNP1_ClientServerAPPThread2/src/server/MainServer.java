package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 
 * @author Fabian Reiber, Francis Opoku Die Klasse erzeugt einen Haupt-Thread,
 *         der auf einem Socket auf ankommende Clients wartet und für diese dann
 *         RequestHandler erzeugt, die die Anfragen der Clients bearbeiten.
 * 
 */
public class MainServer extends Thread {

	public ServerSocket socket;
	public String psw;
	private static int port;
	private static int maxClients;
	private static int countClients;
	private static Scanner adminReader;
	public boolean serverIsAlive = true;
	private static List<RequestHandler> activeRequestHandlers;

	/**
	 * 
	 * @param password
	 *            Passwort für den Shutdown-Befehl
	 * @param max
	 *            Maximale Anzahl an Clients die parallel mit dem Server
	 *            arbeiten dürfen.
	 */
	public MainServer(String password, int max) {
		psw = password;
		maxClients = max;
		countClients = 0;
		activeRequestHandlers = new ArrayList<RequestHandler>();
	}

	/**
	 * Aufforderung zur Angabe des Listenig-Ports für den MainServer
	 */
	public void configure() {
		System.out.println("Enter the port: ");
		adminReader = new Scanner(System.in, StandardCharsets.UTF_8.name());
		if (adminReader.hasNextInt()) {
			port = Integer.parseInt(adminReader.nextLine());
		} else {
			System.out.println("Bitte geben sie einen gültigen Port (int Wert) ein.");
			configure();
		}
	}

	/**
	 * Warten auf Socket auf ankommende Clients. Dann Socket an RequestHandler
	 * übergeben damit dieser Mit dem Client kommunizieren kann. Abschließend
	 * RequestHandler-Thread starten.
	 */
	public void waitForRequests() {
		try {
			socket = new ServerSocket(port);
			while (serverIsAlive) {
				if (countClients <= maxClients) {
					Socket connection = socket.accept();
					RequestHandler server = new RequestHandler(connection, ""
							+ ++countClients, this);
					storeRequestHandler(server);

					server.start();
				}
			}
		} catch (IOException e) {
			if (!serverIsAlive) {
				System.out.println("OK_BYE");
			} else {
				System.out
						.println("IOException in MainServer.waitForRequests()");
			}
		}
	}

	/**
	 * Erst Server konfigurieren (Listening-Port von Server-Admin erfragen),
	 * dann auf ankommende Clients warten.
	 */
	public void run() {
		configure();
		waitForRequests();
	}

	/**
	 * Aktive RequestHandler in Liste verwalten um Referenzen auf diese zu
	 * halten, damit auch eine Kommunikation zu diesen gewährleistet ist.
	 * 
	 * @param server
	 *            Der RequestHandler der mit einem Client kommuniziert.
	 */
	private void storeRequestHandler(RequestHandler server) {
		activeRequestHandlers.add(server);
	}

	/**
	 * Wird von einer RequestHandler-Instanz in run() aufgerufen bei BYE- oder
	 * SHUTDOWN-Befehl. Dann wird der Socket auf dem der RequestHandler mit dem
	 * Client kommuniziert geschlossen und der RequestHandler terminiert
	 * (erreicht ende der run-Methode), weshalb er sich aus der Liste der
	 * verwalteten RequestHandler "austragen" muss.
	 * 
	 * @param r
	 */
	public static void deleteMe(RequestHandler r) {
		activeRequestHandlers.remove(r);
		countClients = countClients - 1;

	}

}
