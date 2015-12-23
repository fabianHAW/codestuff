package kerberos;

/* Simulation einer Kerberos-Session mit Zugriff auf einen Fileserver
 /* KDC-Klasse
 */

import java.util.*;

public class KDC extends Object {

	private final long tenHoursInMillis = 36000000; // 10 Stunden in
	// Millisekunden

	private final long fiveMinutesInMillis = 300000; // 5 Minuten in
	// Millisekunden

	/* *********** Datenbank-Simulation **************************** */

	private String tgsName;

	private String user; // C

	private long userPasswordKey; // K(C)

	private String serverName; // S

	private long serverKey; // K(S)

	private long serverSessionKey; // K(C,S)

	private long tgsKey; // K(TGS)

	private long tgsSessionKey; // K(C,TGS)

	// Konstruktor
	public KDC(String name) {
		tgsName = name;
		// Eigenen Key f�r TGS erzeugen (streng geheim!!!)
		tgsKey = generateSimpleKey();
	}

	public String getName() {
		return tgsName;
	}

	/* *********** Initialisierungs-Methoden **************************** */

	public long serverRegistration(String sName) {
		/*
		 * Server in der Datenbank registrieren. R�ckgabe: ein neuer geheimer
		 * Schl�ssel f�r den Server
		 */
		serverName = sName;
		// Eigenen Key f�r Server erzeugen (streng geheim!!!)
		serverKey = generateSimpleKey();
		return serverKey;
	}

	public void userRegistration(String userName, char[] password) {
		/*
		 * User registrieren --> Eintrag des Usernamens in die Benutzerdatenbank
		 */
		user = userName;
		userPasswordKey = generateSimpleKeyForPassword(password);

		System.out.println("Principal: " + user);
		System.out.println("Password-Key: " + userPasswordKey);
	}

	/* *********** AS-Modul: TGS - Ticketanfrage **************************** */

	public TicketResponse requestTGSTicket(String userName, String tgsServerName, long nonce) {
		/*
		 * Anforderung eines TGS-Tickets bearbeiten. R�ckgabe: TicketResponse
		 * f�r die Anfrage
		 */

		TicketResponse tgsTicketResp = null;
		Ticket tgsTicket = null;
		long currentTime = 0;

		// TGS-Antwort zusammenbauen
		if (userName.equals(user) && // Usernamen und Userpasswort in der
		// Datenbank suchen!
		tgsServerName.equals(tgsName)) {
			// OK, neuen Session Key f�r Client und TGS generieren
			tgsSessionKey = generateSimpleKey();
			currentTime = (new Date()).getTime(); // Anzahl mSek. seit
			// 1.1.1970

			// Zuerst TGS-Ticket basteln ...
			tgsTicket = new Ticket(user, tgsName, currentTime, currentTime + tenHoursInMillis, tgsSessionKey);

			// ... dann verschl�sseln ...
			tgsTicket.encrypt(tgsKey);

			// ... dann Antwort erzeugen
			tgsTicketResp = new TicketResponse(tgsSessionKey, nonce, tgsTicket);

			// ... und verschl�sseln
			tgsTicketResp.encrypt(userPasswordKey);
		}
		return tgsTicketResp;
	}

	/*
	 * *********** TGS-Modul: Server - Ticketanfrage
	 * ****************************
	 */

