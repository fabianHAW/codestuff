package server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Klasse zur Kommunikation mit Clients. Bearbeitet die Anfragen der Clients.
 * 
 * @author Fabian Reiber, Francis Opoku
 * 
 */
public class RequestHandler extends Thread {

	private Socket connection;
	String name;
	MainServer mainServer;

	/**
	 * Ein RequestHandler wird vom MainServer in waitForRequests() erzeugt. Eine
	 * Referenz auf den MainServer wird benötigt, um beim Shutdown-Befehl das
	 * isAlive-Flag des MainServers auf false zu setzen und den Socket des
	 * MainServers zu schließen, auf dem er auf ankommende Clients wartet. Da
	 * der Shutdown-Befehl den Socket schließt, wird eine Exception geschmissen.
	 * Das isAliveFlag wird im catch-Block abgefragt. Dadurch erhält man beim
	 * schmeißen der Exception die Möglichkeit der Unterscheidung zwischen
	 * tatsächlichem Fehler des Programms und gewollter Schließung des Sockets.
	 * 
	 * @param c
	 *            Der Socket auf dem der MainServer einen Client akzeptiert hat.
	 * @param name
	 *            Die Nr. des RequestHandlers in der Reihenfolge der erzeugten
	 *            RequestHandler.
	 * @param main
	 *            Eine Referenz auf den Mainserver.
	 */
	public RequestHandler(Socket c, String name, MainServer main) {
		this.connection = c;
		this.name = name;
		this.mainServer = main;
	}

	/**
	 * Die Kommunikationsreihenfolge ist folgende: Solange der Socket auf dem
	 * der RequestHandler mit dem Client kommuniziert nicht geschlossen ist 1.
	 * Wartet er auf dem Socket auf eine ankommende Zeile von maximal 256
	 * Zeichen. 2. Übergibt diese der Methode handleClientRequest die die
	 * Eingabezeichenkette analysiert. 3. Löscht sich aus der Liste der von
	 * MainServer verwalteten RequestHandler.
	 */
	public void run() {
		while (!connection.isClosed()) {
			try {

				Scanner readFromClient = new Scanner(
						connection.getInputStream(),
						StandardCharsets.UTF_8.name());
				PrintWriter writeToClient = new PrintWriter(
						new OutputStreamWriter(connection.getOutputStream(),
								StandardCharsets.UTF_8), true);

				String clientRequest = readFromClient.findInLine(".{0,255}");
				handleClientRequest(clientRequest, writeToClient);
				//readFromClient.close();
			} catch (Exception e) {
				try {
					connection.close();
				} catch (IOException e1) {
					System.out.println("RequestHandler: connection not closed");
				}
			}

		}

		MainServer.deleteMe(this);

		try {
			connection.close();
		} catch (IOException e) {
			System.out
					.println("Fehler: RequestHandler.run(): connection.close");
			e.printStackTrace();
		}
	}

