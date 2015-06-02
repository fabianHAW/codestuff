package mware_lib;

/**
 * Verweise zum Entwurf:
 * <Entwurfsdokument> : Implementierung der vorgegebenen Methoden in Nr. 3 (d) - mware_lib.
 * <Klassendiagramm> : Implementierung durch vorgegebene Methoden in mware_lib - NameService (abastrakte Klasse)

 * 
 * @author Francis
 * 
 *         Stellt die Schnittstellen fuer die Stellvertreter-Objekte des
 *         Namensdienstes bereit
 */
public abstract class NameService {

	/**
	 * Verweis zum Entwurf
	 * <Sequenzdiagramm vsp3_sequ_server_start> 3.1
	 * 
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
