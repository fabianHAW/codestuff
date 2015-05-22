package accessor_one;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
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
 * 
 * @author Francis und Fabian
 *
 * Stellt das Skeleton dar -> Objekte seitens des Servers greifen darauf zu
 */

public class SkeletonOneAO extends Thread implements Skeleton{

	public static final int ID = ClassOneImplBase.ID;
	private MessageADT message;
	private Socket socket;
	private OutputStream output;
	private ObjectOutputStream ooutput;
	
	public void run(){
		MessageADT reply = invoke();
		sendMessageBack(reply);
	}
	
	private void sendMessageBack(MessageADT reply) {
		// TODO Auto-generated method stub
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

	public MessageADT invoke() {
		ClassOneAO servant = new ClassOneAO();
		List<byte[]> args = message.getArguments();
		String param1 = new String(args.get(0));
		int param2 = ByteBuffer.wrap(args.get(1)).getInt();
		String returnVal;
		MessageADT reply;
		
		try {
			returnVal = servant.methodOne(param1, param2);
			reply = new MessageADT(
					message.getiNetAdrress(), 
					null, 
					null, 
					null, 
					null, 
					returnVal.getBytes(), 
					null, 
					null);
		} catch (SomeException112 e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			reply = new MessageADT(
					message.getiNetAdrress(), 
					null, 
					null, 
					null, 
					null, 
					null, 
					null, 
					new ArrayList<Exception>(Arrays.asList(e)));
		}
		
		return reply;
		// TODO Auto-generated method stub
		
	}
	
	public SkeletonOneAO(MessageADT m){
		this.message = m;
	}


}
