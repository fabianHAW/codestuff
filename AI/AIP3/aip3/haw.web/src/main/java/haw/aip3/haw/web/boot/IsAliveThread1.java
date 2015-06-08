package haw.aip3.haw.web.boot;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class IsAliveThread1 extends Thread{

	private Server1 server;
	private ObjectOutputStream ooutput;
	private Socket socket;
	private OutputStream output;
	
	public IsAliveThread1(Server1 server) {
		// TODO Auto-generated constructor stub
		this.server = server;
		try {
			socket = new Socket("127.0.0.1", 50000);
			output = socket.getOutputStream();
			ooutput = new ObjectOutputStream(output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run(){
		ArrayList<String> aliveMessage = new ArrayList<String>();
		while(server.isAlive()){
			
			aliveMessage.add("server1");
			aliveMessage.add("true");
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Sleep in " + IsAliveThread2.class + " interrupted");
			}
		}
		
		aliveMessage.add("server1");
		aliveMessage.add("false");
		
		try {
			ooutput.writeObject(aliveMessage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error send Message to Monitor in " + IsAliveThread2.class + " interrupted");
			e.printStackTrace();
		}
	}


}
