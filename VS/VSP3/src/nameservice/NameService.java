package nameservice;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import shared_types.NameServiceRequest;
import shared_types.RemoteObjectRef;


/**
 * Verweis zum Entwurf:
 * <Entwurfsdokument> : Implementierung der vorgegebenen Methoden in Nr. 3 (d) - accessor_one.
 * 
 * @author Francis u. Fabian.
 *
 */
public class NameService {

	public static Integer listenPort;
	private static ServerSocket serverSocket;
	private static ObjectInputStream input;
	private static HashMap<String, RemoteObjectRef> referenceObjects;

	/**
	 * Startet den Nameservice und l�sst ihn auf dem entsprechenden Port lauschen.
	 * 
	 * @param port Ein freier Port.
	 */
	public void start(int port) {

		DebugPrinter.debugPrint(this.getClass(), " initialize... ");
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		referenceObjects = new HashMap<String, RemoteObjectRef>();
		listenPort = port;
		listen();
		DebugPrinter.debugPrint(this.getClass(), " initialized! ");
	}

	/**
	 * L�sst den Nameservice auf dem Port lauschen.
	 * Wenn ein Objekt vom Typ NameserviceRequest auf dem Socket eintrifft, wird ein NameServiceThread
	 * gestartet, der sich um die Anfrage k�mmert. Anschlie�end wird wieder auf eintreffende Anfragen
	 * gewartet.
	 * 
	 */
	public void listen() {
		DebugPrinter.debugPrint(this.getClass(), " waiting for requests.");
		while (true) {
			Socket socket = null;
			NameServiceRequest request = null;
			try {
				

				socket = serverSocket.accept();
				input = new ObjectInputStream(socket.getInputStream());
				request = (NameServiceRequest) input.readObject();

				System.out.println(this.getClass()
						+ "start new thread to handle request");

				NameServiceThread t = new NameServiceThread(request, socket);
				t.start();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * F�gt dem Nameservice eine neue Objektreferenz ref unter dem Namen name hinzu.
	 * @param name Der Name des Service.
	 * @param ref Die Objektreferenz.
	 */
	public static void addService(String name, RemoteObjectRef ref) {
		referenceObjects.put(name, ref);
	}

	/**
	 * Liefert den Service, der unter dem Namen name abgespeichert ist.
	 * 
	 * @param name Der Name des Service.
	 * @return service Der Service mit dem Namen name.
	 */
	public static synchronized RemoteObjectRef getService(String name) {
		return referenceObjects.get(name);
	}

	/**
	 * Liefert den Port, auf dem der Nameservice lauscht.
	 * @return port der entsprechende Port.
	 */
	public static int getListenPort() {
		return listenPort;
	}

	/**
	 * Liefert den localhost des Nameservice.
	 * @return localhost Der localhost des Nameservice.
	 */
	public static synchronized InetAddress getLocalHost() {
		try {
			return InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
}
