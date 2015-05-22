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
 * @author Francis und Fabian
 * Stellt den Stub dar -> Objekte seitens des Clients greifen darauf zu
 *
 */

public class ClassOneImplBaseProxy extends ClassOneImplBase{

	private final int REQUEST = 0;
	private final int REPLY = 1;
	private RemoteObjectRef rawObjRef;
	private Socket socket;
	private InputStream input;
	private ObjectInputStream oinput;
	private OutputStream output;
	private ObjectOutputStream ooutput;
	
	public ClassOneImplBaseProxy(RemoteObjectRef rawObjRef){
		this.rawObjRef = rawObjRef;
	}
	
	public String methodOne(String param1, int param2) throws SomeException112 {
		// TODO Auto-generated method stub
		byte[] p1 = param1.getBytes();
		byte[] p2 = ByteBuffer.allocate(Integer.BYTES).putInt(param2).array();
		ArrayList<byte[]> arguments = new ArrayList<byte[]>(Arrays.asList(p1, p2));
		MessageADT m = new MessageADT(CommunicationModule.getLocalHost(), 1, "methodOne", REQUEST, rawObjRef, null, arguments, null);
		
		try {
			socket = new Socket(m.getObjectRef().getInetAddress(), m.getObjectRef().getPort());
			input = socket.getInputStream();
			oinput = new ObjectInputStream(input);
			output = socket.getOutputStream();
			ooutput = new ObjectOutputStream(output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//rueckgabewert empfangen		
		sendRequest(m);
		MessageADT received = listenToSocket();
		String result = unmarshals(received);
		
		
		return result;
	}
	
	
	private MessageADT listenToSocket(){
		MessageADT receivedMessage = null;
		try {
			receivedMessage = (MessageADT)oinput.readObject();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return receivedMessage;
	}
	
	private String unmarshals(MessageADT m){
		byte[] returnval = m.getReturnVal();
		return new String(returnval);
	}
	
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
	
	
	
	
	
	
	
//	private void sendRequest(MessageADT m){
//	
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
//			socket.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
	

}
