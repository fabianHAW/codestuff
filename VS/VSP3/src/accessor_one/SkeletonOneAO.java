package accessor_one;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mware_lib.CommunicationModule;
import mware_lib.MessageADT;
import mware_lib.ReferenceModule;
import shared_types.RemoteObjectRef;
import mware_lib.Skeleton;

/**
 * Verweis zum Entwurf:
 * <Entwurfsdokument> : Implementierung der vorgegebenen Methoden in Nr. 3 (d) - accessor_one.
 * 
 * Stellt das Skeleton dar, dass den angeforderten Methodenaufruf letzten Endes auf dem Objekt ausf�hrt,
 * dass diese Methode implementiert. Sendet den R�ckgabewert in einer Nachricht verpackt an den Absender
 * der Nachricht zur�ck.
 * 
 * @author Francis
 */

public class SkeletonOneAO extends Thread implements Skeleton{

	//Identifiziert dieses Skeleton als jenes, dass die Methodenaufrufe auf Objekten der ClassOneImplBase ausf�hrt.
	public static final int ID = ClassOneImplBase.ID;
	private MessageADT message;
	private Socket socket;
	private OutputStream output;
	private ObjectOutputStream ooutput;
	
	public void run(){
		CommunicationModule.debugPrint(this.getClass(), "run SkeletonOneAO");
		MessageADT reply = invoke();
		sendMessageBack(reply);
	}
	
	/**
	 * Sendet die Antwortnachricht an den Absender zur�ck.
	 * 
	 * @param reply Die Antwortnachricht.
	 */
	private void sendMessageBack(MessageADT reply) {
		// TODO Auto-generated method stub
		CommunicationModule.debugPrint(this.getClass(), "send message back");
		try {
			socket = new Socket(reply.getiNetAdrress(), reply.getPort());
			output = socket.getOutputStream();
			ooutput = new ObjectOutputStream(output);
			ooutput.writeObject(reply);
			ooutput.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Entpackt die Parameter der Methode methodOne und wandelt die byte-Repr�sentationen dieser
	 * in die entsprechenden Typen um.
	 * F�hrt danach auf einem Objekt der Klasse ClasseOneAO die Methode auf und packt den R�ckgabwert
	 * in eine Antwortnachricht. Wenn das Objekt dre Klasse ClassOneAO eine Exception schmei�t,
	 * wird die Exception statt des R�ckgabewerts in die Nachricht gelegt.
	 */
	public MessageADT invoke() {
		RemoteObjectRef r = this.message.getObjectRef();
		Object servant = null;

		List<byte[]> args = message.getArguments();
		String param1 = new String(args.get(0));
		int param2 = ByteBuffer.wrap(args.get(1)).getInt();
		String returnVal;
		MessageADT reply;
		servant = ReferenceModule.getServant(r);
		try {
			returnVal = ((ClassOneImplBase)servant).methodOne(param1, param2);
			reply = new MessageADT(
					message.getiNetAdrress(), 
					message.getPort(),
					message.getMessageID(), //alte MessageID zwecks Message-Zuordnung zu Proxy 
					null, 
					1, 
					null, 
					returnVal.getBytes(), 
					null, 
					null);
		} catch (SomeException112 e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			reply = new MessageADT(
					message.getiNetAdrress(), 
					message.getPort(),
					message.getMessageID(), //Alte MessageID zwecks Message-Zuordnung zu Proxy
					null, 
					1, 
					null, 
					null, 
					null, 
					new ArrayList<Exception>(Arrays.asList(e)));
		}
		
		return reply;
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Speichert die empfangene Nachricht, die die entsprechende Methodenbeschreibung der Methode
	 * enth�lt, die ausgef�hrt werden soll, damit bei einem Aufruf von start() auf diesem Thread
	 * darauf zugegriffen werden kann.
	 * @param m Die empfangene Nachricht.
	 */
	public SkeletonOneAO(MessageADT m){
		this.message = m;
	}


}
