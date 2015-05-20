package mware_lib;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 
 * @author Francis und Fabian
 *
 */

public class CommunicationModule extends Thread{
	
	private ServerSocket serverSocket;
	private ObjectInputStream input;
	private static final String COMMUNICATIONMODULEHOST = "localhost";
	private static final int COMMUNICATIONMODULEPORT = 50001;
	
	public CommunicationModule(){
		try {
			this.serverSocket = new ServerSocket(COMMUNICATIONMODULEPORT);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	public void run(){
		while(!Thread.currentThread().isInterrupted()){
			
			try {
				Socket socket = this.serverSocket.accept();
				this.input = new ObjectInputStream(socket.getInputStream());
				MessageADT m = (MessageADT) this.input.readObject();
				
				//Reply
				if(m.getMessageType() != 0){
					//InetAddress von extern
					if(!socket.getInetAddress().getHostName().equals(COMMUNICATIONMODULEHOST)){
						handleServerReply(m);
					}
					else{
						sendReplyToClient(m);
					}
				}
				//Request
				else{
					//InetAdress von intern
					if(socket.getInetAddress().getHostName().equals(COMMUNICATIONMODULEHOST)){
						sendRequestToServer(m);
					}
					else{
						handleClientRequest(m);
					}
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void handleServerReply(MessageADT m){
		//port vom proxy fehlt
//		Socket s = new Socket("localhost");
//		ObjectOutputStream o = new ObjectOutputStream(s.getOutputStream());
//		o.writeObject(m);
//		o.close();
//		s.close();
	}
	
	private void sendReplyToClient(MessageADT mReturn){
		try {
			Socket s = new Socket(mReturn.getiNetAdrress(), COMMUNICATIONMODULEPORT);
			ObjectOutputStream o = new ObjectOutputStream(s.getOutputStream());
			o.writeObject(mReturn);
			o.close();
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void sendRequestToServer(MessageADT m){
		try {
			Socket s = new Socket(m.getObjectRef().getInetAddress(), m.getObjectRef().getPort());
			ObjectOutputStream o = new ObjectOutputStream(s.getOutputStream());
			o.writeObject(m);
			o.close();
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void handleClientRequest(MessageADT m){
		//korrekten RequestDemultiplexer auswählen
	}
	
	public void stopCommunicationModule(){
		this.interrupt();
	}
	
	public static String getCommunicationmodulehost() {
		return COMMUNICATIONMODULEHOST;
	}
	public static int getCommunicationmoduleport() {
		return COMMUNICATIONMODULEPORT;
	}
	
	

}
