package accessor_two;

import java.net.InetAddress;

import mware_lib.MessageADT;
import mware_lib.ObjectAdapter;

public class RequestDemultiplexer implements mware_lib.RequestDemultiplexer {

	private ObjectAdapter o;
	 
	public RequestDemultiplexer() {
		this.o = new ObjectAdapterAT();
	}

	@Override
	public void pass(MessageADT m, InetAddress komModAddr) {
		o.initSkeleton(m, komModAddr);		
	}

}
