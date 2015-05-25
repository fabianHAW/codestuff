package test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import mware_lib.CommunicationModule;
import mware_lib.NameService;
import mware_lib.ObjectBroker;
import accessor_one.ClassOneAO;

public class ServerStart extends Thread{

	public ServerStart() {
		// TODO Auto-generated constructor stub
	}
	
	public void run(){
		accessor_one_test();
	}
	
	public void accessor_one_test(){

		ClassOneAO servant = new ClassOneAO();
		CommunicationModule.setCommunicatiomoduleport(50001);
	
		String host = null;
		try {
			host = InetAddress.getLocalHost().getCanonicalHostName();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("host: " + host);
		ObjectBroker objBroker = ObjectBroker.init(host, 50000, true);

		NameService nameSvc = objBroker.getNameService();
	
		nameSvc.rebind(servant, "test");

		
		try {
			System.out.println("server: sleeping");
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		objBroker.shutDown();
		System.out.println("end server");
	}

}
