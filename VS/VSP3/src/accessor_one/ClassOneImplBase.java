package accessor_one;

import mware_lib.CommunicationModule;
import mware_lib.ReferenceModule;
import mware_lib.RemoteObjectRef;

/**
 * 
 * @author Fabian
 * 
 *         Stellt den Proxies und den Skeletons die noetigen Methoden zur
 *         Verfuegung
 */
public abstract class ClassOneImplBase {

	/**
	 * Identifiziert Objekte dieser Klasse und wird von den
	 * Objektadaptern verwendet, um Objekte vom Typ RemoteObjectRef,
	 * welche in der MessageADT sind, den zugehörigen Skeletons zuzuordnen.
	 * Die Skeletons haben eine Liste mit IDs der Objekte, für die sie die
	 * Stellvertetermethoden implementieren. 
	 */
	public static final int ID = 1;
	
	/**
	 * Prüft ob param2 % 2 == 0 ist und wenn dies so ist, 
	 * liefert die Methode param1 + " " + param2 + " % 2 == 0" zurück,
	 * sonst wirft sie die Exception. 
	 * @param param1 Irgendein String.
	 * @param param2 Irgendein int.
	 * @return result In der Form param1 + " " + param2 + " % 2 == 0"
	 * @throws SomeException112
	 */
	public abstract String methodOne(String param1, int param2) throws SomeException112;
	
	/**
	 * Wenn der Proxy noch nicht im ReferenceModule ist:
	 * Erzeugt ein neues Stellvertreterobjekt für ClassOneAO und bindet
	 * das rawObejctRef an das Prox, indem es dieses mit dem Objekt instanziiert.
	 * Wenn der Proxy bereits im ReferenceModule ist:
	 * Sucht diesen und liefert ihn zurück.
	 * @param rawObjectRef Die Objektreferenz eines Objekts vom Typ ClassOneAO
	 * @return proxy Das Stellvertreterobjekt
	 */
	public static ClassOneImplBase narrowCast(Object rawObjectRef) {
		if(!ReferenceModule.contains(rawObjectRef)){
		ClassOneImplBaseProxy proxy = new ClassOneImplBaseProxy((RemoteObjectRef)rawObjectRef);
		ReferenceModule.add((RemoteObjectRef)rawObjectRef, proxy);
		CommunicationModule
		.debugPrint("accessor_one.ClassOneImpleBase: new proxy created");
		return proxy;
		}else{
			CommunicationModule
			.debugPrint("accessor_one.ClassOneImpleBase: got proxy from ReferenceModule");
			return (ClassOneImplBase) ReferenceModule.getProxy((RemoteObjectRef)rawObjectRef);
		}
		
	}

}
