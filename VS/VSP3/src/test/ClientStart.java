package test;

import accessor_one.SomeException110;
import accessor_one.SomeException112;
import mware_lib.NameService;
import mware_lib.ObjectBroker;

public class ClientStart extends Thread{

	private String nameserviceHost;
	private int nameservicePort;
	
	public ClientStart(String nameserviceHost, int nameservicePort) {
		this.nameserviceHost = nameserviceHost;
		this.nameservicePort = nameservicePort;
	}
	
	public void run(){
		accessor_one_test();
		accessor_two_test();
	}
	
	public void accessor_one_test(){
	
		ObjectBroker objBroker = ObjectBroker.init(this.nameserviceHost, this.nameservicePort, false);
	
		NameService nameSvc = objBroker.getNameService();
		String namec1 = "c1";
		String namec2 = "c2";
		String namec4 = "c4";
		Object rawObjRef1 = nameSvc.resolve(namec1);
		Object rawObjRef2 = nameSvc.resolve(namec2);
		Object rawObjRef4 = nameSvc.resolve(namec4);
	
		accessor_one.ClassOneImplBase remoteObj1 = accessor_one.ClassOneImplBase.narrowCast(rawObjRef1);
		accessor_one.ClassTwoImplBase remoteObj2 = accessor_one.ClassTwoImplBase.narrowCast(rawObjRef2);
		accessor_one.ClassTwoImplBase remoteObj4 = accessor_one.ClassTwoImplBase.narrowCast(rawObjRef4);
		
		try {
			remoteObj4.methodTwo();
		} catch (SomeException112 e1) {
			e1.printStackTrace();
		}
		catch(NullPointerException e){
			System.out.println("your servant is not registered");
			System.out.println("------------------------------------------------------------------------------------");
		}
		
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
			System.out.println("------------------------------------------------------------------------------------");
		} catch (accessor_one.SomeException112 e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("accessor_one.ClassOneImplBase (\"" + namec1 + "\"");
			System.out.println("methodOne");
			System.out.println("param1 = \"" + c1param1 + "\" param2 = " + c1param2);
			System.out.println("accessor_one.SomeException112 with message \"" + e.getMessage() + "\"");
			System.out.println("------------------------------------------------------------------------------------");
		}
		
		/**Test ClassTwoAO methodOne**/
		try {
			returnvalc2m1 = remoteObj2.methodOne(2.0);
			System.out.println("accessor_one.ClassTwoImplBase (\"" + namec2 + "\")");
			System.out.println("methodOne");
			System.out.println("param1 = " + c2param1);
			System.out.println("return value = " + returnvalc2m1);
			System.out.println("------------------------------------------------------------------------------------");
		} catch (SomeException110 e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("accessor_one.ClassTwoImplBase (\"" + namec2 + "\"");
			System.out.println("methodOne");
			System.out.println("param1 = " + c2param1);
			System.out.println("accessor_one.SomeException110 with message \"" + e.getMessage() + "\"");
			System.out.println("------------------------------------------------------------------------------------");
		}
		
		/**Test ClassTwoAO methodTwo**/
		try {
			returnvalc2m2 = remoteObj2.methodTwo();
			System.out.println("accessor_one.ClassTwoImplBase (\"" + namec2 + "\")");
			System.out.println("methodTwo");
			System.out.println("no params");
			System.out.println("return value = " + returnvalc2m2);
			System.out.println("------------------------------------------------------------------------------------");
		} catch (SomeException112 e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("accessor_one.ClassTwoImplBase (\"" + namec2 + "\"");
			System.out.println("methodTwo");
			System.out.println("no params");
			System.out.println("accessor_one.SomeException112 with message \"" + e.getMessage() + "\"");
			System.out.println("------------------------------------------------------------------------------------");
		}

