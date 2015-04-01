package server;

/**
 * 
 * @author Fabian Reiber, Francis Opoku
 * 
 */

public class Server extends Thread {

	public static void main(String[] args) {
		// Parameter: String = Passwort, int = Maximale Anzahl Clients
		MainServer server = new MainServer("123", 15);
		server.start();
	}

}
