package accessor_one;

import shared_types.RemoteObjectRef;
import mware_lib.CommunicationModule;
import mware_lib.ReferenceModule;

/**
 *  Verweise zum Entwurf:
 * <Entwurfsdokument> : Implementierung der vorgegebenen Methoden in Nr. 3 (d) - accessor_one.
 * <Klassendiagramm> : Implementierung durch vorgegebene Methoden in accessor_one - ClassOneImplBase
 * <Sequenzdiagramm client_reply> : Realiserung der Sequenznummern 5, 5.1, 5.2, 5.3, 6, 6.1, 7, 8, 
 * 
 * @author Fabian
 * 
 *         Stellt den Proxies und den Skeletons die noetigen Methoden zur
 *         Verfuegung
 */
public abstract class ClassOneImplBase {

	/**
	 * 
	 * Identifiziert Objekte dieser Klasse und wird von den
	 * Objektadaptern verwendet, um Objekte vom Typ RemoteObjectRef,
	 * welche in der MessageADT sind, den zugeh�rigen Skeletons zuzuordnen.
	 * Die Skeletons haben eine Liste mit IDs der Objekte, f�r die sie die
	 * Stellvertetermethoden implementieren. 
	 */
	public static final int ID = 1;
	
	/**
	 * 
	 * Pr�ft ob param2 % 2 == 0 ist und wenn dies so ist, 
	 * liefert die Methode param1 + " " + param2 + " % 2 == 0" zur�ck,
	 * sonst wirft sie die Exception. 
	 * @param param1 Irgendein String.
	 * @param param2 Irgendein int.
	 * @return result In der Form param1 + " " + param2 + " % 2 == 0"
	 * @throws SomeException112
	 */
	public abstract String methodOne(String param1, int param2) throws SomeException112;
	
	/**
	 * Verweis zum Entwurf:
	 * <Sequenzdiagramm client_reply> : Realiserung der Sequenznummern 5
	 * 
	 * Wenn der Proxy noch nicht im ReferenceModule ist:
	 * Erzeugt ein neues Stellvertreterobjekt f�r ClassOneAO und bindet
	 * das rawObejctRef an das Prox, indem es dieses mit dem Objekt instanziiert.
	 * Wenn der Proxy bereits im ReferenceModule ist:
	 * Sucht diesen und liefert ihn zur�ck.
	 * @param rawObjectRef Die Objektreferenz eines Objekts vom Typ ClassOneAO
	 * @return proxy Das Stellvertreterobjekt
	 */
	public static ClassOneImplBase narrowCast(Object rawObjectRef) {
		if(rawObjectRef == null){
			return null;
		}
		//Verweis zum Entwurf:
		//<Sequenzdiagramm client_reply> : Realiserung der Sequenznummern 5.3
		ClassOneImplBaseProxy remoteObj = null;
		//Verweis zum Entwurf:
		//<Sequenzdiagramm client_reply> : Realiserung der Sequenznummern 5.1, 5.2
		if(!ReferenceModule.contains(rawObjectRef)){
		//Verweis zum Entwurf:
		//<Sequenzdiagramm client_reply> : Realiserung der Sequenznummern 7
		remoteObj = new ClassOneImplBaseProxy((RemoteObjectRef)rawObjectRef);
		//Verweis zum Entwurf:
		//<Sequenzdiagramm client_reply> : Realiserung der Sequenznummern 8
		ReferenceModule.add((RemoteObjectRef)rawObjectRef, remoteObj);
		CommunicationModule
		.debugPrint("accessor_one.ClassOneImpleBase: new proxy created");
		return remoteObj;
		}else{
			CommunicationModule
			.debugPrint("accessor_one.ClassOneImpleBase: got proxy from ReferenceModule");
			//Verweis zum Entwurf:
			//<Sequenzdiagramm client_reply> : Realiserung der Sequenznummern 6, 6.1
			remoteObj = (ClassOneImplBaseProxy) ReferenceModule.getProxy((RemoteObjectRef)rawObjectRef);
			return remoteObj;
		}
		
	}

}
