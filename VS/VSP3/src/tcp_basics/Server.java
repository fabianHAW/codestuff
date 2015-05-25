package tcp_basics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.sun.corba.se.pept.encoding.InputObject;

import accessor_two.ClassOneAT;
import mware_lib.CommunicationModule;
import mware_lib.NameService;
import mware_lib.NameServiceRequest;
import mware_lib.ObjectBroker;

/**
 * Refresher zur TCP-Programmierung (Serverseite)
 *
 */
public class Server {
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("start server");
		ClassOneAT servant = new ClassOneAT();
		CommunicationModule.setCommunicatiomoduleport(50001);
		System.out.println("server: set communicationmoduleport");
		ObjectBroker objBroker = ObjectBroker.init("hildes-stube", 50000, true);
		System.out.println("server: got objBroker");
		NameService nameSvc = objBroker.getNameService();
		System.out.println("server: got NameServiceProxy");
		nameSvc.rebind(servant, "test");
		System.out.println("server: rebinded new servant");
		
		try {
			System.out.println("server: sleeping");
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		objBroker.shutDown();
		System.out.println("end server");
		
//		ServerSocket mySvrSocket;
//		BufferedReader in;
//		OutputStream out;		
//		
//		mySvrSocket = new ServerSocket(14001);		
//		
//		// Auf Verbindungsanfrage warten.
//		Socket mySock = mySvrSocket.accept(); // -> Socket fuer die eigentliche Verbindung
//		
//		// I/O-Kanäle der Socket
//		in = new BufferedReader(new InputStreamReader(mySock.getInputStream()));
//		out = mySock.getOutputStream();
//		
//		// Kommunikation
//		System.out.println(in.readLine());
//		out.write(("Who's there?\n").getBytes());
//		
//		// Verbindung schliessen
//		in.close();
//		out.close();
//		mySock.close();
//		
//		// Server runterfahren
//		mySvrSocket.close();
		
		
		//test sending simple object via socket
//		ServerSocket mySvrSocket;
//		mySvrSocket = new ServerSocket(14001);	
//		Socket mySock = mySvrSocket.accept();
//		ObjectInputStream input = new ObjectInputStream(mySock.getInputStream());
//		//ObjectOutputStream output = new ObjectOutputStream(mySock.getOutputStream());
//		try {
//			NameServiceRequest n = (NameServiceRequest) input.readObject();
//			System.out.println(n.getRequestType() + " " + n.getServiceName());
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		input.close();
//		mySock.close();
//		mySvrSocket.close();
		
	}
}
