package client;

/*  Java.net fuer Network-Belange */
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;
/* Java.io fuer Input- Output-Belange. */
import java.io.*;

/**
 * Die Client-Klasse kümmert sich um die Kommunikation mit dem User und sendet
 * die Anfragen des Users an den Server (RequestHandler).
 * 
 * @author Fabian Reiber, Francis Opoku
 * 
 */
public class Client {

	private static String host;
	private static int port;
	private static InetAddress address;
	private static Socket connection;
	private static PrintWriter writer;
	private static Scanner reader;
	private static Scanner readFromUser;
	private static String result = "";

	/**
	 * Die createShutDownHook() Methode wird bei Beendigung des Clients
	 * aufgerufen. Hier kann eine "saubere" Beendigung des Programms
	 * (Client-Seitig) durchgeführt werden, indem der Socket geschlossen und
	 * eine Abschiedsmeldung erscheint. Die askUserForHostAndPort() macht genau
	 * das und erzeugt den Socket auf dem mit dem, dem Client zugehörigen,
	 * RequestHandler kommuniziert wird. Die establishConnection() Methode
	 * stellt eine Verbindung ist für die Kommunikation mit dem RequestHandler
	 * verantwortlich.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		createShutDownHook();
		askUserForHostAndPort();
		establishConnection();
	}

	/**
	 * Hier wird der Host und der Port erfragt, und ein Socket erzeugt der ein
	 * Kommunikationskanal zu dem host auf dem Port darstellt.
	 */
	public static void askUserForHostAndPort() {
		readFromUser = new Scanner(System.in, StandardCharsets.UTF_8.name());

		System.out.println("Enter the serveraddress: ");
		Client.host = readFromUser.nextLine().trim();

		System.out.println("Enter the port: ");

		if (readFromUser.hasNextInt()) {
			Client.port = Integer.parseInt(readFromUser.nextLine().trim());
		} else {
			System.out.println("Bitte geben sie einen gültigen "
					+ "positiven, ganzzahligen Wert als Port ein.");
			askUserForHostAndPort();
		}

		/* Adressobjekt des Servers erzeugen */
		try {
			address = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			System.out.println("Der Host " + host
					+ " ist nicht bekannt. Erneut versuchen:");
			askUserForHostAndPort();
		}
		/* Eine Socketconnection erzeugen */
		try {
			connection = new Socket(address, port);
		} catch (IOException e) {
			System.out.println("Fehler. Erneut versuchen: ");
			askUserForHostAndPort();
		}
	}

	/**
	 * Vom Benutzer wird eine Eingabeaufforderung verlangt, welche dann an den
	 * Server geschickt wird. Die Antwort wird in dieser Methode erhalten und
	 * dem User über syso ausgegeben. Das geht solange, solange die Antwort des
	 * Befehls nicht BYE oder OK_BYE war oder der Socket nicht geschlossen
	 * wurde.
	 */
	public static void establishConnection() {
		while (!connection.isClosed() && !result.equals("BYE")
				&& !result.equals("OK_BYE")) {
			try {

				reader = new Scanner(new InputStreamReader(
						connection.getInputStream(), StandardCharsets.UTF_8));

				writer = new PrintWriter(new OutputStreamWriter(
						connection.getOutputStream(), StandardCharsets.UTF_8),
						true);

				String command = getUserCommand();

				if (!writeToserver(writer, command)) {

					connection.close();
					System.out
							.println("Die Verbindung zum Server wurde unterbochen.");
				} else {
					try {
						result = reader.nextLine();
						System.out.println(result);
					} catch (NoSuchElementException e) {
						System.out
								.println("Der Server ist nicht mehr erreichbar.");
						System.out
								.println("Wende Sie sich an den Systemadministartor.");
						connection.close();
					}
				}

			} catch (IOException e) {
				System.out
						.println("IOException in Client.establishConnection()");
			}
		}
	}

	/**
	 * Einlesen der Benutzeranfrage
	 * 
	 * @return command Die Benutzeranfrage
	 */
	public static String getUserCommand() {
		System.out.println("Warte auf Eingabe...");
		readFromUser = new Scanner(System.in, StandardCharsets.UTF_8.name());
		String command = readFromUser.findInLine(".{0,255}"); // findInLine
		return command;
	}

	/**
	 * Benutzeranfrage zum RequestHandler schicken.
	 * 
	 * @param write
	 *            Der OutputStream des Sockets zum RequestHandler als
	 *            PrintWriter
	 * @param command
	 *            Die Benutzereingabe
	 * @return true wenn kein Error erfolgt ist, sonst false.
	 */
	public static boolean writeToserver(PrintWriter write, String command) {
		if (!write.checkError()) {
			write.write(command);
			writer.println();
			return true;
		}

		return false;
	}

	/**
	 * Bei Beendigung des Programms (auch bei strg-c bzw. ctrl-c) wird diese
	 * Methode aufgerufen. Der Socket wird geschlossen, die Namen der Autoren
	 * werden angezeigt.
	 */
	private static void createShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println();
				System.out
						.println("Autoren: Fabian Reiber, Francis Opoku, HAW Hamburg");
				System.out.println("Beende...");

				try {
					connection.close();
				} catch (IOException e) {
					System.out.println("...nicht sauber wg. IOException");
				}
			}
		}));
	}

}
