package accessor_two;

import java.net.InetAddress;

import mware_lib.MessageADT;
import mware_lib.Skeleton;

/**
 * 
 * @author Francis und Fabian
 *
 * Stellt das Skeleton dar -> Objekte seitens des Servers greifen darauf zu
 */

public class SkeletonOneAT extends Thread implements Skeleton{

	public void run(){
		
	}
	
	public MessageADT invoke() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public SkeletonOneAT(MessageADT m, InetAddress inetAddress){
		
	}

}
