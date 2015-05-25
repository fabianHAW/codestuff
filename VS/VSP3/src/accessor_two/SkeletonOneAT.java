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
		CommunicationModule.debugPrint(this.getClass(), "initialized");
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

		CommunicationModule.debugPrint(this.getClass(),
				"got servant from ReferenceModule");

		if (method.equals("methodOne")) {
			try {
				CommunicationModule.debugPrint(this.getClass(),
						"call methodOne of skeleton");
				returnVal = String.valueOf(
						((ClassOneAT) s).methodOne(param1, param2)).getBytes();
			} catch (SomeException112 e) {
				le.add(e);
			}
		} else if (method.equals("methodTwo")) {
			try {
				CommunicationModule.debugPrint(this.getClass(),
						"call methodTwo of skeleton");
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
			CommunicationModule.debugPrint(this.getClass(),
					"new MessageADT generated with exception");
			return new MessageADT(this.m.getiNetAdrress(),
					this.m.getMessageID(), this.m.getMethodName(),
					ClassOneImplBase.REPLY, this.m.getObjectRef(), returnVal,
					this.m.getArguments(), le);
		}
		CommunicationModule.debugPrint(this.getClass(),
				"new MessageADT generated without exception");
		return new MessageADT(this.m.getiNetAdrress(), this.m.getMessageID(),
				this.m.getMethodName(), ClassOneImplBase.REPLY,
				this.m.getObjectRef(), returnVal, this.m.getArguments(),
				this.m.getExceptionList());
	}

	public void sendBack(MessageADT mReturn) {
		Socket s = null;
		ObjectOutputStream o = null;
		try {
			// TODO zum lokalen testen hier der port des kommunikationsmoduls
			// des clients
			s = new Socket(mReturn.getiNetAdrress(), 50003);
			// s = new Socket(mReturn.getiNetAdrress(),
			// CommunicationModule.getCommunicationmoduleport());
			o = new ObjectOutputStream(s.getOutputStream());

			o.writeObject(mReturn);
			CommunicationModule.debugPrint(this.getClass(),
					"successful send back");

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
