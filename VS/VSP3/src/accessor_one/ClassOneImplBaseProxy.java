package accessor_one;

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
	
	public ClassOneImplBaseProxy(RemoteObjectRef rawObjRef){
		this.rawObjRef = rawObjRef;
	}
	
	public String methodOne(String param1, int param2) throws SomeException112 {
		// TODO Auto-generated method stub
		byte[] p1 = param1.getBytes();
		byte[] p2 = ByteBuffer.allocate(Integer.BYTES).putInt(param2).array();
		ArrayList<byte[]> arguments = new ArrayList<byte[]>(Arrays.asList(p1, p2));
		MessageADT m = new MessageADT(CommunicationModule.getInetAddress(), 1, "methodOne", REQUEST, rawObjRef, null, arguments, null);
		MessageADT received = sendRequest(m);
		String result = unmarshals(received);
		return result;
	}
	
	
	private void listenToSocket(){
		
	}
	
	private String unmarshals(MessageADT m){
		byte[] returnval = m.getReturnVal();
		return new String(returnval);
	}
	
	private MessageADT sendRequest(MessageADT m){
		CommunicationModuleThread t = CommunicationModule.sendRequest(m);
		t.run();
		
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
