package nameservice;

/**
 * Zwecks Debug-Ausgaben importiert.
 */
import mware_lib.ObjectBroker;

/**
 * 
 * @author Fabian
 * Gibt Debug Ausgaben aus
 */

public class DebugPrinter extends Thread {

	public DebugPrinter() {
		
	}

	
	public static void debugPrint(String text) {
		if (ObjectBroker.DEBUG) {
			System.out.println(text);
		}
	}

	/**
	 * Erzeugt Debug-Ausgaben auf der Konsole, sofern Debug-Flag auf true
	 * 
	 * @param klasse
	 *            die Klasse die diese Methode aufruft
	 * @param text
	 *            Nachricht die ausgegeben werden soll
	 */
	public static void debugPrint(Class<?> klasse, String text) {
		if (ObjectBroker.DEBUG) {
			System.out.println(klasse.getName() + ": " + text);
		}
	}
}
