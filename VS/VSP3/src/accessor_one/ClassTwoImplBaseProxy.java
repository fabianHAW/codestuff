package accessor_one;


import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import mware_lib.CommunicationModule;
import mware_lib.CommunicationModuleThread;
import mware_lib.MessageADT;
import mware_lib.RemoteObjectRef;

/**
 * 
 * @author Francis und Fabian Stellt den Stub dar -> Objekte seitens des Clients
 *         greifen darauf zu
 *
 */

public class ClassTwoImplBaseProxy extends ClassTwoImplBase {

	private final int REQUEST = 0;
	private RemoteObjectRef rawObjRef;

	
	public ClassTwoImplBaseProxy(RemoteObjectRef rawObj){
		rawObjRef = rawObj;
	}

	/**
	 * Erzeugt aus param1 ein byte-Array, legt es in einer Liste in die MessageADT
	 * zusammen mit der Internetadresse des CommunicationModule, dem Methodennamen,
	 * dem MessageTyp (Request) und der rawObjRef und sendet die MessageADT über das
	 * Kommunikationsmodul an den Host auf dem das Objekt ist, dass diese Methode implementiert.
	 * Wartet anschließend auf ein Ergebnis und liefert dieses zurück.
	 */
	public int methodOne(double param1) throws SomeException110 {
		// TODO Auto-generated method stub
		byte[] p1 = new byte[Double.BYTES];
		ByteBuffer.wrap(p1).putDouble(param1);
		ArrayList<byte[]> arguments = new ArrayList<byte[]>(Arrays.asList(p1));
		MessageADT m = new MessageADT(CommunicationModule.getLocalHost(), CommunicationModule.messageIDCounter(),
				"methodOne", REQUEST, rawObjRef, null, arguments, null);

		MessageADT received = sendRequest(m);
		//MessageADT received = listenToSocket();
		int result = unmarshals(received);

		return result;
	}

	/**
	 * Erzeugt eine MessageADT mit der Internetadresse des CommunicationModule, dem Methodennamen,
	 * dem MessageTyp (Request) und der rawObjRef und sendet die MessageADT über das
	 * Kommunikationsmodul an den Host auf dem das Objekt ist, dass diese Methode implementiert.
	 * Wartet anschließend auf ein Ergebnis und liefert dieses zurück.
	 */
	public double methodTwo() throws SomeException112 {
		MessageADT m = new MessageADT(CommunicationModule.getLocalHost(), CommunicationModule.messageIDCounter(),
				"methodTwo", REQUEST, rawObjRef, null, null, null);

		MessageADT received = sendRequest(m);
		//MessageADT received = listenToSocket();
		int result = unmarshals(received);

		return result;
	}

	/**
	 * Entpackt den Rückgabewert aus der nachricht und liefert ihn zurück.
	 * @param m Die Antwortnachricht.
	 * @return r Das Ergebnis.
	 */
	private int unmarshals(MessageADT m) {
		byte[] returnval = m.getReturnVal();
		return ByteBuffer.wrap(returnval).getInt();
	}

	/**
	 * Sendet über das Kommunikationsmodul die Request-Nachricht an den Host,
	 * auf dem das Objekt ist, das die Methode implementiert.
	 * @param m Die zu versendende Nachricht.
	 * @return m2 Die empfangene Nachricht.
	 */
	private MessageADT sendRequest(MessageADT m){
		CommunicationModuleThread t = CommunicationModule.getNewCommunicationThread(m);
		
		synchronized(t){
			try{
				t.wait();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
		return t.getReceivedMessage();
	}

}
