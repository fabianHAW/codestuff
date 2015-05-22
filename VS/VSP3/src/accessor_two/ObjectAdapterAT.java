package accessor_two;

import java.util.ArrayList;
import java.util.Arrays;

import mware_lib.MessageADT;
import mware_lib.ObjectAdapter;

public class ObjectAdapterAT implements ObjectAdapter{

	public ObjectAdapterAT() {
	}

	@Override
	public void initSkeleton(MessageADT m) {		
		int objectNumber = m.getObjectRef().getObjectNumber();
		switch(objectNumber){
		case SkeletonOneAT.ID:
			SkeletonOneAT s = new SkeletonOneAT(m);
			s.start();
			break;
		default:
			System.out.println("can't find right Skeleton!");
			break;		
		}
	}

	@Override
	public ArrayList<Integer> getSkeletonIDs() {
		return new ArrayList<Integer>(Arrays.asList(SkeletonOneAT.ID));
	}

}
