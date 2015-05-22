package nameservice;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import mware_lib.MessageADT;

public class NameService extends mware_lib.NameService {

	private static Integer port;
	private static ServerSocket serverSocket;
	private static ObjectInputStream input;
	
	public static void main(String[] args) {

		if (args.length != 1) {
			usage();
			System.exit(-1);
		}
		port = Integer.valueOf(args[0]);
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listen();
	}
	
	public static void listen(){
		Socket socket = null;
		try {
			socket = serverSocket.accept();
			input = new ObjectInputStream(socket.getInputStream());
			NameServiceRequest n = (NameServiceRequest) input.readObject();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	// - Schnittstelle zum Namensdienst -

	// Meldet ein Objekt (servant) beim Namensdienst an.
	// Eine eventuell schon vorhandene Objektreferenz gleichen Namens
	// soll überschrieben werden.
	@Override
	public void rebind(Object servant, String name) {
		// TODO Auto-generated method stub

	}

	// Liefert eine generische Objektreferenz zu einem Namen. (vgl. unten)
	@Override
	public Object resolve(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private static void usage(){
		System.out.println("***forgot portnumber! try this***\n"
				+ "java nameservice/NameServiceImpl portnumber");
	}

}
