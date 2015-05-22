package accessor_one;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;

import mware_lib.MessageADT;
import mware_lib.ObjectAdapter;

public class RequestDemultiplexer {

	ArrayList<ObjectAdapter> adapter;
	InetAddress komModAddr;
	
	public RequestDemultiplexer(InetAddress komModAddr) {
		// TODO Auto-generated constructor stub
		this.komModAddr = komModAddr;
		adapter = new ArrayList<ObjectAdapter>(
				Arrays.asList(
						new ObjectAdapterAO1()
						));
	}
	
	public void pass(MessageADT m){
		boolean found = false;
		for(ObjectAdapter o : adapter){
			for(Integer id : o.getSkeletonIDs()){
				if(id.intValue() == m.getObjectRef().getObjectNumber()){
					o.initSkeleton(m, komModAddr);
					found = true;
					break;
				}
			}
			if(found){
				break;
			}
		}
	}

}
