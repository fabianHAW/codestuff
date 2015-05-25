package accessor_one;

import java.util.Random;

import mware_lib.CommunicationModule;
import mware_lib.ReferenceModule;
import mware_lib.RemoteObjectRef;

public abstract class ClassTwoImplBase {

	/**
	 * Identifiziert Objekte dieser Klasse und wird von den
	 * Objektadaptern verwendet, um Objekte vom Typ RemoteObjectRef,
	 * welche in der MessageADT sind, den zugehörigen Skeletons zuzuordnen.
	 * Die Skeletons haben eine Liste mit IDs der Objekte, für die sie die
	 * Stellvertetermethoden implementieren. 
	 */
	public static final int ID = 2;
	
	/**
	 * Prüft (param1 / 2) > (Integer.MAX_VALUE / 2) und liefert
	 * (int)param1*2 zurück, wenn dies der Fall ist. Sonst wird
	 * die Exception geschmissen.
	 * @param param1 Irgendein Double Wert.
	 * @return (int)param1*2 Wenn (param1 / 2) > (Integer.MAX_VALUE / 2)
	 * @throws SomeException110
	 */
	public abstract int methodOne(double param1) throws SomeException110;
	
	/**
	 * Erzeugt eine Zufallszahl und prüft ob diese % 2 == 0 ist.
	 * Falls ja, wird diese Zufallszahl zurückgegeben, sonst wird die
	 * Exception geschmissen.
	 * @return random Eine Zufallszahl die % 2 == 0 ist.
	 * @throws SomeException112
	 */
	public abstract double methodTwo() throws SomeException112;
	
	/**
	 * Wenn der Proxy noch nicht im ReferenceModule ist:
	 * Erzeugt ein neues Stellvertreterobjekt für ClassTwoAO und bindet
	 * das rawObejctRef an das Proxy, indem es dieses mit dem Objekt instanziiert.
	 * Wenn der Proxy bereits im ReferenceModule ist:
	 * Sucht diesen und liefert ihn zurück.
	 * @param rawObjectRef Die Objektreferenz eines Objekts vom Typ ClassTwoAO
	 * @return proxy Das Stellvertreterobjekt
	 */
	public static ClassTwoImplBase narrowCast(Object rawObjectRef) {
		if(!ReferenceModule.contains(rawObjectRef)){
		ClassTwoImplBaseProxy proxy = new ClassTwoImplBaseProxy((RemoteObjectRef)rawObjectRef);
		ReferenceModule.add((RemoteObjectRef)rawObjectRef, proxy);
		CommunicationModule
		.debugPrint("accessor_one.ClassTwoImpleBase: new proxy created");
		return proxy;
	} else  {
		CommunicationModule
		.debugPrint("accessor_one.ClassTwoImpleBase: got proxy from ReferenceModule");
		return (ClassTwoImplBase) ReferenceModule.getProxy((RemoteObjectRef)rawObjectRef);
	}
	}

}
