package mware_lib;

/**
 * 
 * @author Francis
 * 
 *         Stellt die Schnittstellen fuer die Stellvertreter-Objekte des
 *         Namensdienstes bereit
 */
public abstract class NameService {

	/**
	 * Meldet einen neuen Servant beim Namensdienst an
	 * 
	 * @param servant
	 *            entfernte Objekt-Referenz des Servants
	 * @param name
	 *            Name unter der der Servant zu finden ist
	 */
	public abstract void rebind(Object servant, String name);

	/**
	 * Loest einen Servant anhand des Namens auf
	 * 
	 * @param name
	 *            Name des Servants der angefordert wird
	 * @return entfernte Objekt-Referenz des Servants
	 */
	public abstract Object resolve(String name);

}
