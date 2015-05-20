package mware_lib;

import java.net.InetAddress;

public interface RequestDemultiplexer {
	
	public void pass(MessageADT m, InetAddress komModAddr);

}
