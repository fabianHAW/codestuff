package accessor_one;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;

import mware_lib.MessageADT;
import mware_lib.ObjectAdapter;

public class ObjectAdapterAO1 implements ObjectAdapter {

	public ObjectAdapterAO1() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * {@link Deprecated}
	 * @param m
	 */
	public void passMessage(MessageADT m){
	
	}

	@Override
	public void initSkeleton(MessageADT m, InetAddress komModAddr) {
		// TODO Auto-generated method stub
		if(SkeletonOneAO.ID == m.getObjectRef().getObjectNumber()){
			SkeletonOneAO sk = new SkeletonOneAO(m, komModAddr);
			sk.run();	
		}else if(SkeletonTwoAO.ID == m.getObjectRef().getObjectNumber()){
			SkeletonTwoAO sk = new SkeletonTwoAO(m, komModAddr);
			sk.run();
		}
	}
	
	public ArrayList<Integer> getSkeletonIDs(){
		return new ArrayList<Integer>(Arrays.asList(SkeletonOneAO.ID, SkeletonTwoAO.ID));
	}

}
