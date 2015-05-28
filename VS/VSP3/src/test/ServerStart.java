package test;


import java.net.InetAddress;
import java.net.UnknownHostException;

import mware_lib.NameService;
import mware_lib.ObjectBroker;
import accessor_one.ClassOneAO;
import accessor_one.ClassTwoAO;
import accessor_two.ClassOneAT;

public class ServerStart extends Thread{

	public ServerStart() {
		// TODO Auto-generated constructor stub
	}
	
	public void run(){
		accessor_one_test();
	}
	
	public void accessor_one_test(){

		ClassOneAO c1 = new ClassOneAO();
		ClassTwoAO c2 = new ClassTwoAO();
		ClassOneAT c3 = new ClassOneAT();
	
		String host = null;//"lab35.cpt.haw-hamburg.de";
		
		try {
			host = InetAddress.getLocalHost().getCanonicalHostName();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ObjectBroker objBroker = ObjectBroker.init(host, 50000, true);
		NameService nameSvc = objBroker.getNameService();
		nameSvc.rebind(c1, "c1");
		nameSvc.rebind(c2, "c2");
		nameSvc.rebind(c3, "c3");
		
		try {
			System.out.println("server: sleeping");
			Thread.sleep(40000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		objBroker.shutDown();
		System.out.println("end server");
	}

}
