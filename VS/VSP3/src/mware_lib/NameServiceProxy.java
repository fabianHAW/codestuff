package mware_lib;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 
 * @author Fabian
 * 
 *         Ist der Stellvertreter des Namensdienstes. Dieser bekommt durch die
 *         Methodenaufrufe rebind/resolve die Aufforderung beim eigentlichen
 *         entfernten Namensdienst die jeweilige Anfrage zu stellen. Dies
 *         geschieht mittels Sockets.
 */
public class NameServiceProxy extends NameService {

	private ServerSocket serverSocket;
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private String serviceHost;
	private int servicePort;

	public NameServiceProxy(String serviceHost, int port) {
		this.serviceHost = serviceHost;
		this.servicePort = port;
		CommunicationModule.debugPrint(this.getClass(), "initialized");
	}

	@Override
	public void rebind(Object servant, String name) {
		if (servant != null) {
			/*
			 * vsp3_sequ_server_start: 3.2.1.1: neue entfernte Objekt-Referenz
			 * erzeugen 3.2.1.1.3: entfernte Objekt-Referenz erhalten
			 */
			RemoteObjectRef rof = ReferenceModule.createNewRemoteRef(servant);
			/*
			 * vsp3_sequ_server_start: 3.2.1.2: Neue Nachricht fuer Nameservice
			 * erzeugen
			 */

			String[] message = new String[] { "rebind", name,
					rof.getInetAddress().getHostName(),
					String.valueOf(rof.getPort()),
					String.valueOf(rof.getTime()),
					String.valueOf(rof.getObjectNumber()) };

			try {
				this.socket = new Socket(this.serviceHost, this.servicePort);
				this.output = new ObjectOutputStream(
						this.socket.getOutputStream());
				/*
				 * vsp3_sequ_server_start: 4: Nachricht an Nameservice senden
				 */
				this.output.writeObject(message);

				CommunicationModule.debugPrint(this.getClass(),
						"send request (rebind) to nameservice");

				this.output.close();
				this.socket.close();
			} catch (IOException e) {
				System.out.println(this.getClass()
						+ ": cannot send a request (rebind) to nameservice!");
			}
			CommunicationModule.debugPrint(this.getClass(), "new Servant: "
					+ servant + "with name: " + name + " rebinded");
		}
	}

	@Override
	public Object resolve(String name) {
		String[] message = null;
		String[] request = null;
		try {
			this.serverSocket = new ServerSocket(0);
			message = new String[] { "resolve", name,
					InetAddress.getLocalHost().getCanonicalHostName(),
					String.valueOf(this.serverSocket.getLocalPort()) };
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			this.socket = new Socket(this.serviceHost, this.servicePort);
			this.output = new ObjectOutputStream(this.socket.getOutputStream());
			CommunicationModule.debugPrint(this.getClass(),
					"send request (resolve) to nameservice");
			this.output.writeObject(message);

			this.socket = this.serverSocket.accept();
			this.input = new ObjectInputStream(this.socket.getInputStream());
			request = (String[]) this.input.readObject();

			this.input.close();
			this.output.close();
			this.socket.close();
			this.serverSocket.close();
		} catch (IOException e) {
			System.out.println(this.getClass()
					+ ": cannot send a request (resolve) to nameservice");
		} catch (ClassNotFoundException e) {
			System.out.println(this.getClass()
					+ ": cannot get object from nameservice");
		}

		CommunicationModule.debugPrint(this.getClass(), "Service: " + name
				+ " resolved");

		if (request[2] != null) {
			try {
				return new RemoteObjectRef(InetAddress.getByName(request[2]),
						Integer.valueOf(request[3]), Long.valueOf(request[4]),
						Integer.valueOf(request[5]));
			} catch (NumberFormatException | UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
