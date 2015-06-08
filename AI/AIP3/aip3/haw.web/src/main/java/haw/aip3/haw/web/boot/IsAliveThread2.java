package haw.aip3.haw.web.boot;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class IsAliveThread2 extends Thread{

	private Server2 server;
	private ObjectOutputStream ooutput;
	private Socket socket;
	private OutputStream output;
	
	public IsAliveThread2(Server2 server) {
		// TODO Auto-generated constructor stub
		this.server = server;
		try {
			socket = new Socket("127.0.0.1", 50011);
			output = socket.getOutputStream();
			ooutput = new ObjectOutputStream(output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error send Message to Monitor in " + IsAliveThread2.class + " interrupted");
			e.printStackTrace();
		}
	}
	
	@Override
	public void run(){
		ArrayList<String> aliveMessage = new ArrayList<String>();
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
			m.setIsAlive(true);
			m.setSocket(new Socket(server.getSocket().getInetAddress(), server.getSocket().getLocalPort()));
			ooutput.writeObject(m);
			ooutput.writeObject(aliveMessage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}
