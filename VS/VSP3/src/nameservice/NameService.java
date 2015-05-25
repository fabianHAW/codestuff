package nameservice;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import mware_lib.CommunicationModule;
import mware_lib.NameServiceRequest;
import mware_lib.RemoteObjectRef;

public class NameService {

	private static Integer listenPort;
	private static ServerSocket serverSocket;
	private static ObjectInputStream input;
	private static HashMap<String, RemoteObjectRef> referenceObjects;

	public void start(int port) {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		referenceObjects = new HashMap<String, RemoteObjectRef>();
		listenPort = port;
		listen();
	}

	public void listen() {
		while (true) {
			Socket socket = null;
			NameServiceRequest request = null;
			try {
				System.out
						.println(this.getClass() + "waiting for new requests");

				socket = serverSocket.accept();
				input = new ObjectInputStream(socket.getInputStream());
				request = (NameServiceRequest) input.readObject();

				System.out.println(this.getClass()
						+ "start new thread to handle request");

				NameServiceThread t = new NameServiceThread(request, socket);
				t.start();
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void addService(String name, RemoteObjectRef ref) {
		referenceObjects.put(name, ref);
	}

	public static synchronized RemoteObjectRef getService(String name) {
		return referenceObjects.get(name);
	}

	public static int getListenPort() {
		return listenPort;
	}

	public static synchronized InetAddress getLocalHost() {
		try {
			return InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
