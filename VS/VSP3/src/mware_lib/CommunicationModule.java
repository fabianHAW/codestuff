package mware_lib;

import java.net.InetAddress;

/**
 * 
 * @author Francis und Fabian
 *
 * Stellt Request/Reply-Protokoll bereit und hat somit 
 * auch einen Socket bzw. Serversocket
 */

public class CommunicationModule {
	
	private static InetAddress inetAddress;
	
	public void sendReplyToClient(MessageADT m){
		
	}
	
	public static CommunicationModuleThread sendRequest(MessageADT m){
		return new CommunicationModuleThread(m);
	}
	
	public static InetAddress getInetAddress(){
		return inetAddress;
	}

}
