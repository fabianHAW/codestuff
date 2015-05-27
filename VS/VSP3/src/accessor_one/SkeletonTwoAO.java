package accessor_one;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mware_lib.CommunicationModule;
import mware_lib.MessageADT;
import mware_lib.Skeleton;

/**
 * Verweis zum Entwurf:
 * <Entwurfsdokument> : Implementierung der vorgegebenen Methoden in Nr. 3 (d) - accessor_one.
 * 
 * Stellt das Skeleton dar, dass den angeforderten Methodenaufruf letzten Endes auf dem Objekt ausführt,
 * dass diese Methode implementiert. Sendet den Rückgabewert in einer Nachricht verpackt an den Absender
 * der Nachricht zurück.
 * 
 * @author Francis
 */

public class SkeletonTwoAO extends Thread implements Skeleton{

	//Identifiziert dieses Skeleton als jenes, dass die Methodenaufrufe auf Objekten der ClassTwoImplBase ausführt.
	public static final int ID = ClassTwoImplBase.ID;
	private MessageADT message;
	private Socket socket;
	private OutputStream output;
	private ObjectOutputStream ooutput;
	
	public void run(){
		MessageADT reply = invoke();
		sendMessageBack(reply);
		CommunicationModule.debugPrint(this.getClass(), "run SkeletonTwoAO");
	}
	
	/**
	 * Sendet die Antwortnachricht an den Absender zurück.
	 * 
	 * @param reply Die Antwortnachricht.
	 */
	private void sendMessageBack(MessageADT reply) {
		// TODO Auto-generated method stub
		CommunicationModule.debugPrint(this.getClass(), "send message back");
		try {
			socket = new Socket(reply.getiNetAdrress(), CommunicationModule.getCommunicationmoduleport());
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
	 * Entscheidet zunächst, welche der beiden angebotenen Methoden ausgeführt werden soll,
	 * und ruft dann die entsprechende Methode auf.
	 * 
	 * @return reply Die Antwortnachricht.
	 */
	public MessageADT invoke() {
		if(message.getMethodName().toLowerCase().equals("methodone")){
			return callMethodOne();
		}else{ //else methodTwo
			return callMethodTwo();
		}
	}
	
	/**
	 * Führt danach auf einem Objekt der Klasse ClasseTwoAO die Methode auf und packt den Rückgabwert
	 * in eine Antwortnachricht. Wenn das Objekt der Klasse ClassTwoAO eine Exception schmeißt,
	 * wird die Exception statt des Rückgabewerts in die Nachricht gelegt.
	 * 
	 * @return reply Die Antwortnachricht.
	 */
	private MessageADT callMethodTwo() {
		// TODO Auto-generated method stub
				ClassTwoAO servant = new ClassTwoAO();
				double returnVal;
				MessageADT reply;
				
				try {
					returnVal = servant.methodTwo();
					byte[] b = new byte[Double.BYTES];
					ByteBuffer.wrap(b).putDouble(returnVal);
					reply = new MessageADT(
							message.getiNetAdrress(), 
							message.getMessageID(), //Alte MessageID zwecks Message-Zuordnung zu Proxy
							null, 
							1, 
							null, 
							b, 
							null, 
							null);
				} catch (SomeException112 e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					reply = new MessageADT(
							message.getiNetAdrress(), 
							message.getMessageID(), //Alte MessageID zwecks Message-Zuordnung zu Proxy
							null, 
							1, 
							null, 
							null, 
							null, 
							new ArrayList<Exception>(Arrays.asList(e)));
				}
				return reply;
	}

	/**
	 * Entpackt den Parameter der Methode methodOne und wandelt die byte-Repräsentationen dessen
	 * in den entsprechenden Typ um.
	 * Führt danach auf einem Objekt der Klasse ClasseTwoAO die Methode auf und packt den Rückgabwert
	 * in eine Antwortnachricht. Wenn das Objekt der Klasse ClassTwoAO eine Exception schmeißt,
	 * wird die Exception statt des Rückgabewerts in die Nachricht gelegt.
	 * 
	 * @return reply Die Antwortnachricht.
	 */
	private MessageADT callMethodOne() {
		// TODO Auto-generated method stub
		ClassTwoAO servant = new ClassTwoAO();
		List<byte[]> args = message.getArguments();
		double param1 = ByteBuffer.wrap(args.get(0)).getDouble();
		int returnVal;
		MessageADT reply;
		
		try {
			returnVal = servant.methodOne(param1);
			byte[] b = new byte[Integer.BYTES];
			ByteBuffer.wrap(b).putInt(returnVal);
			reply = new MessageADT(
					message.getiNetAdrress(), 
					message.getMessageID(), //Alte MessageID zwecks Message-Zuordnung zu Proxy
					null, 
					1, 
					null, 
					b, 
					null, 
					null);
		} catch (SomeException110 e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			reply = new MessageADT(
					message.getiNetAdrress(), 
					message.getMessageID(), //Alte MessageID zwecks Message-Zuordnung zu Proxy
					null, 
					1, 
					null, 
					null, 
					null, 
					new ArrayList<Exception>(Arrays.asList(e)));
		}
		return reply;
	}

	/**
	 * Speichert die empfangene Nachricht, die die entsprechende Methodenbeschreibung der Methode
	 * enthält, die ausgeführt werden soll, damit bei einem Aufruf von start() auf diesem Thread
	 * darauf zugegriffen werden kann.
	 * @param m Die empfangene Nachricht.
	 */
	public SkeletonTwoAO(MessageADT m){
		this.message = m;
	}

}
