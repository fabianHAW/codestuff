package accessor_one;

import mware_lib.CommunicationModule;
import mware_lib.ReferenceModule;
import shared_types.RemoteObjectRef;

/**
 * Verweise zum Entwurf: <Entwurfsdokument> : Implementierung der vorgegebenen
 * Methoden in Nr. 3 (d) - accessor_one. <Klassendiagramm> : Implementierung
 * durch vorgegebene Methoden in accessor_one - ClassTwoImplBase
 * 
 * @author Fabian
 * 
 *         Stellt den Proxies und den Skeletons die noetigen Methoden zur
 *         Verfuegung
 */
public abstract class ClassTwoImplBase {

	/**
	 * Identifiziert Objekte dieser Klasse und wird von den Objektadaptern
	 * verwendet, um Objekte vom Typ RemoteObjectRef, welche in der MessageADT
	 * sind, den zugeh�rigen Skeletons zuzuordnen. Die Skeletons haben eine
	 * Liste mit IDs der Objekte, f�r die sie die Stellvertetermethoden
	 * implementieren.
	 */
	public static final int ID = 2;

	/**
	 * Pr�ft (param1 / 2) > (Integer.MAX_VALUE / 2) und liefert (int)param1*2
	 * zur�ck, wenn dies der Fall ist. Sonst wird die Exception geschmissen.
	 * 
	 * @param param1
	 *            Irgendein Double Wert.
	 * @return (int)param1*2 Wenn (param1 / 2) > (Integer.MAX_VALUE / 2)
	 * @throws SomeException110
	 */
	public abstract int methodOne(double param1) throws SomeException110;

	/**
	 * Erzeugt eine Zufallszahl und pr�ft ob diese % 2 == 0 ist. Falls ja, wird
	 * diese Zufallszahl zur�ckgegeben, sonst wird die Exception geschmissen.
	 * 
	 * @return random Eine Zufallszahl die % 2 == 0 ist.
	 * @throws SomeException112
	 */
	public abstract double methodTwo() throws SomeException112;

	/**
	 * Wenn der Proxy noch nicht im ReferenceModule ist: Erzeugt ein neues
	 * Stellvertreterobjekt f�r ClassTwoAO und bindet das rawObejctRef an das
	 * Proxy, indem es dieses mit dem Objekt instanziiert. Wenn der Proxy
	 * bereits im ReferenceModule ist: Sucht diesen und liefert ihn zur�ck.
	 * 
	 * @param rawObjectRef
	 *            Die Objektreferenz eines Objekts vom Typ ClassTwoAO
	 * @return proxy Das Stellvertreterobjekt
	 */
	public static ClassTwoImplBase narrowCast(Object rawObjectRef) {
		if (rawObjectRef == null) {
			return null;
		}

		if (!ReferenceModule.contains(rawObjectRef)) {
			ClassTwoImplBaseProxy proxy = new ClassTwoImplBaseProxy(
					(RemoteObjectRef) rawObjectRef);
			ReferenceModule.add((RemoteObjectRef) rawObjectRef, proxy);
			CommunicationModule
					.debugPrint("accessor_one.ClassTwoImpleBase: new proxy created");
			return proxy;
		} else {
			CommunicationModule
					.debugPrint("accessor_one.ClassTwoImpleBase: got proxy from ReferenceModule");
			return (ClassTwoImplBase) ReferenceModule
					.getProxy((RemoteObjectRef) rawObjectRef);
		}
	}

}
