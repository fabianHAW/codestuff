package accessor_one;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;

import mware_lib.CommunicationModule;
import mware_lib.MessageADT;
import mware_lib.ObjectAdapter;

public class ObjectAdapterAO implements ObjectAdapter {

	public ObjectAdapterAO() {
		// TODO Auto-generated constructor stub
		CommunicationModule.debugPrint(this.getClass(), "inizialized");
	}

	@Override
	public void initSkeleton(MessageADT m) {
		// TODO Auto-generated method stub
		if(SkeletonOneAO.ID == m.getObjectRef().getObjectNumber()){
			SkeletonOneAO sk = new SkeletonOneAO(m);
			CommunicationModule.debugPrint(this.getClass(),
					"new skeletonOneAO created");
			sk.run();	
		}else if(SkeletonTwoAO.ID == m.getObjectRef().getObjectNumber()){
			SkeletonTwoAO sk = new SkeletonTwoAO(m);
			CommunicationModule.debugPrint(this.getClass(),
					"new skeletonTwoAO created");
			sk.run();
		}
	}
	
	public ArrayList<Integer> getSkeletonIDs(){
		return new ArrayList<Integer>(Arrays.asList(SkeletonOneAO.ID, SkeletonTwoAO.ID));
	}

}
