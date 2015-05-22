package nameservice;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import mware_lib.MessageADT;
import mware_lib.NameServiceRequest;
import mware_lib.RemoteObjectRef;

public class NameService  {

	private static Integer port;
	private static ServerSocket serverSocket;
	private static ObjectInputStream input;
	private static HashMap<String, RemoteObjectRef> referenceObjects;
	
	public void start(int port){
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listen();
	}
	
	public  void listen(){
		Socket socket = null;
		NameServiceRequest request = null;
		try {
			socket = serverSocket.accept();
			input = new ObjectInputStream(socket.getInputStream());
			request = (NameServiceRequest) input.readObject();
			NameServiceThread t = new NameServiceThread(request);
			t.start();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void addService(String name, RemoteObjectRef ref){
		referenceObjects.put(name, ref);
	}
	
	public static synchronized RemoteObjectRef getService(String name){
		return referenceObjects.get(name);
	}
	
	public static int getPort(){
		return port;
	}
	
	public static synchronized InetAddress getLocalHost(){
		try {
			return InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
