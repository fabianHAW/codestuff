package test;

import mware_lib.NameService;
import mware_lib.ObjectBroker;
import accessor_one.ClassOneAO;
import accessor_one.ClassTwoAO;
import accessor_two.ClassOneAT;

public class ServerStart extends Thread{

	private String nameserviceHost;
	private int nameservicePort;
	
	public ServerStart(String nameserviceHost, int nameservicePort) {
		this.nameserviceHost = nameserviceHost;
		this.nameservicePort = nameservicePort;
	}
	
	public void run(){
		accessor_one_and_accessor_two_test();
	}
	
	public void accessor_one_and_accessor_two_test(){

		ClassOneAO c1 = new ClassOneAO();
		ClassTwoAO c2 = new ClassTwoAO();
		ClassOneAT c3 = new ClassOneAT();

		ObjectBroker objBroker = ObjectBroker.init(this.nameserviceHost, this.nameservicePort, true);
		NameService nameSvc = objBroker.getNameService();
		nameSvc.rebind(c1, "c1");
		nameSvc.rebind(c2, "c2");
		nameSvc.rebind(c3, "c3");
		
		try {
			System.out.println("server: sleeping");
			Thread.sleep(40000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		objBroker.shutDown();
		System.out.println("end server");
	}

}
