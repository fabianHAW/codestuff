package nameservice;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import mware_lib.NameServiceRequest;
import mware_lib.RemoteObjectRef;

public class NameServiceThread extends Thread {

	private NameServiceRequest request;
	
	public NameServiceThread(NameServiceRequest r) {
		// TODO Auto-generated constructor stub
		request = r;
		
	}
	
	public void run(){
		String type = request.getRequestType().toLowerCase();
		if(type.equals("rebind")){
			rebind(request.getObjectRef(), request.getServiceName());
		}else if(type.equals("resolve")){
			Object o = resolve(request.getServiceName());
			sendObject(o);
		}
	}
	

	private void sendObject(Object o) {
		// TODO Auto-generated method stub
		Socket socket = null;
		try {
			socket = new Socket(NameService.getLocalHost(), NameService.getPort());
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(o);
			out.close();
			socket.close();
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
	
	private static void usage(){
		System.out.println("***forgot portnumber! try this***\n"
				+ "java nameservice/NameServiceImpl portnumber");
	}


}
