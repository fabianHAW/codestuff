package mware_lib;

/**
 *  *  Verweise zum Entwurf:
 * <Entwurfsdokument> : Implementierung der vorgegebenen Methoden in Nr. 3 (d) - mware_lib.
 * <Klassendiagramm> : Implementierung durch vorgegebene Methoden in mware_lib - Skeleton Interface

 * 
 * @author Francis
 * 
 *         Als Thread implementiert, damit mehrere Anfragen des gleichen
 *         Objektes durchgeführt werden können. Durch die entfernte
 *         Objektreferenz in der empfangenen Nachricht, kann durch das
 *         Referenzmodul auf das lokale Objekt geschlossen werden.
 */

public interface Skeleton {

	/**
	 * Fuehrt ein unmarshaling des Requests durch
	 * 
	 * @return neu erzeugte MessageADT
	 */
	public MessageADT invoke();

}
