package haw.aip3.haw.web.boot;

import java.net.Socket;

public class MessageADT {

	
	private Socket socket;
	private boolean isAlive;
	
	public MessageADT() {
		// TODO Auto-generated constructor stub
	}
	
	public Socket getSocket(){
		return socket;
	}
	
	public void setSocket(Socket s ){
		socket = s;
	}
	
	public boolean isAlive(){
		return isAlive;
	}
	
	
	

	
	

}
