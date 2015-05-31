package test;

import mware_lib.NameService;
import mware_lib.ObjectBroker;

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
		ClassTwoAO c21 = new ClassTwoAO();
		ClassTwoAO c22 = new ClassTwoAO();
		ClassOneAT c31 = new ClassOneAT();
		ClassOneAT c32 = new ClassOneAT();

		ObjectBroker objBroker = ObjectBroker.init(this.nameserviceHost, this.nameservicePort, true);
		NameService nameSvc = objBroker.getNameService();
		nameSvc.rebind(c1, "c1");
		nameSvc.rebind(c21, "c21");
		nameSvc.rebind(c22, "c22");
		nameSvc.rebind(c31, "c31");
		nameSvc.rebind(c32, "c32");
		
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
