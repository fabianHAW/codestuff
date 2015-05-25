package tcp_basics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import util.Werkzeug;
import accessor_two.ClassOneImplBase;
import accessor_two.SomeException112;
import accessor_two.SomeException304;
import mware_lib.CommunicationModule;
import mware_lib.NameService;
import mware_lib.NameServiceRequest;
import mware_lib.ObjectBroker;

/**
 * Refresher zur TCP-Programmierung (Clientseite)
 *
 */
public class Client {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		//accessor_two_test();
		accessor_one_test();
	}
	
	public static void accessor_two_test(){
		System.out.println("start client");
		CommunicationModule.setCommunicatiomoduleport(50003);
		System.out.println("client: set communicationmoduleport");
		ObjectBroker objBroker = ObjectBroker.init("hildes-stube", 50000, true);
		System.out.println("client: got objBroker");
		NameService nameSvc = objBroker.getNameService();
		System.out.println("client: got NameServiceProxy");
		Object rawObjRef = nameSvc.resolve("test");
		System.out.println(rawObjRef);
		System.out.println("client: got RemoteObjectRef");
		ClassOneImplBase remoteObj = ClassOneImplBase.narrowCast(rawObjRef);
		System.out.println("client: got new proxy");
				
		try {
			System.out.println("client: call remote method");
			double d1 = remoteObj.methodOne("test", 3.2);
			System.out.println(d1);
			double d2 = remoteObj.methodTwo("beer", 2.3);
			System.out.println(d2);
		} catch (SomeException112 e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SomeException304 e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		objBroker.shutDown();
		System.out.println("end client");
		
		
//		Socket mySock;
//		BufferedReader in;
//		OutputStream out;		
//		
//		// Verbindung aufbauaen
//		mySock = new Socket("localhost", 14001);
//		
//		// I/O-Kanäle der Socket
//		in = new BufferedReader(new InputStreamReader(mySock.getInputStream()));
//		out = mySock.getOutputStream();
//		
//		// Kommunikation
//		out.write(("Knock, knock!\n").getBytes());
//		System.out.println(in.readLine());
//		
//		// Verbindung schliessen
//		in.close();
//		out.close();
//		mySock.close();
		

		//test sending simple object via socket
//		Socket mySock;
//		mySock = new Socket("localhost", 14001);
//		ObjectOutputStream output = new ObjectOutputStream(mySock.getOutputStream());
//		output.writeObject(new NameServiceRequest("rebind", "test", null));
//		
//		output.close();
//		mySock.close();
	}
	
	public static void accessor_one_test(){
		System.out.println("start client");
		CommunicationModule.setCommunicatiomoduleport(50003);
		System.out.println("client: set communicationmoduleport");
		String host = null;
		try {
			host = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ObjectBroker objBroker = ObjectBroker.init(host, 50000, true);
		System.out.println("client: got objBroker");
		NameService nameSvc = objBroker.getNameService();
		System.out.println("client: got NameServiceProxy");
		Object rawObjRef = nameSvc.resolve("test");
		System.out.println(rawObjRef);
		System.out.println("client: got RemoteObjectRef");
		accessor_one.ClassOneImplBase remoteObj = accessor_one.ClassOneImplBase.narrowCast(rawObjRef);
		System.out.println("client: got new proxy");
				
		System.out.println("client: call remote method");
		String s1 = "false";
		try {
			s1 = remoteObj.methodOne("test", 3);
		} catch (accessor_one.SomeException112 e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(s1);
		
		objBroker.shutDown();
		System.out.println("end client");
	}

}
