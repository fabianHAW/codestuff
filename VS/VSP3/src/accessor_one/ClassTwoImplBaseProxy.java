package accessor_one;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import shared_types.RemoteObjectRef;
import mware_lib.CommunicationModule;
import mware_lib.CommunicationModuleThread;
import mware_lib.MessageADT;

/**
 * Verweis zum Entwurf: <Entwurfsdokument> : Implementierung der vorgegebenen
 * Methoden in Nr. 3 (d) - accessor_one. <Klassendiagramm> : Implementierung
 * durch vorgegebene Methoden in accessor_one - ClassTwoImplBaseProxy
 * 
 * @author Francis Stellt den Stub dar -> Objekte seitens des Clients greifen
 *         darauf zu
 * 
 */

public class ClassTwoImplBaseProxy extends ClassTwoImplBase {

	private final int REQUEST = 0;
	private RemoteObjectRef rawObjRef;

	/**
	 * Speichert die RemoteObjectRef in der Instanzvariablen rawObjRef.
	 * 
	 * @param rawObj
	 *            Die Objekt-Referenz vom Nameservice
	 */
	public ClassTwoImplBaseProxy(RemoteObjectRef rawObj) {
		rawObjRef = rawObj;
	}

	/**
	 * 
	 * 
	 * Erzeugt aus param1 ein byte-Array, legt es in einer Liste in die
	 * MessageADT zusammen mit der Internetadresse des CommunicationModule, dem
	 * Methodennamen, dem MessageTyp (Request) und der rawObjRef und sendet die
	 * MessageADT �ber das Kommunikationsmodul an den Host auf dem das Objekt
	 * ist, dass diese Methode implementiert. Wartet anschlie�end auf ein
	 * Ergebnis und liefert dieses zur�ck.
	 */
	public int methodOne(double param1) throws SomeException110 {
		// TODO Auto-generated method stub
		byte[] p1 = new byte[Double.BYTES];
		ByteBuffer.wrap(p1).putDouble(param1);
		ArrayList<byte[]> arguments = new ArrayList<byte[]>(Arrays.asList(p1));
		MessageADT m = new MessageADT(CommunicationModule.getLocalHost(),
				CommunicationModule.getCommunicationmoduleport(),
				CommunicationModule.messageIDCounter(), "methodOne", REQUEST,
				rawObjRef, null, arguments, null);

		MessageADT received = sendRequest(m);
		Integer result = null;

		if (received.getReturnVal() != null) {
			result = (int) unmarshals(received, UNMARSHAL_TYPE.METHOD_ONE);
		} else {
			for (Exception e : received.getExceptionList()) {
				throw (SomeException110) e;
			}
		}

		return result;
	}

	/**
	 * Erzeugt eine MessageADT mit der Internetadresse des CommunicationModule,
	 * dem Methodennamen, dem MessageTyp (Request) und der rawObjRef und sendet
	 * die MessageADT �ber das Kommunikationsmodul an den Host auf dem das
	 * Objekt ist, dass diese Methode implementiert. Wartet anschlie�end auf ein
	 * Ergebnis und liefert dieses zur�ck.
	 */
	public double methodTwo() throws SomeException112 {
		MessageADT m = new MessageADT(CommunicationModule.getLocalHost(), -1,
				CommunicationModule.messageIDCounter(), "methodTwo", REQUEST,
				rawObjRef, null, null, null);

		MessageADT received = sendRequest(m);
		Double result = null;

		if (received.getReturnVal() != null) {
			result = (double) unmarshals(received, UNMARSHAL_TYPE.METHOD_TWO);
		} else {
			for (Exception e : received.getExceptionList()) {
				throw (SomeException112) e;
			}
		}

		return result;
	}

	/**
	 * Entpackt den R�ckgabewert aus der nachricht und liefert ihn zur�ck.
	 * 
	 * @param m
	 *            Die Antwortnachricht.
	 * @return r Das Ergebnis.
	 */
	private Object unmarshals(MessageADT m, UNMARSHAL_TYPE type) {
		if (type == UNMARSHAL_TYPE.METHOD_ONE) {
			byte[] returnval = m.getReturnVal();
			return ByteBuffer.wrap(returnval).getInt();
		} else if (type == UNMARSHAL_TYPE.METHOD_TWO) {
			byte[] returnval = m.getReturnVal();
			return ByteBuffer.wrap(returnval).getDouble();
		}

		return null;
	}

	/**
	 * Sendet �ber das Kommunikationsmodul die Request-Nachricht an den Host,
	 * auf dem das Objekt ist, das die Methode implementiert.
	 * 
	 * @param m
	 *            Die zu versendende Nachricht.
	 * @return m2 Die empfangene Nachricht.
	 */
	private MessageADT sendRequest(MessageADT m) {
		CommunicationModuleThread t = CommunicationModule
				.getNewCommunicationThread(m);

		synchronized (t) {
			try {
				t.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return t.getReceivedMessage();
	}

	/**
	 * Identifiziert die aufrufende Methode, damit entschieden werden kann, ob
	 * der Return-Wert aus der Antwortnachricht als int oder double gelesen
	 * wird.
	 * 
	 * @author Francis
	 * 
	 */
	private enum UNMARSHAL_TYPE {
		METHOD_ONE, METHOD_TWO;
	}

}
