package mware_lib;

import java.util.List;

/**
 * 
 * @author Francis und Fabian
 *
 * Stellt Methoden fuer die Skeletons (Serverseite) zur Verfuegung
 */

public interface Skeleton {

	//ruft die Methoden des Servers auf
	public MessageADT invoke();
		
}
