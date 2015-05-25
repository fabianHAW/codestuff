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
		CommunicationModule.debugPrint(this.getClass(), "initialized");
		this.rof = rof;
	}

	public double methodOne(String param1, double param2)
			throws SomeException112 {
		MessageADT m = prepareMessageAndWaitForReply(param1, param2,
				"methodOne");

		List<Exception> exceptionList = m.getExceptionList();
		if (exceptionList != null && exceptionList.size() != 0) {
			for (Exception item : exceptionList) {
				CommunicationModule.debugPrint(this.getClass(), "exception <"
						+ item.getClass().getName() + "> found in MessageADT");
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
		if (exceptionList != null && exceptionList.size() != 0) {
			for (Exception item : exceptionList) {
				if (item instanceof SomeException112) {
					CommunicationModule.debugPrint(this.getClass(),
							"exception <" + item.getClass().getName()
									+ "> found in MessageADT");
					throw (SomeException112) item;
				} else if (item instanceof SomeException304) {
					CommunicationModule.debugPrint(this.getClass(),
							"exception <" + item.getClass().getName()
									+ "> found in MessageADT");
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

		MessageADT m = new MessageADT(CommunicationModule.getLocalHost(),
				CommunicationModule.messageIDCounter(), mName,
				ClassOneImplBase.REQUEST, this.rof, null, values, null);

		CommunicationModule.debugPrint(this.getClass(),
				"new MessageADT created");
		
		CommunicationModuleThread cT = CommunicationModule
				.getNewCommunicationThread(m);
		
		CommunicationModule.debugPrint(this.getClass(),
				"new CommunicationModuleThread created");

		synchronized (cT) {
			try {
				CommunicationModule.debugPrint(this.getClass(),
						"waiting for CommunicationModuleThread");
				cT.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return cT.getReceivedMessage();
	}

}
