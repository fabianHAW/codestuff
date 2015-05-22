package accessor_one;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
//	private final int REPLY = 1;
	private RemoteObjectRef rawObjRef;
//	private Socket socket;
//	private InputStream input;
//	private ObjectInputStream oinput;
//	private OutputStream output;
//	private ObjectOutputStream ooutput;
	
	public ClassTwoImplBaseProxy(RemoteObjectRef rawObj){
		rawObjRef = rawObj;
	}

	public int methodOne(double param1) throws SomeException110 {
		// TODO Auto-generated method stub
		byte[] p1 = new byte[Double.BYTES];
		ByteBuffer.wrap(p1).putDouble(param1);
		ArrayList<byte[]> arguments = new ArrayList<byte[]>(Arrays.asList(p1));
		MessageADT m = new MessageADT(CommunicationModule.getLocalHost(), 1,
				"methodOne", REQUEST, rawObjRef, null, arguments, null);

//		try {
//			socket = new Socket(m.getObjectRef().getInetAddress(), m
//					.getObjectRef().getPort());
//			input = socket.getInputStream();
//			oinput = new ObjectInputStream(input);
//			output = socket.getOutputStream();
//			ooutput = new ObjectOutputStream(output);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		MessageADT received = sendRequest(m);
		//MessageADT received = listenToSocket();
		int result = unmarshals(received);

		return result;
	}

	public double methodTwo() throws SomeException112 {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		
		MessageADT m = new MessageADT(CommunicationModule.getLocalHost(), 1,
				"methodTwo", REQUEST, rawObjRef, null, null, null);

//		try {
//			socket = new Socket(m.getObjectRef().getInetAddress(), m
//					.getObjectRef().getPort());
//			input = socket.getInputStream();
//			oinput = new ObjectInputStream(input);
//			output = socket.getOutputStream();
//			ooutput = new ObjectOutputStream(output);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		MessageADT received = sendRequest(m);
		//MessageADT received = listenToSocket();
		int result = unmarshals(received);

		return result;
	}

	/**
	 * Nicht implementiert.
	 * @param o
	 * @return
	 */
	public byte[] valsToByte(List<Object> o) {

		return null;
	}

//	private MessageADT listenToSocket() {
//		MessageADT receivedMessage = null;
//		try {
//			receivedMessage = (MessageADT) oinput.readObject();
//		} catch (ClassNotFoundException | IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return receivedMessage;
//	}

	private int unmarshals(MessageADT m) {
		byte[] returnval = m.getReturnVal();
		return ByteBuffer.wrap(returnval).getInt();
	}

//	private void sendRequest(MessageADT m) {
//
//		try {
//			ooutput.writeObject(m);
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		try {
//			output.close();
//			ooutput.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
	
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
