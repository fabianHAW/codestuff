package mware_lib;

import java.util.ArrayList;

public interface ObjectAdapter {
	
	public void initSkeleton(MessageADT m);
	public ArrayList<Integer> getSkeletonIDs();

}
