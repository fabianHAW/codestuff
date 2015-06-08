package haw.aip3.haw.web.dispatcher;

import haw.aip3.haw.web.boot.MessageADT;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Monitor extends Thread {

	private ServerSocket serversocket;
	private static final int LISTENPORT = 50000;
	private ObjectInputStream input;
	private boolean isAlive;
	Set<Socket> serverSet;

	public Monitor() {
		try {
			this.serversocket = new ServerSocket(LISTENPORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.isAlive = true;
		this.serverSet = new HashSet<Socket>();
	}

	public void run() {
		System.out.println(Monitor.class + " running.");
		Socket s = null;
		while (this.isAlive) {
			try {
				s = this.serversocket.accept();
				this.input = new ObjectInputStream(s.getInputStream());

				MessageADT request = (MessageADT) this.input.readObject();
				handleRequest(request);

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		try {
			if (this.input != null)
				this.input.close();
			if (s != null)
				s.close();
			if (this.serversocket != null)
				this.serversocket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleRequest(MessageADT request) {
		if (request.isAlive()) {
			this.serverSet.add(request.getSocket());
		} else {
			this.serverSet.remove(request.getSocket());
		}
	}

	public Socket getAliveServer() {

		if (this.serverSet.size() > 0) {
			for (Socket item : this.serverSet) {
				return item;
			}
		}
		return null;
	}

	public void monitorShutdown() {
		this.isAlive = false;
		try {
			this.serversocket.close();
		} catch (IOException e) {
			System.out.println(this.getClass() + ": closed serversocket");
		}
		;
	}
}
