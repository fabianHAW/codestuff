package mware_lib;

/**
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
