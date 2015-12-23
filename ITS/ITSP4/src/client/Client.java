package client;

import java.util.Date;

import kerberos.Auth;
import kerberos.KDC;
import kerberos.Ticket;
import kerberos.TicketResponse;
import server.Server;

public class Client extends Object {

	private KDC myKDC; // Konstruktor-Parameter

	private String currentUser; // Speicherung bei Login n�tig
	private Ticket tgsTicket = null; // Speicherung bei Login n�tig
	private long tgsSessionKey; // K(C,TGS) // Speicherung bei Login n�tig

	String command = "showFile";

	// Konstruktor
	public Client(KDC kdc) {
		myKDC = kdc;
	}

	public boolean login(String userName, char[] password) {
		boolean loginSucc = false;

		currentUser = userName;
		long nonce1 = generateNonce();
		long userKey = generateSimpleKeyFromPassword(password);

		// TGS Ticket anfordern
		TicketResponse ticketResp = myKDC.requestTGSTicket(currentUser, myKDC.getName(), nonce1);

		if (ticketResp != null) {
			boolean ticketRespDecrypted = ticketResp.decrypt(userKey);
			if (ticketRespDecrypted) {
				// Mitgeschickter nonce und erhaltener nonce sollten gleich
				// sein.
				System.out.println("Client: TGS Ticket erhalten und erfolgreich entschluesselt!");
				if (ticketResp.getNonce() == nonce1) {

					// TGS Ticket sichern
					tgsTicket = ticketResp.getResponseTicket();
					System.out.println("Client: TGS Ticket gesichert!");

					// TGS Sessionkey sichern
					tgsSessionKey = ticketResp.getSessionKey();
					System.out.println("Client: TGS SessionKey gesichert!");

					loginSucc = true;
				} else
					System.out.println("Nonce ist nicht korrekt!");
			}
		}

		return loginSucc;
	}

	public boolean showFile(Server fileServer, String filePath) {
		boolean showFileSucc = false;
		TicketResponse ticketResp = null;
		long nonce2 = 0;

		long currentTimeTgs = (new Date()).getTime();
		Auth tgsAuth = new Auth(currentUser, currentTimeTgs);

		boolean tgsAuthEncrypted = tgsAuth.encrypt(tgsSessionKey);

		// Sicherstellen, dass die Authentifikation auch tatsaechlich
		// verschluesselt wurde
		if (tgsAuthEncrypted) {
			nonce2 = generateNonce();
			// Server Ticket anfordern
			System.out.println("Client: Authentifikation verschluesselt und Server Ticket anfordern!");
			ticketResp = myKDC.requestServerTicket(tgsTicket, tgsAuth, fileServer.getName(), nonce2);
		}

		if (ticketResp != null) {
			boolean ticketRespDecrypted = ticketResp.decrypt(tgsSessionKey);
			if (ticketRespDecrypted) {

				System.out.println("Client: Ticket Response für Server Ticket erhalten und erfolgreich entschluesselt!");
				// Mitgeschickter nonce und erhaltener nonce sollten gleich
				// sein.
				if (ticketResp.getNonce() == nonce2) {
					long serverSessionKey = ticketResp.getSessionKey();

					// Server Ticket sichern
					Ticket srvTicket = ticketResp.getResponseTicket();
					System.out.println("Client: Server Ticket gesichert!");

					System.out.println("Client: Server Request wird vorbereitet...");
					long currentTimeSrv = (new Date()).getTime();
					Auth srvAuth = new Auth(currentUser, currentTimeSrv);

					boolean srvAuthEncrypted = srvAuth.encrypt(serverSessionKey);
					// Sicherstellen, dass die Authentifikation auch
					// tatsaechlich
					// verschluesselt wurde
					if (srvAuthEncrypted) {
						// Anfrage an Server senden
						System.out.println("Client: Authentifikation verschluesselt und Request an Server senden!");
						showFileSucc = fileServer.requestService(srvTicket, srvAuth, command, filePath);
					}

				} else
					System.out.println("Nonce ist nicht korrekt!");
			}
		}

		return showFileSucc;
	}

	/* *********** Hilfsmethoden **************************** */

	private long generateSimpleKeyFromPassword(char[] passwd) {
		// Liefert einen eindeutig aus dem Passwort abgeleiteten Schl�ssel
		// zur�ck, hier simuliert als long-Wert
		long pwKey = 0;
		if (passwd != null) {
			for (int i = 0; i < passwd.length; i++) {
				pwKey = pwKey + passwd[i];
			}
		}
		return pwKey;
	}

	private long generateNonce() {
		// Liefert einen neuen Zufallswert
		long rand = (long) (100000000 * Math.random());
		return rand;
	}
}
