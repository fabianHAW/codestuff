package kerberos;

/* Simulation einer Kerberos-Session mit Zugriff auf einen Fileserver
 /* Auth-Klasse (Authentification)
 */

import java.util.*;

public class Auth extends Object {

	private String myClientName; // Konstruktor-Parameter

	private long myCurrentTime; // Konstruktor-Parameter

	// Geheimer Schl�ssel, mit dem die Authentifikation (simuliert)
	// verschl�sselt ist:
	private long myAuthKey;

	private boolean isEncryptedState; // Aktueller Zustand des Objekts

	// Kalenderobjekt zur Zeitumrechnung (f�r Testausgaben)
	private Calendar cal;

	// Konstruktor
	public Auth(String clientName, long currentTime) {
		myClientName = clientName;
		myCurrentTime = currentTime;
		myAuthKey = -1;
		isEncryptedState = false;
		cal = new GregorianCalendar(); // f�r Testausgaben
	}

	public String getClientName() {
		if (isEncryptedState) {
			printError("Zugriff auf verschl�sselte Authentifikation (getClientName)");
		}
		return myClientName;
	}

	public long getCurrentTime() {
		if (isEncryptedState) {
			printError("Zugriff auf verschl�sselte Authentifikation (getCurrentTime)");
		}
		return myCurrentTime;
	}

	public boolean encrypt(long key) {
		// Authentifikation mit dem Key verschl�sseln.
		// Falls die Authentifikation bereits verschl�sselt ist, wird false
		// zur�ckgegeben.
		boolean encOK = false;
		if (isEncryptedState) {
			printError("Auth ist bereits verschl�sselt");
		} else {
			myAuthKey = key;
			isEncryptedState = true;
			encOK = true;
		}
		return encOK;
	}

	public boolean decrypt(long key) {
		// Authentifikation mit dem Key entschl�sseln.
		// Falls der Key falsch ist oder
		// falls die Authentifikation bereits entschl�sselt ist, wird false
		// zur�ckgegeben.
		boolean decOK = false;
		if (!isEncryptedState) {
			printError("Auth ist bereits entschl�sselt");
		}
		if (myAuthKey != key) {
			printError("Auth-Entschl�sselung mit key " + key
					+ " ist fehlgeschlagen");
		} else {
			isEncryptedState = false;
			decOK = true;
		}
		return decOK;
	}

	public boolean isEncrypted() {
		// Aktuellen Zustand zur�ckgeben:
		// verschl�sselt (true) / entschl�sselt (false)
		return isEncryptedState;
	}

	public void printError(String message) {
		System.out.println("+++++++++++++++++++");
		System.out.println("+++++++++++++++++++ Fehler +++++++++++++++++++ "
				+ message + "! Auth-Key: " + myAuthKey);
		System.out.println("+++++++++++++++++++");
	}

	public void print() {
		System.out.println("********* Authentifikation f�r " + myClientName
				+ " *******");
		System.out.println("CurrentTime: " + getDateString(myCurrentTime));
		System.out.println("Auth Key: " + myAuthKey);
		if (isEncryptedState) {
			System.out.println("Auth-Zustand: verschl�sselt (encrypted)!");
		} else {
			System.out.println("Auth-Zustand: entschl�sselt (decrypted)!");
		}
		System.out.println();
	}

	private String getDateString(long time) {
		// Umrechnung der Zeitangabe time (Millisek. seit 1.1.1970) in einen
		// Datumsstring
		String dateString;

		cal.setTimeInMillis(time);
		dateString = cal.get(Calendar.DAY_OF_MONTH) + "."
				+ (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.YEAR)
				+ " " + cal.get(Calendar.HOUR_OF_DAY) + ":"
				+ cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND)
				+ ":" + cal.get(Calendar.MILLISECOND);
		return dateString;
	}
}
