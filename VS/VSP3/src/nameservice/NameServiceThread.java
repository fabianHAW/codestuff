package nameservice;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Behandelt den NameserviceRequest.
 * 
 * @author Francis
 * 
 */
public class NameServiceThread extends Thread {

	private String[] request;
	private Socket socket;

	// public NameServiceThread(NameServiceRequest r, Socket s) {
	// System.out.println(this.getClass() + "initialized");
	// request = r;
	// this.socket = s;
	//
	// }

	public NameServiceThread(String[] r, Socket s) {
		System.out.println(this.getClass() + "initialized");
		request = r;
		this.socket = s;

	}

	/**
	 * Entscheidet auf Grundlage des Typs des NameserviceRequest, ob ein rebind
	 * oder ein resolve angefordert wurde, und ruft die entsprechende Methode
	 * auf.
	 */
	public void run() {
		if (request[0].equals("rebind")) {
			System.out.println(this.getClass() + "call rebind");
			RemoteObjectRef rof = newRemoteRef(request);
			rebind(rof, request[1]);
		} else if (request[0].equals("resolve")) {
			System.out.println(this.getClass() + "call resolve");
			RemoteObjectRef o = resolve(request[1]);
			sendObject(request, o);
		}

		// String type = request.getRequestType().toLowerCase();
		// if (type.equals("rebind")) {
		// System.out.println(this.getClass() + "call rebind");
		// rebind(request.getObjectRef(), request.getServiceName());
		// } else if (type.equals("resolve")) {
		// System.out.println(this.getClass() + "call resolve");
		// Object o = resolve(request.getServiceName());
		// sendObject(type, request.getServiceName(), o);
		// }
	}

	/**
	 * Bei einem resolve wird das gesuchte Objekt in einem NameServiceRequest
	 * zur�ckgeliefert.
	 * 
	 * @param type
	 *            Der Typ der Nachricht.
	 * @param name
	 *            Der Name des Objekts.
	 * @param o
	 *            Die Objektreferenz.
	 */
	private void sendObject(String[] message, RemoteObjectRef o) {
		String[] request = null;
		if (o == null) {
			request = new String[] { "resolve", message[1], null, null, null,
					null };
		} else {
			request = new String[] { "resolve", message[1],
					o.getInetAddress().getHostName(),
					String.valueOf(o.getPort()), String.valueOf(o.getTime()),
					String.valueOf(o.getObjectNumber()) };
		}
		try {
			this.socket = new Socket(InetAddress.getByName(message[2]),
					Integer.valueOf(message[3]));
			ObjectOutputStream out = new ObjectOutputStream(
					this.socket.getOutputStream());
			out.writeObject(request);

			System.out.println(this.getClass() + "send request back to client");

			out.close();
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static RemoteObjectRef newRemoteRef(String[] message) {
		try {
			return new RemoteObjectRef(InetAddress.getByName(message[2]),
					Integer.valueOf(message[3]), Long.valueOf(message[4]),
					Integer.valueOf(message[5]));
		} catch (NumberFormatException | UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Speichert ein Objektreferenz unter dem Namen name ab. Eine evtl.
	 * vorhandene andere Objektreferenz wird �berschrieben.
	 * 
	 * @param servant
	 *            Die Objektreferenz.
	 * @param name
	 *            Der Name des Service der hinter der Objektreferenz steht.
	 */
	public void rebind(RemoteObjectRef servant, String name) {
		if (servant != null)
			NameService.addService(name, servant);
	}

	/**
	 * Liefert die Objektreferenz die dem Namen zugeh�rig ist.
	 * 
	 * @param name
	 *            Der Name des Service der hinter der Objektreferenz steht.
	 * @return ref Die Objektreferenz.
	 */
	public RemoteObjectRef resolve(String name) {
		return NameService.getService(name);
	}
}
