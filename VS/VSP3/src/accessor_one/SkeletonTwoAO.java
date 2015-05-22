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
 * 
 * @author Francis und Fabian
 *
 * Stellt das Skeleton dar -> Objekte seitens des Servers greifen darauf zu
 */

public class SkeletonTwoAO extends Thread implements Skeleton{

	public static final int ID = ClassTwoImplBase.ID;
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
		if(message.getMethodName().toLowerCase().equals("methodone")){
			return callMethodOne();
		}else{ //else methodTwo
			return callMethodTwo();
		}
	}
	
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
							null, 
							null, 
							null, 
							null, 
							b, 
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
	}

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
					null, 
					null, 
					null, 
					null, 
					b, 
					null, 
					null);
		} catch (SomeException110 e) {
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
	}

	public SkeletonTwoAO(MessageADT m){
		this.message = m;
	}

}
