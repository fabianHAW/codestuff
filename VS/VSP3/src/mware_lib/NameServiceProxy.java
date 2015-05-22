package mware_lib;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class NameServiceProxy extends NameService {

	Socket socket;
	ObjectInputStream input;
	ObjectOutputStream output;
	private String serviceHost;
	private int port;

	public NameServiceProxy(String h, int p) {
		this.serviceHost = h;
		this.port = p;

		try {
			this.socket = new Socket(this.serviceHost, this.port);
			this.input = new ObjectInputStream(socket.getInputStream());
			this.output = new ObjectOutputStream(socket.getOutputStream());
		} catch (UnknownHostException e) {
			System.out.println("Host is unknown.");
		} catch (IOException e) {
			System.out.println("Can't open Input-/Outputstreams");
		}
	}

	@Override
	public void rebind(Object servant, String name) {
		
		RemoteObjectRef rof = ReferenceModule.createNewRemoteRef(servant);
		
		NameServiceRequest n = new NameServiceRequest("rebind", name, rof);
		try {
			this.output.writeObject(n);
		} catch (IOException e) {
			System.out.println("Can't send Request (rebind) to NameService!");
		}
	}

	@Override
	public Object resolve(String name) {
		Object o = null;
		NameServiceRequest n = new NameServiceRequest("resolve", name, null);
		try {
			this.output.writeObject(n);
			o = this.input.readObject();
		} catch (IOException e) {
			System.out.println("Can't send Request (resolve) to NameService!");
		} catch (ClassNotFoundException e) {
			System.out.println("Can't get Object of NameService");
		}
		return o;
	}

}
