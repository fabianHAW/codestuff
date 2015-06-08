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
			socket = new Socket("127.0.0.1", 50010);
			output = socket.getOutputStream();
			ooutput = new ObjectOutputStream(output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run(){
		
		while(server.isAlive()){
			
			
			
			try {
				MessageADT m = new MessageADT();
				m.setIsAlive(true);
				m.setSocket(new Socket(server.getSocket().getInetAddress(), server.getSocket().getLocalPort()));
				ooutput.writeObject(m);
				Thread.sleep(10000);
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Sleep in " + IsAliveThread2.class + " interrupted");
			}
		}
		
		
		try {
			MessageADT m = new MessageADT();
			m.setIsAlive(false);
			m.setSocket(new Socket(server.getSocket().getInetAddress(), server.getSocket().getLocalPort()));
			ooutput.writeObject(m);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error send Message to Monitor in " + IsAliveThread2.class + " interrupted");
			e.printStackTrace();
		}
	}


}