	/**
	 * Der Befehl wird von der Eingabezeichenfolge des Clients separiert. Er
	 * wird Analysiert und entsprechend seiner Gültigkeit dann auf der
	 * restlichen Zeichenkette durch einen entsprechenden Methodenaufruf
	 * ausgeführt. Falls es kein gültiger Befehl ist, wird der Client darüber
	 * informiert.
	 * 
	 * @param clientRequest
	 *            Eingabezeichenfolge des Clients.
	 * @param writeToClient
	 *            Der OutputStream des Sockets als PrintWriter um das Ergebnis
	 *            zum Cliet zu schreiben.
	 */
	public void handleClientRequest(String clientRequest,
			PrintWriter writeToClient) {

		String userNeed = clientRequest;
		String[] inputString = userNeed.split(" ", 2);
		String result = "";
		String command = inputString[0];

		if (!command.equals("BYE") && inputString.length == 1) {
			result = "Fehler: " + command + " ohne nachfolgende "
					+ " Zeichenkette ungültig.";
			writeToClient.println(result);
		} else {
			if (command.equals(Commands.UPPERCASE.toString())) {
				for (int i = 1; i < inputString.length; i++) {
					result = "OK ";
					result += getUppercase(inputString[i]);
				}
				writeToClient.println(result);
			} else if (command.equals(Commands.LOWERCASE.toString())) {
				for (int i = 1; i < inputString.length; i++) {
					result = "OK ";
					result += getLowercase(inputString[i]);
				}
				writeToClient.println(result);
			} else if (command.equals(Commands.REVERSE.toString())) {
				for (int i = 1; i < inputString.length; i++) {
					result = "OK ";
					result += getReverse(inputString[i]);
				}
				writeToClient.println(result);
			} else if (command.equals(Commands.BYE.toString())) {
				try {
					result = "BYE";
					writeToClient.println(result);
					connection.close();
				} catch (IOException e) {
					System.out
							.println("Fehler: RequestHandler.handleClientRequest(...): else-if(...(Commands.BYE...))-CatchBlock");
					e.printStackTrace();
				}
			} else if (command.equals(Commands.SHUTDOWN.toString())) {
				try {
					result = doShutdown(inputString[1].toString());
					writeToClient.println(result);
					connection.close();
				} catch (IOException e) {
					System.out
							.println("Fehler: RequestHandler.handleClientRequest(...): else-if(...(Commands.SHUTDOWN...))-CatchBlock");
					e.printStackTrace();
				}
			} else {
				result = "Fehler: " + inputString[0]
						+ " ist kein gültiger Befehl.";
				writeToClient.println(result);
			}
		}
	}

	/**
	 * Bei gültigen Passwort: serverIsAlive vom MainServer auf false setzen.
	 * Socket auf dem der MainServer auf ankommende Clients wartet schließen.
	 * Dadurch wird eine Exception geschmissen, die als gewollt erkannt wird,
	 * indem im catch-Block des MainServers das serverIsAlive-Flag gelesen wird.
	 * Ist es false, weiß man, das der Shutdown-Befehl den Socket geschlossen
	 * hat.
	 * 
	 * @param params
	 *            Das Passwort
	 * @return OK_BYE wenn das Passwort richtig war, "Wrong password" sonst.
	 * @throws IOException
	 */
	private String doShutdown(String params) throws IOException {
		if (params.equals(mainServer.psw)) {
			mainServer.serverIsAlive = false;
			mainServer.socket.close();
			return "OK_BYE";
		}
		return "Wrong password";
	}

	/**
	 * Schreibt jedes Wort - aber nicht die Position der Wörter im Satz - in
	 * umgekehrte Reihenfolge um.
	 * 
	 * @param params
	 *            Die zu manipulierende Zeicehnkette.
	 * @return Die manimulierte Zeichenkette.
	 */
	private String getReverse(String params) {
		char[] original = params.toCharArray();
		// char[] reverted = new char[original.length];
		String reverted = "";

		for (int i = original.length - 1; i >= 0; i--) {
			reverted += original[i];
		}

		return reverted;
	}

	/**
	 * Erzeugt aus jedem groß geschriebenen Zeichen des Gegenteil.
	 * 
	 * @param params
	 *            Die zu manipulierende Zeichenkette.
	 * @return Die manipulierte Zeichenkette.
	 */
	private static String getLowercase(String params) {
		return params.toLowerCase();
	}

	/**
	 * Erzeugt aus jedem klein geschriebenen Zeichen das Gegenteil.
	 * 
	 * @param params
	 *            Die zu manipulierende Zeichenkette.
	 * @return Die manipulierte Zeichenkette.
	 */
	private static String getUppercase(String params) {
		return params.toUpperCase();
	}

	/**
	 * Die akzeptierten Kommandos
	 * 
	 * @author Fabian Reiber, Francis Opoku
	 * 
	 */
	public enum Commands {
		UPPERCASE, LOWERCASE, REVERSE, BYE, SHUTDOWN
	}

	/**
	 * VERALTET! Wird nicht benutzt. Methode um von außen den Socket zum Client
	 * zwangsweise zu trennen.
	 */
	public void disconnect() {
		try {
			connection.close();
		} catch (IOException e) {
			System.out.println("RequestHandler " + name
					+ " konnte nicht disconnected werden.");
		}
	}

	/**
	 * VERALTET! Wird nicht benutzt.
	 * 
	 * @return
	 */
	public boolean isConnected() {
		return connection.isConnected();
	}
}