	public TicketResponse requestServerTicket(Ticket tgsTicket, Auth tgsAuth, String serverName, long nonce) {
		TicketResponse ticketResp = null;
		boolean tgsEncrypted = tgsTicket.decrypt(tgsKey);

		// Sicherstellen, dass das TGS Ticket auch tatsaechlich
		// verschluesselt war
		if (tgsEncrypted) {
			if (timeValid(tgsTicket.getStartTime(), tgsTicket.getEndTime())) {
				System.out.println("KDC: TGS Ticket ist nicht aelter als 10 Std.!");
				boolean authEncrypted = tgsAuth.decrypt(tgsTicket.getSessionKey());

				// Sicherstellen, dass die Authentifikation auch tatsaechlich
				// verschluesselt war
				if (authEncrypted) {
					// Pruefung, ob Authentifikationszeit nicht aelter als 5 Min
					// ist und ob der User der ist, der sich vorher gemeldet hat
					if (timeFresh(tgsAuth.getCurrentTime()) && user.equals(tgsAuth.getClientName())) {
						System.out.println("KDC: Authentifikation des Clients ist nicht aelter als 5 Minuten!");
						long startTime = (new Date()).getTime();
						long endTime = startTime + fiveMinutesInMillis;

						// Server Ticket erzeugen
						Ticket serverTicket = new Ticket(user, serverName, startTime, endTime, serverSessionKey);
						System.out.println("KDC: Neues Server Ticket erzeugt!");
						
						long serverKey = getServerKey(serverName);
						if (serverKey != -1) {

							boolean serverTicketEncrypted = serverTicket.encrypt(serverKey);
							// Sicherstellen, dass das Server Ticket auch
							// tatsaechlich
							// verschluesselt wurde
							if (serverTicketEncrypted) {
								ticketResp = new TicketResponse(serverSessionKey, nonce, serverTicket);
								System.out.println("KDC: Ticket Response erzeugt!");

								// Sicherstellen, dass der Ticket Response auch
								// tatsaechlich
								// verschluesselt wurde
								boolean ticketRespEncrypted = ticketResp.encrypt(tgsSessionKey);
								if (!ticketRespEncrypted)
									ticketResp = null;
							}
						}

					} else
						System.out.println(
								"Die Zeit der Authentifikation ist nicht mehr fresh und/oder der Clientname ist nicht korrekt!");
				}
			} else
				System.out.println("TGS Ticket ist bereits abgelaufen!");
		}
		return ticketResp;
	}

	/* *********** Hilfsmethoden **************************** */

	private long getServerKey(String sName) {
		// Liefert den zugeh�rigen Serverkey f�r den Servernamen zur�ck
		// Wenn der Servername nicht bekannt, wird -1 zur�ckgegeben
		if (sName.equalsIgnoreCase(serverName)) {
			System.out.println("Serverkey ok");
			return serverKey;
		} else {
			System.out.println("Serverkey unbekannt!!!!");
			return -1;
		}
	}

	private long generateSimpleKeyForPassword(char[] pw) {
		// Liefert einen Schl�ssel f�r ein Passwort zur�ck, hier simuliert als
		// long-Wert
		long pwKey = 0;
		for (int i = 0; i < pw.length; i++) {
			pwKey = pwKey + pw[i];
		}
		return pwKey;
	}

	private long generateSimpleKey() {
		// Liefert einen neuen geheimen Schl�ssel, hier nur simuliert als
		// long-Wert
		long sKey = (long) (100000000 * Math.random());
		return sKey;
	}

	boolean timeValid(long lowerBound, long upperBound) {
		long currentTime = (new Date()).getTime(); // Anzahl mSek. seit
		// 1.1.1970
		if (currentTime >= lowerBound && currentTime <= upperBound) {
			return true;
		} else {
			System.out.println(
					"-------- Time not valid: " + currentTime + " not in (" + lowerBound + "," + upperBound + ")!");
			return false;
		}
	}

	boolean timeFresh(long testTime) {
		// Wenn die �bergebene Zeit nicht mehr als 5 Minuten von der aktuellen
		// Zeit abweicht,
		// wird true zur�ckgegeben
		long currentTime = (new Date()).getTime(); // Anzahl mSek. seit
		// 1.1.1970
		if (Math.abs(currentTime - testTime) < fiveMinutesInMillis) {
			return true;
		} else {
			System.out.println("-------- Time not fresh: " + currentTime + " is current, " + testTime + " is old!");
			return false;
		}
	}
}
