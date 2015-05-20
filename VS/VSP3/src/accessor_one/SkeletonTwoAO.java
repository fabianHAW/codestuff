package accessor_one;


import java.net.InetAddress;

import mware_lib.MessageADT;
import mware_lib.Skeleton;

/**
 * 
 * @author Francis und Fabian
 *
 * Stellt das Skeleton dar -> Objekte seitens des Servers greifen darauf zu
 */

public class SkeletonTwoAO extends Thread implements Skeleton{

	public void run(){
		
	}
	
	public MessageADT invoke() {
		return null;
		// TODO Auto-generated method stub
		
	}
	
	public SkeletonTwoAO(MessageADT m, InetAddress komModAddr){
		
	}

}