		objBroker.shutDown();
	
	}
	
	public void accessor_two_test(){
		
		ObjectBroker objBroker = ObjectBroker.init(this.nameserviceHost, this.nameservicePort, false);
		
		NameService nameSvc = objBroker.getNameService();
		String namec3 = "c3";
		Object rawObjRef3 = nameSvc.resolve(namec3);
	
		accessor_two.ClassOneImplBase remoteObj3 = accessor_two.ClassOneImplBase.narrowCast(rawObjRef3);
		
		/**Params c3**/
		double returnvalc3;
		
		/**Params c3 methodOne**/
		String c3param1m1_1 = "cute sloth";
		double c3param2m1_1 = 3.2;
		String c3param1m1_2 = null;
		double c3param2m1_2= 3.2;
		String c3param1m1_3 = "cute sloth";
		double c3param2m1_3= 1.9;
		
		/**Params c3 methodTwo**/
		String c3param1m2_1 = "the monkey with shoes";
		double c3param2m2_1 = 1.9;
		String c3param1m2_2 = null;
		double c3param2m2_2 = 1.9;
		String c3param1m2_3 = "the monkey with shoes";
		double c3param2m2_3 = 2.3;
		String c3param1m2_4 = "the monkey without shoes";
		double c3param2m2_4 = 1.9;
		
		/**Test ClassOneAT methodOne**/
		try {
			/**1.Test: normale-Test**/
			returnvalc3 = remoteObj3.methodOne(c3param1m1_1, c3param2m1_1);
			System.out.println("accessor_two.ClassOneImplBase (\"" + namec3 + "\")");
			System.out.println("methodOne");
			System.out.println("param1 = " + c3param1m1_1 + " param2 = " + c3param2m1_1);
			System.out.println("return value = " + returnvalc3);
			System.out.println("------------------------------------------------------------------------------------");
			
			/**2.Test: null-Test**/
			returnvalc3 = remoteObj3.methodOne(c3param1m1_2, c3param2m1_2);
			System.out.println("accessor_two.ClassOneImplBase (\"" + namec3 + "\")");
			System.out.println("methodOne");
			System.out.println("param1 = " + c3param1m1_2 + " param2 = " + c3param2m1_2);
			System.out.println("return value = " + returnvalc3);
			System.out.println("------------------------------------------------------------------------------------");
			
			/**3.Test: Exception112-Test**/
			returnvalc3 = remoteObj3.methodOne(c3param1m1_3, c3param2m1_3);
			System.out.println("accessor_two.ClassOneImplBase (\"" + namec3 + "\")");
			System.out.println("methodOne");
			System.out.println("param1 = " + c3param1m1_3 + " param2 = " + c3param2m1_3);
			System.out.println("return value = " + returnvalc3);
			System.out.println("------------------------------------------------------------------------------------");
		} catch (accessor_two.SomeException112 e) {
			//e.printStackTrace();
			System.out.println("accessor_one.ClassOneImplBase (\"" + namec3 + "\"");
			System.out.println("methodOne");
			System.out.println("param1 = \"" + c3param1m1_3 + "\" param2 = " + c3param2m1_3);
			System.out.println("accessor_one.SomeException112 with message \"" + e.getMessage() + "\"");
			System.out.println("------------------------------------------------------------------------------------");
		}
		
		/**Test ClassOneAT methodTwo**/
		try {
			/**1.Test: Normal-Test**/
			returnvalc3 = remoteObj3.methodTwo(c3param1m2_1, c3param2m2_1);
			System.out.println("accessor_two.ClassOneImplBase (\"" + namec3 + "\")");
			System.out.println("methodOne");
			System.out.println("param1 = " + c3param1m2_1 + " param2 = " + c3param2m2_1);
			System.out.println("return value = " + returnvalc3);
			System.out.println("------------------------------------------------------------------------------------");
			
			/**2.Test: null-Test**/
			returnvalc3 = remoteObj3.methodTwo(c3param1m2_2, c3param2m2_2);
			System.out.println("accessor_two.ClassOneImplBase (\"" + namec3 + "\")");
			System.out.println("methodOne");
			System.out.println("param1 = " + c3param1m2_2 + " param2 = " + c3param2m2_2);
			System.out.println("return value = " + returnvalc3);
			System.out.println("------------------------------------------------------------------------------------");
			
			/**3.Test: Exception112-Test**/
			returnvalc3 = remoteObj3.methodTwo(c3param1m2_3, c3param2m2_3);
			System.out.println("accessor_two.ClassOneImplBase (\"" + namec3 + "\")");
			System.out.println("methodOne");
			System.out.println("param1 = " + c3param1m2_3 + " param2 = " + c3param2m2_3);
			System.out.println("return value = " + returnvalc3);
			System.out.println("------------------------------------------------------------------------------------");
			
		} catch (accessor_two.SomeException112 e) {
			//e.printStackTrace();
			System.out.println("accessor_two.ClassOneImplBase (\"" + namec3 + "\"");
			System.out.println("methodOne");
			System.out.println("param1 = \"" + c3param1m2_3 + "\" param2 = " + c3param2m2_3);
			System.out.println("accessor_two.SomeException112 with message \"" + e.getMessage() + "\"");
		} catch (accessor_two.SomeException304 e) {
			//e.printStackTrace();
			System.out.println("accessor_two.ClassOneImplBase (\"" + namec3 + "\"");
			System.out.println("methodOne");
			System.out.println("param1 = \"" + c3param1m2_4 + "\" param2 = " + c3param2m2_4);
			System.out.println("accessor_two.SomeException304 with message \"" + e.getMessage() + "\"");
			System.out.println("------------------------------------------------------------------------------------");
		}
		
		try{
			/**4.Test: Exception304-Test**/
			returnvalc3 = remoteObj3.methodTwo(c3param1m2_4, c3param2m2_4);
			System.out.println("accessor_two.ClassOneImplBase (\"" + namec3 + "\")");
			System.out.println("methodOne");
			System.out.println("param1 = " + c3param1m2_4 + " param2 = " + c3param2m2_4);
			System.out.println("return value = " + returnvalc3);
			System.out.println("------------------------------------------------------------------------------------");
		} catch (accessor_two.SomeException112 e) {
			//e.printStackTrace();
			System.out.println("accessor_two.ClassOneImplBase (\"" + namec3 + "\"");
			System.out.println("methodOne");
			System.out.println("param1 = \"" + c3param1m2_3 + "\" param2 = " + c3param2m2_3);
			System.out.println("accessor_two.SomeException112 with message \"" + e.getMessage() + "\"");
			System.out.println("------------------------------------------------------------------------------------");
		} catch (accessor_two.SomeException304 e) {
			//e.printStackTrace();
			System.out.println("accessor_two.ClassOneImplBase (\"" + namec3 + "\"");
			System.out.println("methodOne");
			System.out.println("param1 = \"" + c3param1m2_4 + "\" param2 = " + c3param2m2_4);
			System.out.println("accessor_two.SomeException304 with message \"" + e.getMessage() + "\"");
			System.out.println("------------------------------------------------------------------------------------");
		}
		

		objBroker.shutDown();
		
	}
}
