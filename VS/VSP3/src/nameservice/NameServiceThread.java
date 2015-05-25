package nameservice;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import mware_lib.NameServiceRequest;
import mware_lib.RemoteObjectRef;

public class NameServiceThread extends Thread {

	private NameServiceRequest request;
	private Socket socket;

	public NameServiceThread(NameServiceRequest r, Socket s) {
		System.out.println(this.getClass() + "initialized");
		request = r;
		this.socket = s;

	}

	public void run() {
		String type = request.getRequestType().toLowerCase();
		if (type.equals("rebind")) {
			System.out.println(this.getClass() + "call rebind");
			rebind(request.getObjectRef(), request.getServiceName());
		} else if (type.equals("resolve")) {
			System.out.println(this.getClass() + "call resolve");
			Object o = resolve(request.getServiceName());
			sendObject(type, request.getServiceName(), o);
		}
	}

	private void sendObject(String type, String name, Object o) {
		NameServiceRequest request = new NameServiceRequest(type, name,
				(RemoteObjectRef) o);
		try {
			// TODO fuer lokale zwecke hier der listenport = 50004 und der
			// lokale
			// host. verteilt wird der socket genommen der dem thread uebergeben
			// wurde -> folgende zeile loeschen
			this.socket = new Socket(NameService.getLocalHost(), 50004);

			ObjectOutputStream out = new ObjectOutputStream(
					this.socket.getOutputStream());
			out.writeObject(request);

			System.out.println(this.getClass() + "send request back to client");

			out.close();
			this.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// - Schnittstelle zum Namensdienst -

	// Meldet ein Objekt (servant) beim Namensdienst an.
	// Eine eventuell schon vorhandene Objektreferenz gleichen Namens
	// soll überschrieben werden.
	public void rebind(Object servant, String name) {
		// TODO Auto-generated method stub
		NameService.addService(name, (RemoteObjectRef) servant);
	}

	// Liefert eine generische Objektreferenz zu einem Namen. (vgl. unten)
	public Object resolve(String name) {
		// TODO Auto-generated method stub
		return NameService.getService(name);
	}
}
