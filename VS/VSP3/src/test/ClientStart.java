package test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import accessor_one.SomeException110;
import accessor_one.SomeException112;
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
		String namec1 = "c1";
		String namec2 = "c2";
		Object rawObjRef1 = nameSvc.resolve(namec1);
		Object rawObjRef2 = nameSvc.resolve(namec2);
	
		accessor_one.ClassOneImplBase remoteObj1 = accessor_one.ClassOneImplBase.narrowCast(rawObjRef1);
		accessor_one.ClassTwoImplBase remoteObj2 = accessor_one.ClassTwoImplBase.narrowCast(rawObjRef2);
		
		/**Params c1**/
		String returnvalc1 = null;
		String c1param1 = "test";
		int c1param2 = 4;
		
		/**Params c2 methodOne**/
		double c2param1 = 2.0;
		int returnvalc2m1;
		
		/**Params c2 methodTwo**/
		double returnvalc2m2;
		/**Test ClassOneAO methodOne**/
		try {
			
			returnvalc1 = remoteObj1.methodOne(c1param1, c1param2);
			System.out.println("accessor_one.ClassOneImplBase (\"" + namec1 + "\")");
			System.out.println("methodOne");
			System.out.println("param1 = " + c1param1 + " param2 = " + c1param2);
			System.out.println("return value = " + returnvalc1);
		} catch (accessor_one.SomeException112 e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("accessor_one.ClassOneImplBase (\"" + namec1 + "\"");
			System.out.println("methodOne");
			System.out.println("param1 = \"" + c1param1 + "\" param2 = " + c1param2);
			System.out.println("accessor_one.SomeException112 with message \"" + e.getMessage() + "\"");
		}
		
		/**Test ClassTwoAO methodOne**/
		try {
			returnvalc2m1 = remoteObj2.methodOne(2.0);
			System.out.println("accessor_one.ClassTwoImplBase (\"" + namec2 + "\")");
			System.out.println("methodOne");
			System.out.println("param1 = " + c2param1);
			System.out.println("return value = " + returnvalc2m1);
		} catch (SomeException110 e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("accessor_one.ClassTwoImplBase (\"" + namec2 + "\"");
			System.out.println("methodOne");
			System.out.println("param1 = " + c2param1);
			System.out.println("accessor_one.SomeException110 with message \"" + e.getMessage() + "\"");
		}
		
		/**Test ClassTwoAO methodTwo**/
		try {
			returnvalc2m2 = remoteObj2.methodTwo();
			System.out.println("accessor_one.ClassTwoImplBase (\"" + namec2 + "\")");
			System.out.println("methodTwo");
			System.out.println("no params");
			System.out.println("return value = " + returnvalc2m2);
		} catch (SomeException112 e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("accessor_one.ClassTwoImplBase (\"" + namec2 + "\"");
			System.out.println("methodTwo");
			System.out.println("no params");
			System.out.println("accessor_one.SomeException112 with message \"" + e.getMessage() + "\"");

		}

		objBroker.shutDown();
	
	}

}
