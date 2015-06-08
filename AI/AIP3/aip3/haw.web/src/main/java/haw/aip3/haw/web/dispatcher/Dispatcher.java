package haw.aip3.haw.web.dispatcher;

import haw.aip3.haw.web.boot.RequestADT;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.springframework.boot.CommandLineRunner;

public class Dispatcher implements CommandLineRunner{

	private Monitor monitor;
	private ServerSocket serversocket;
	private ObjectInputStream input;
	private static final int LISTENPORT = 50001;
	private boolean isAlive;
	
	public Dispatcher() {
		this.monitor = new Monitor();
		this.monitor.start();
		try {
			this.serversocket = new ServerSocket(LISTENPORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.isAlive = true;
	}

	@Override
	public void run(String... arg0) throws Exception {
		Socket s = null;
		while(this.isAlive){
			s = this.serversocket.accept();
			this.input = new ObjectInputStream(s.getInputStream());
			RequestADT request = (RequestADT) this.input.readObject();
		}
	}
	
	
	public void dispatcherShutdown(){
		this.isAlive = false;
		try {
			this.serversocket.close();
		} catch (IOException e) {
			System.out.println(this.getClass() + ": dispatcher closed");
		}
	}

	
	private class DispatcherThread{
		public DispatcherThread(){
			
		}
	}

}
