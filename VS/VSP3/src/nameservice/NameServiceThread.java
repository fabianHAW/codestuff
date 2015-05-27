package nameservice;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import mware_lib.NameServiceRequest;
import mware_lib.RemoteObjectRef;

/**
 * Behandelt den NameserviceRequest.
 * 
 * @author Francis
 *
 */
public class NameServiceThread extends Thread {

	private NameServiceRequest request;
	private Socket socket;

	public NameServiceThread(NameServiceRequest r, Socket s) {
		System.out.println(this.getClass() + "initialized");
		request = r;
		this.socket = s;

	}

	/**
	 * Entscheidet auf Grundlage  des Typs des NameserviceRequest, ob ein rebind oder ein
	 * resolve angefordert wurde, und ruft die entsprechende Methode auf.
	 */
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

	/**
	 * Bei einem resolve wird das gesuchte Objekt in einem NameServiceRequest zurückgeliefert.
	 * 
	 * @param type Der Typ der Nachricht.
	 * @param name Der Name des Objekts.
	 * @param o Die Objektreferenz.
	 */
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

	/**
	 * Speichert ein Objektreferenz unter dem Namen name ab.
	 * Eine evtl. vorhandene andere Objektreferenz wird überschrieben.
	 * 
	 * @param servant Die Objektreferenz.
	 * @param name Der Name des Service der hinter der Objektreferenz steht.
	 */
	public void rebind(Object servant, String name) {
		// TODO Auto-generated method stub
		NameService.addService(name, (RemoteObjectRef) servant);
	}

	/**
	 * Liefert die Objektreferenz die dem Namen zugehörig ist.
	 * 
	 * @param name Der Name des Service der hinter der Objektreferenz steht.
	 * @return ref Die Objektreferenz.
	 */
	public Object resolve(String name) {
		// TODO Auto-generated method stub
		return NameService.getService(name);
	}
}
