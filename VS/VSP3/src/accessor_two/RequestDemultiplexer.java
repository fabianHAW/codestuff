package accessor_two;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mware_lib.MessageADT;
import mware_lib.ObjectAdapter;

public class RequestDemultiplexer implements mware_lib.RequestDemultiplexer {

	private List<ObjectAdapter> adapter;

	public RequestDemultiplexer() {
		this.adapter = new ArrayList<ObjectAdapter>(
				Arrays.asList(new ObjectAdapterAT()));
	}

	// Inetaddress nicht noetig, da lokal gearbeitet wird.
	// ggf. port uebergeben
	@Override
	public void pass(MessageADT m, InetAddress komModAddr) {
		int objectNumber = m.getObjectRef().getObjectNumber();
		for (ObjectAdapter item : this.adapter) {
			if(item.getSkeletonIDs().contains(objectNumber)){
				item.initSkeleton(m, komModAddr);
			}
		}
	}
}
