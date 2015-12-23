package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/* Simulation einer Kerberos-Session mit Zugriff auf einen Fileserver
 /* Server-Klasse
 */
import java.util.Date;

import kerberos.Auth;
import kerberos.KDC;
import kerberos.Ticket;

public class Server extends Object {

	private final long fiveMinutesInMillis = 300000; // 5 Minuten in
														// Millisekunden

	private String myName; // Konstruktor-Parameter
	private KDC myKDC; // wird bei KDC-Registrierung gespeichert
	private long myKey; // wird bei KDC-Registrierung gespeichert

	// Konstruktor
	public Server(String name) {
		myName = name;
	}

	public String getName() {
		return myName;
	}

	public void setupService(KDC kdc) {
		// Anmeldung des Servers beim KDC
		myKDC = kdc;
		myKey = myKDC.serverRegistration(myName);
		System.out.println(
				"Server " + myName + " erfolgreich registriert bei KDC " + myKDC.getName() + " mit ServerKey " + myKey);
	}

	public boolean requestService(Ticket srvTicket, Auth srvAuth, String command, String parameter) {
		boolean requestSucc = false;

		boolean srcTicketDecrypted = srvTicket.decrypt(myKey);
		// Sicherstellen, dass das Server Ticket auch tatsaechlich
		// verschluesselt war
		if (srcTicketDecrypted) {
			if (timeValid(srvTicket.getStartTime(), srvTicket.getEndTime())) {
				System.out.println(
						"Server: Server Ticket erfolgreich entschluesselt und Server Ticket ist nicht aelter als 5 Minuten!");
				boolean srvAuthDecrypted = srvAuth.decrypt(srvTicket.getSessionKey());

				// Sicherstellen, dass die Authentifikation auch tatsaechlich
				// verschluesselt war
				if (srvAuthDecrypted) {
					// Pruefung, ob Authentifikationszeit nicht aelter als 5 Min
					// ist
					if (timeFresh(srvAuth.getCurrentTime())) {
						System.out.println("Server: Authentifikation ist nicht aelter als 5 Minuten!");
						if (command.equals("showFile"))
							requestSucc = showFile(parameter);
					} else
						System.out.println("Die Zeit der Authentifikation ist nicht mehr fresh!");
				}
			} else
				System.out.println("Server Ticket ist bereits abgelaufen!");
		}

		return requestSucc;
	}

	/* *********** Services **************************** */

	private boolean showFile(String filePath) {
		/*
		 * Angegebene Datei auf der Konsole ausgeben. R�ckgabe: Status der
		 * Operation
		 */
		String lineBuf = null;
		File myFile = new File(filePath);
		boolean status = false;

		if (!myFile.exists()) {
			System.out.println("Datei " + filePath + " existiert nicht!");
		} else {
			try {
				// Datei �ffnen und zeilenweise lesen
				BufferedReader inFile = new BufferedReader(new InputStreamReader(new FileInputStream(myFile)));
				lineBuf = inFile.readLine();
				System.out.println("*******************ANFANG*******************");
				while (lineBuf != null) {
					System.out.println(lineBuf);
					lineBuf = inFile.readLine();
				}
				System.out.println("********************ENDE********************");
				inFile.close();
				status = true;
				System.out.println("Server: Datei vollstaendig ausgelesen!");
			} catch (IOException ex) {
				System.out.println("Fehler beim Lesen der Datei " + filePath + ex);
			}
		}
		return status;
	}

	/* *********** Hilfsmethoden **************************** */

	private boolean timeValid(long lowerBound, long upperBound) {
		/*
		 * Wenn die aktuelle Zeit innerhalb der �bergebenen Zeitgrenzen liegt,
		 * wird true zur�ckgegeben
		 */

		long currentTime = (new Date()).getTime(); // Anzahl mSek. seit 1.1.1970
		if (currentTime >= lowerBound && currentTime <= upperBound) {
			return true;
		} else {
			System.out.println(
					"-------- Time not valid: " + currentTime + " not in (" + lowerBound + "," + upperBound + ")!");
			return false;
		}
	}

	boolean timeFresh(long testTime) {
		/*
		 * Wenn die �bergebene Zeit nicht mehr als 5 Minuten von der aktuellen
		 * Zeit abweicht, wird true zur�ckgegeben
		 */
		long currentTime = (new Date()).getTime(); // Anzahl mSek. seit 1.1.1970
		if (Math.abs(currentTime - testTime) < fiveMinutesInMillis) {
			return true;
		} else {
			System.out.println("-------- Time not fresh: " + currentTime + " is current, " + testTime + " is old!");
			return false;
		}
	}
}
