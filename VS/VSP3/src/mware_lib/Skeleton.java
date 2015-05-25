package mware_lib;

/**
 * 
 * @author Francis
 * 
 *         Stellt die Schnittstellen des Skeletons auf der Server-Seite bereit
 */

public interface Skeleton {

	/**
	 * Fuehrt ein unmarshaling des Requests durch
	 * 
	 * @return neu erzeugte MessageADT
	 */
	public MessageADT invoke();

}
