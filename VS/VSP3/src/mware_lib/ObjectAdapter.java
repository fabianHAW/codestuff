package mware_lib;

import java.net.InetAddress;
import java.util.ArrayList;

public interface ObjectAdapter {
	
	public void initSkeleton(MessageADT m, InetAddress komModAddr);
	public ArrayList<Integer> getSkeletonIDs();

}
