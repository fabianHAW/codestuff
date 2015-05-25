package mware_lib;

import java.util.ArrayList;

/**
 * 
 * @author Francis
 * 
 *         Ist fuer die Weiterleitung der eingehenden entfernten
 *         Methodenaufrufen zustaendig. Er waehlt das korrekte Skeleton aus, was
 *         fuer die entsprechende Methode zustaendig ist
 */
public interface ObjectAdapter {

	/**
	 * Initiiert neues Skeleton und startet diesen
	 * 
	 * @param m
	 *            MessageADT mit den notwendigen Informationen zum entfernten
	 *            Methodenaufruf
	 */
	public void initSkeleton(MessageADT m);

	/**
	 * Gibt die Liste der Skeleton-IDs zurueck fuer die der Objekt-Adapter
	 * zustaendig ist
	 * 
	 * @return Liste mit allen Skeleton-IDs
	 */
	public ArrayList<Integer> getSkeletonIDs();

}
