package accessor_two;

import java.net.InetAddress;

import mware_lib.MessageADT;
import mware_lib.ObjectAdapter;

public class ObjectAdapterAT implements ObjectAdapter{

	public ObjectAdapterAT() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initSkeleton(MessageADT m, InetAddress komModAddr) {		
		//vorher noch die objektid rausholen und entscheiden welches skeleton
		SkeletonOneAT s = new SkeletonOneAT(m, komModAddr);
		s.start();
		
	}

}
