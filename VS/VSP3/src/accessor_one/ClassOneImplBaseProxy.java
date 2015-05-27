package accessor_one;


import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import mware_lib.CommunicationModule;
import mware_lib.CommunicationModuleThread;
import mware_lib.MessageADT;
import mware_lib.RemoteObjectRef;

/**
 *  Verweise zum Entwurf:
 * <Entwurfsdokument> : Implementierung der vorgegebenen Methoden in Nr. 3 (d) - accessor_one.
 * <Klassendiagramm> : Implementierung durch vorgegebene Methoden in accessor_one - ClassOneImplBaseProxy
 * <Sequenzdiagramm client_request> : Realisierung der Sequenznummern 7, 9.1, 10, 11, 13 aus dem Sequenzdiagramm client_request
 * <Sequenzdiagramm client_reply> : Realisierung der Sequenznummern 2.1, 3
 * 
 * @author Francis und Fabian
 * Stellt den Stub dar -> Objekte seitens des Clients greifen darauf zu
 *
 */

public class ClassOneImplBaseProxy extends ClassOneImplBase{

	private final int REQUEST = 0;
	private RemoteObjectRef rawObjRef;

	/**
	 * Verweis zum Entwurf:
	 * <Sequenzdiagramm client_request> : Realisierung der Sequenznummer 9.1
	 * 
	 * @param rawObjRef Die Die Objekt-Referenz vom Nameservice.
	 */
	public ClassOneImplBaseProxy(RemoteObjectRef rawObjRef){
		this.rawObjRef = rawObjRef;
	}
	
	/**
	 * 	Verweis zum Entwurf:
	 * <Sequenzdiagramm client_request> : Realisierung der Sequenznummer 7
	 * 
	 * Sendet den Methodenaufruf an den Servant.
	 * @param param1 Irgendein String
	 * @param param2 Irgendein Int
	 */
	public String methodOne(String param1, int param2) throws SomeException112 {
		// TODO Auto-generated method stub
		ArrayList<byte[]> vals = valsToByte(param1, param2);
		
		//Verweis zum Entwurf:
		//<Sequenzdiagramm client_request> : Realisierung der Sequenznummer 11
		MessageADT m = new MessageADT(CommunicationModule.getLocalHost(), CommunicationModule.messageIDCounter(), "methodOne", REQUEST, rawObjRef, null, vals, null);
		
		MessageADT received = sendRequest(m);
		//MessageADT received = listenToSocket();
		String result = null;
		
		//Verweis zum Entwurf:
		//<Sequenzdiagramm client_reply>  : Realisierung der Sequenznummer 4
		if(received.getReturnVal() != null){
			return unmarshals(received);
		}
	
		CommunicationModule.debugPrint(received.getExceptionList().get(0).getMessage());
		return null;
	}
	
	/**
	 * Verweis zum Entwurf:
	 * <Sequenzdiagramm client_request>: Realisierung der Sequenznummer 10
	 * 
	 * Packt die Parameter in byte-Arrays.
	 * 
	 * @param param1 Irgendein String
	 * @param param2 Irgendein Int
	 * @return new Arraylist<byte[]> Eine Liste aus byte-Arrays der Parameter param1 und param2.
	 */
	private ArrayList<byte[]> valsToByte(String param1, int param2){
		byte[] p1 = param1.getBytes();
		byte[] p2 = ByteBuffer.allocate(Integer.BYTES).putInt(param2).array();
		return new ArrayList<byte[]>(Arrays.asList(p1, p2));
		
		
	}
	
	/**
	 * Verweis zum Entwurf:
	 * <Sequenzdiagramm client_reply> Sequenznummer 3
	 * @param m Die Antwortnachricht.
	 * @return s Der String-Rückgabewert der methodOne, welcher in der Nachricht ist.
	 */
	private String unmarshals(MessageADT m){
		byte[] returnval = m.getReturnVal();
		return new String(returnval);
	}
	
	/**
	 * Verweis zum Entwurf:
	 * <Sequenzdiagramm client_request> : Realisierung der Sequenznummer 12
	 * 
	 * @param m Die zu versendende Nachricht
	 * @return m2 Die Antwortnachricht, die im CommunicationModuleThread gespeichert ist.
	 */
	private MessageADT sendRequest(MessageADT m){
		CommunicationModuleThread t = CommunicationModule.getNewCommunicationThread(m);
		
		synchronized(t){
			try{
				//Verweis zum Entwurf:
				//<Sequenzdiagramm client_reply>  : Realisierung der Sequenznummer 13
				t.wait();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
		//Verweis zum Entwurf:
		//<Sequenzdiagramm client_reply>  : Realisierung der Sequenznummer 2.1
		return t.getReceivedMessage();
	}

}
