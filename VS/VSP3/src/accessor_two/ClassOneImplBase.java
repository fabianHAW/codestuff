package accessor_two;

import mware_lib.CommunicationModule;
import mware_lib.ReferenceModule;
import shared_types.RemoteObjectRef;

/**
 * 
 * @author Fabian
 * 
 *         Bietet den Proxies und den Skeletons die noetigen Methoden zur
 *         Verfuegung
 */
public abstract class ClassOneImplBase {

	public static final int REQUEST = 0;
	public static final int REPLY = 1;
	/**
	 * Eindeutige ID damit im Objekt-Adapter entschieden werden kann, welches
	 * Skeleton fuer die Anfrage zustaendig ist
	 */
	public static final int ID = 3;

	/**
	 * 
	 * @param param1
	 *            irgendeine Zeichenfolge
	 * @param param2
	 *            irgendein doulbe-Wert
	 * @return gibt 2.2 als double zurueck
	 * @throws SomeException112
	 *             wird geworfen, wenn @param2 < 2
	 */
	public abstract double methodOne(String param1, double param2)
			throws SomeException112;

	/**
	 * 
	 * @param param1
	 *            irgendeine Zeichenfolge
	 * @param param2
	 *            irgendein doulbe-Wert
	 * @return gibt 3.3 als double zurueck
	 * @throws SomeException112
	 *             wird geworfen wenn @param2 > 2
	 * @throws SomeException304
	 *             wird geworfen wenn @param1 = "the monkey without shoes"
	 */
	public abstract double methodTwo(String param1, double param2)
			throws SomeException112, SomeException304;

	/**
	 * Laesst, wenn noch nicht vorhanden, ein neues Stellvertreter-Objekt vom
	 * Referenzmodule erzeugen, sonst wird es aus der Liste der bereits
	 * existierenden Proxies geholt
	 * 
	 * @param rawObjectRef
	 *            entfernte Objekt-Referenz
	 * @return Stellvertreter-Objekt
	 */
	public static ClassOneImplBase narrowCast(Object rawObjectRef) {
		if(rawObjectRef == null){
			return null;
		}
		
		boolean inside = ReferenceModule.contains(rawObjectRef);
		ClassOneImplBase remoteObj = null;

		if (inside) {
			remoteObj = (ClassOneImplBase) ReferenceModule
					.getProxy((RemoteObjectRef) rawObjectRef);
			CommunicationModule
					.debugPrint("accessor_two.ClassOneImpleBase: got proxy from ReferenceModule");
		} else {
			remoteObj = new ClassOneImplBaseProxy(
					(RemoteObjectRef) rawObjectRef);
			ReferenceModule.add((RemoteObjectRef) rawObjectRef, remoteObj);
			CommunicationModule
					.debugPrint("accessor_two.ClassOneImpleBase: new proxy created");
		}
		return remoteObj;
	}

}
