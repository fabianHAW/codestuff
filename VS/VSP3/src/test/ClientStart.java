package test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import mware_lib.CommunicationModule;
import mware_lib.NameService;
import mware_lib.ObjectBroker;

public class ClientStart extends Thread{

	public ClientStart() {
		// TODO Auto-generated constructor stub
	}
	
	public void run(){
		accessor_one_test();
	}
	
	public  void accessor_one_test(){
	
		CommunicationModule.setCommunicatiomoduleport(50003);
	
		String host = null;
		try {
			host = InetAddress.getLocalHost().getCanonicalHostName();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ObjectBroker objBroker = ObjectBroker.init(host, 50000, true);
	
		NameService nameSvc = objBroker.getNameService();
	
		Object rawObjRef = nameSvc.resolve("test");
		System.out.println(rawObjRef);
	
		accessor_one.ClassOneImplBase remoteObj = accessor_one.ClassOneImplBase.narrowCast(rawObjRef);
	
	
		String s1 = "false";
		try {
			s1 = remoteObj.methodOne("test", 4);
		} catch (accessor_one.SomeException112 e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(s1);
		
		objBroker.shutDown();
	
	}

}
