package test;

import java.util.ArrayList;
import java.util.Random;

import mware_lib.NameService;
import mware_lib.ObjectBroker;

public class ServerStartExpanded extends Thread{

	private int nameservicePort;
	private String nameserviceHost;
	private ArrayList<String> servants;

	public ServerStartExpanded(int nsport, String nshost) {
		// TODO Auto-generated constructor stub
		servants = new ArrayList<String>();
		nameservicePort = nsport;
		nameserviceHost = nshost;
	}
	
	public void run(){
		accessor_one_two_test();
	}
	
	public void accessor_one_two_test(){
		ObjectBroker objBroker = ObjectBroker.init(this.nameserviceHost, this.nameservicePort, true);
		NameService nameSvc = objBroker.getNameService();
	
		
		Random classid = new Random();
		ArrayList<Integer> counts = new ArrayList<Integer>();
		counts.add( 1 + classid.nextInt(10)); //counts(0) = ClassOneAO
		counts.add(1 + classid.nextInt(10)); //counts(1) = ClassTwoAO
		counts.add(1 + classid.nextInt(10)); //counts(2) = ClassOneAT
		
		//Generates a random number of servants
		for(int i = 0; i < counts.get(0); i++){
			String name = "1_classOneAO-" + i;
			nameSvc.rebind(new ClassOneAO(), name);
			servants.add(name);
			System.out.println("name: " + name);
		}
		
		for(int i = 0; i < counts.get(1); i++){
			String name = "2_classTwoAO-" + i;
			nameSvc.rebind(new ClassTwoAO(), name);
			servants.add(name);
			System.out.println("name: " + name);
		}
		
		for(int i = 0; i < counts.get(1); i++){
			String name = "3_classOneAT-" + i;
			nameSvc.rebind(new ClassOneAT(), name);
			servants.add(name);
			System.out.println("name: " + name);
		}

		
		try {
			System.out.println("server: sleeping");
			Thread.sleep(40000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		objBroker.shutDown();
		System.out.println("end server");
	}
	
	public ArrayList<String> getServantNames(){
		System.out.println("servants size: " + servants.size());
		return servants;
	}

}
