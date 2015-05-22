package accessor_two;

import java.util.ArrayList;
import java.util.List;

import mware_lib.CommunicationModule;
import mware_lib.CommunicationModuleThread;
import mware_lib.MessageADT;
import mware_lib.RemoteObjectRef;

/**
 * 
 * @author Francis und Fabian
 * 
 */

public class ClassOneImplBaseProxy extends ClassOneImplBase {

	private RemoteObjectRef rof;

	public ClassOneImplBaseProxy(RemoteObjectRef rof) {
		this.rof = rof;
	}

	public double methodOne(String param1, double param2)
			throws SomeException112 {
		MessageADT m = prepareMessageAndWaitForReply(param1, param2,
				"methodOne");

		List<Exception> exceptionList = m.getExceptionList();
		if (exceptionList.size() != 0) {
			for (Exception item : exceptionList) {
				throw (SomeException112) item;
			}
		}

		return Double.parseDouble(new String(m.getReturnVal()));
	}

	public double methodTwo(String param1, double param2)
			throws SomeException112, SomeException304 {
		MessageADT m = prepareMessageAndWaitForReply(param1, param2,
				"methodTwo");

		List<Exception> exceptionList = m.getExceptionList();
		if (exceptionList.size() != 0) {
			for (Exception item : exceptionList) {
				if (item instanceof SomeException112) {
					throw (SomeException112) item;
				} else if (item instanceof SomeException304) {
					throw (SomeException304) item;
				}
			}
		}

		return Double.parseDouble(new String(m.getReturnVal()));
	}

	private MessageADT prepareMessageAndWaitForReply(String param1,
			double param2, String mName) {
		List<byte[]> values = new ArrayList<byte[]>();
		values.add(param1.getBytes());
		values.add(String.valueOf(param2).getBytes());

		MessageADT m = new MessageADT(CommunicationModule.getLocalHost(), -1,
				mName, 0, this.rof, null, values, null);

		CommunicationModuleThread cT = CommunicationModule
				.getNewCommunicationThread(m);

		try {
			cT.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return cT.getReceivedMessage();
	}

}
