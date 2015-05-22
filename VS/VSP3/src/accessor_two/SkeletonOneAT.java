package accessor_two;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import mware_lib.CommunicationModule;
import mware_lib.MessageADT;
import mware_lib.ReferenceModule;
import mware_lib.RemoteObjectRef;
import mware_lib.Skeleton;

/**
 * 
 * @author Francis und Fabian
 * 
 */

public class SkeletonOneAT extends Thread implements Skeleton {

	private MessageADT m;
	public static final int ID = ClassOneImplBase.ID;

	public SkeletonOneAT(MessageADT m) {
		this.m = m;
	}

	public void run() {
		MessageADT mReturn = invoke();
		sendBack(mReturn);
	}

	public MessageADT invoke() {
		RemoteObjectRef r = this.m.getObjectRef();
		Object s = null;
		String method = this.m.getMethodName();
		String param1 = new String(this.m.getArguments().get(0));
		double param2 = Double.parseDouble(new String(this.m.getArguments()
				.get(1)));
		byte[] returnVal = null;
		List<Exception> le = new ArrayList<Exception>();

		s = ReferenceModule.getServant(r);

		if (method.equals("methodOne")) {
			try {
				returnVal = String.valueOf(
						((ClassOneAT) s).methodOne(param1, param2)).getBytes();
			} catch (SomeException112 e) {
				le.add(e);
			}
		} else if (method.equals("methodTwo")) {
			try {
				returnVal = String.valueOf(
						((ClassOneAT) s).methodTwo(param1, param2)).getBytes();
			} catch (SomeException112 e) {
				le.add(e);
			} catch (SomeException304 e) {
				le.add(e);
			}
		}
		return generateNewMessage(returnVal, le);
	}

	private MessageADT generateNewMessage(byte[] returnVal, List<Exception> le) {
		if (le.size() != 0) {
			return new MessageADT(this.m.getiNetAdrress(),
					this.m.getMessageID(), this.m.getMethodName(),
					ClassOneImplBase.REPLY, this.m.getObjectRef(), returnVal,
					this.m.getArguments(), le);
		}
		return new MessageADT(this.m.getiNetAdrress(), this.m.getMessageID(),
				this.m.getMethodName(), ClassOneImplBase.REPLY,
				this.m.getObjectRef(), returnVal, this.m.getArguments(),
				this.m.getExceptionList());
	}

	public void sendBack(MessageADT mReturn) {
		Socket s = null;
		ObjectOutputStream o = null;
		try {
			s = new Socket(mReturn.getiNetAdrress(),
					CommunicationModule.getCommunicationmoduleport());
			o = new ObjectOutputStream(s.getOutputStream());
			o.writeObject(mReturn);

			o.close();
			s.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
