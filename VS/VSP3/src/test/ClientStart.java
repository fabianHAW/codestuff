package test;

import accessor_one.SomeException110;
import accessor_one.SomeException112;
import accessor_two.SomeException304;
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
		accessor_one_concurrencyTest(Integer.valueOf(this.getName()));
		accessor_two_concurrencyTest(Integer.valueOf(this.getName()));
	}
	
	public void accessor_one_test(){
		ObjectBroker objBroker = ObjectBroker.init(this.nameserviceHost, this.nameservicePort, false);
	
		NameService nameSvc = objBroker.getNameService();
		String namec1 = "c1";
		String namec2 = "c21";
		String namec4 = "c4";
		Object rawObjRef1 = nameSvc.resolve(namec1);
		Object rawObjRef2 = nameSvc.resolve(namec2);
		Object rawObjRef4 = nameSvc.resolve(namec4);
	
		accessor_one.ClassOneImplBase remoteObj1 = accessor_one.ClassOneImplBase.narrowCast(rawObjRef1);
		accessor_one.ClassTwoImplBase remoteObj2 = accessor_one.ClassTwoImplBase.narrowCast(rawObjRef2);
		accessor_one.ClassTwoImplBase remoteObj4 = accessor_one.ClassTwoImplBase.narrowCast(rawObjRef4);
		
		/****
		 * Test eines Methoenaufrufs auf einem Objekt, das nicht existiert.
		 * Soll NullPointerException schmei�en
		 */
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
		String returnvalc1Wrong = null;
		String c1param1Wrong = "test";
		int c1param2Wrong = 5;
		
		/**Params c21 methodOne**/
		double c21param1 = 2.0;
		int returnvalc2m1;
		double c21param1Wrong = Double.MAX_VALUE;
		int returnvalc2m1Wrong;
		
		/**Params c21 methodTwo**/
		double returnvalc2m2;
		/**Test ClassOneAO methodOne**/
		
		/*****
		 * Test accessor_one.ClassOneImplBase methodOne
		 * Keine Exception
		 */
		try {
			returnvalc1 = remoteObj1.methodOne(c1param1, c1param2);
			System.out.println("accessor_one.ClassOneImplBase (\"" + namec1 + "\")");
			System.out.println("methodOne");
			System.out.println("param1 = \"" + c1param1 + "\" param2 = " + c1param2);
			System.out.println("return value = " + returnvalc1);
			System.out.println("------------------------------------------------------------------------------------");
		} catch (accessor_one.SomeException112 e) {
			System.out.println("accessor_one.ClassOneImplBase (\"" + namec1 + "\"");
			System.out.println("methodOne");
			System.out.println("param1 = \"" + c1param1 + "\" param2 = " + c1param2);
			System.out.println("accessor_one.SomeException112 with message \"" + e.getMessage() + "\"");
			System.out.println("------------------------------------------------------------------------------------");
		}
		
		/*****
		 * Test accessor_one.ClassOneImplBase methodOne
		 * Exception
		 */
		try {			
			returnvalc1 = remoteObj1.methodOne(c1param1Wrong, c1param2Wrong);
			System.out.println("accessor_one.ClassOneImplBase (\"" + namec1 + "\")");
			System.out.println("methodOne");
			System.out.println("param1 = \"" + c1param1Wrong + "\" param2 = " + c1param2Wrong);
			System.out.println("return value = " + returnvalc1Wrong);
			System.out.println("------------------------------------------------------------------------------------");
		} catch (accessor_one.SomeException112 e) {
			System.out.println("accessor_one.ClassOneImplBase (\"" + namec1 + "\")");
			System.out.println("methodOne");
			System.out.println("param1 = \"" + c1param1Wrong + "\" param2 = " + c1param2Wrong);
			System.out.println("accessor_one.SomeException112 with message \"" + e.getMessage() + "\"");
			System.out.println("------------------------------------------------------------------------------------");
		}
		
		/*****
		 * Test accessor_one.ClassTwoImplBase methodOne
		 * Keine Exception
		 */
		try {
			returnvalc2m1 = remoteObj2.methodOne(c21param1);
			System.out.println("accessor_one.ClassTwoImplBase (\"" + namec2 + "\")");
			System.out.println("methodOne");
			System.out.println("param1 = " + c21param1);
			System.out.println("return value = " + returnvalc2m1);
			System.out.println("------------------------------------------------------------------------------------");
		} catch (SomeException110 e) {
			System.out.println("accessor_one.ClassTwoImplBase (\"" + namec2 + "\")");
			System.out.println("methodOne");
			System.out.println("param1 = " + c21param1);
			System.out.println("accessor_one.SomeException110 with message \"" + e.getMessage() + "\"");
			System.out.println("------------------------------------------------------------------------------------");
		}
		
		/*****
		 * Test accessor_one.ClassTwoImplBase methodOne
		 *  Exception
		 */
		try {
			returnvalc2m1Wrong = remoteObj2.methodOne(c21param1Wrong);
			System.out.println("accessor_one.ClassTwoImplBase (\"" + namec2 + "\")");
			System.out.println("methodOne");
			System.out.println("param1 = " + c21param1Wrong);
			System.out.println("return value = " + returnvalc2m1Wrong);
			System.out.println("------------------------------------------------------------------------------------");
		} catch (SomeException110 e) {
			System.out.println("accessor_one.ClassTwoImplBase (\"" + namec2 + "\")");
			System.out.println("methodOne");
			System.out.println("param1 = " + c21param1Wrong);
			System.out.println("accessor_one.SomeException110 with message \"" + e.getMessage() + "\"");
			System.out.println("------------------------------------------------------------------------------------");
		}
		
		
		/*****
		 * Test accessor_one.ClassTwoImplBase methodTwo
		 * Keine Exception
		 */
		try {
			returnvalc2m2 = remoteObj2.methodTwo();
			System.out.println("accessor_one.ClassTwoImplBase (\"" + namec2 + "\")");
			System.out.println("methodTwo");
			System.out.println("no params");
			System.out.println("return value = " + returnvalc2m2);
			System.out.println("------------------------------------------------------------------------------------");
		} catch (SomeException112 e) {
			System.out.println("accessor_one.ClassTwoImplBase (\"" + namec2 + "\")");
			System.out.println("methodTwo");
			System.out.println("no params");
			System.out.println("accessor_one.SomeException112 with message \"" + e.getMessage() + "\"");
			System.out.println("------------------------------------------------------------------------------------");
		}

		objBroker.shutDown();
	
	}
	
	public void accessor_one_concurrencyTest(int clientName){	
		String namec22 = "c22";	
		
		ObjectBroker objBroker = ObjectBroker.init(this.nameserviceHost, this.nameservicePort, false);
		NameService nameSvc = objBroker.getNameService();
		Object rawObjRef22 = nameSvc.resolve(namec22);
		accessor_one.ClassTwoImplBase remoteObj2 = accessor_one.ClassTwoImplBase.narrowCast(rawObjRef22);
		
		double c22param1m1 = 3.2;
		double returnvalc22m1;
		
		double returnvalc22m2;
		
		switch(clientName){
		case 0:
			try {
				returnvalc22m1 = remoteObj2.methodOne(c22param1m1);
				System.out.println("accessor_one.ClassTwoImplBase (\"" + namec22 + "\")");
				System.out.println("methodOne");
				System.out.println("param1 = " + c22param1m1);
				System.out.println("return value = " + returnvalc22m1);
				System.out.println("------------------------------------------------------------------------------------");
			} catch (SomeException110 e) {
				System.out.println("accessor_one.ClassTwoImplBase (\"" + namec22 + "\")");
				System.out.println("methodOne");
				System.out.println("param1 = \"" + c22param1m1);
				System.out.println("accessor_one.SomeException110 with message \"" + e.getMessage() + "\"");
				System.out.println("------------------------------------------------------------------------------------");
			}
			break;
		case 1:
			try {
				returnvalc22m2 = remoteObj2.methodTwo();
				System.out.println("accessor_one.ClassTwoImplBase (\"" + namec22 + "\")");
				System.out.println("methodTwo");
				System.out.println("no params");
				System.out.println("return value = " + returnvalc22m2);
				System.out.println("------------------------------------------------------------------------------------");
			} catch (SomeException112 e) {
				System.out.println("accessor_one.ClassTwoImplBase (\"" + namec22 + "\")");
				System.out.println("methodTwo");
				System.out.println("no params");
				System.out.println("accessor_one.SomeException110 with message \"" + e.getMessage() + "\"");
				System.out.println("------------------------------------------------------------------------------------");
			}
			break;
		}
		
		objBroker.shutDown();
	}
	
	public void accessor_two_test(){
		
		ObjectBroker objBroker = ObjectBroker.init(this.nameserviceHost, this.nameservicePort, false);
		
		NameService nameSvc = objBroker.getNameService();
		String namec31 = "c31";
		Object rawObjRef3 = nameSvc.resolve(namec31);
	
		accessor_two.ClassOneImplBase remoteObj31 = accessor_two.ClassOneImplBase.narrowCast(rawObjRef3);
		
		/**Params c31**/
		double returnvalc31;
		
		/**Params c31 methodOne**/
		String c31param1m1_1 = "cute sloth";
		double c31param2m1_1 = 3.2;
		String c31param1m1_2 = null;
		double c31param2m1_2= 3.2;
		String c31param1m1_3 = "cute sloth";
		double c31param2m1_3= 1.9;
		
		/**Params c31 methodTwo**/
		String c31param1m2_1 = "the monkey with shoes";
		double c31param2m2_1 = 1.9;
		String c31param1m2_2 = null;
		double c31param2m2_2 = 1.9;
		String c31param1m2_3 = "the monkey with shoes";
		double c31param2m2_3 = 2.3;
		String c31param1m2_4 = "the monkey without shoes";
		double c31param2m2_4 = 1.9;
		
		/**Test ClassOneAT methodOne**/
		try {
			/**1.Test: normale-Test**/
			returnvalc31 = remoteObj31.methodOne(c31param1m1_1, c31param2m1_1);
			System.out.println("accessor_two.ClassOneImplBase (\"" + namec31 + "\")");
			System.out.println("methodOne");
			System.out.println("param1 = \"" + c31param1m1_1 + "\" param2 = " + c31param2m1_1);
			System.out.println("return value = " + returnvalc31);
			System.out.println("------------------------------------------------------------------------------------");
			
			/**2.Test: null-Test**/
			returnvalc31 = remoteObj31.methodOne(c31param1m1_2, c31param2m1_2);
			System.out.println("accessor_two.ClassOneImplBase (\"" + namec31 + "\")");
			System.out.println("methodOne");
			System.out.println("param1 = \"" + c31param1m1_2 + "\" param2 = " + c31param2m1_2);
			System.out.println("return value = " + returnvalc31);
			System.out.println("------------------------------------------------------------------------------------");
			
			/**3.Test: Exception112-Test**/
			returnvalc31 = remoteObj31.methodOne(c31param1m1_3, c31param2m1_3);
			System.out.println("accessor_two.ClassOneImplBase (\"" + namec31 + "\")");
			System.out.println("methodOne");
			System.out.println("param1 = \"" + c31param1m1_3 + "\" param2 = " + c31param2m1_3);
			System.out.println("return value = " + returnvalc31);
			System.out.println("------------------------------------------------------------------------------------");
		} catch (accessor_two.SomeException112 e) {
			System.out.println("accessor_two.ClassOneImplBase (\"" + namec31 + "\")");
			System.out.println("methodOne");
			System.out.println("param1 = \"" + c31param1m1_3 + "\" param2 = " + c31param2m1_3);
			System.out.println("accessor_one.SomeException112 with message \"" + e.getMessage() + "\"");
			System.out.println("------------------------------------------------------------------------------------");
		}
		
		/**Test ClassOneAT methodTwo**/
		try {
			/**1.Test: Normal-Test**/
			returnvalc31 = remoteObj31.methodTwo(c31param1m2_1, c31param2m2_1);
			System.out.println("accessor_two.ClassOneImplBase (\"" + namec31 + "\")");
			System.out.println("methodOne");
			System.out.println("param1 = \"" + c31param1m2_1 + "\" param2 = " + c31param2m2_1);
			System.out.println("return value = " + returnvalc31);
			System.out.println("------------------------------------------------------------------------------------");
			
			/**2.Test: null-Test**/
			returnvalc31 = remoteObj31.methodTwo(c31param1m2_2, c31param2m2_2);
			System.out.println("accessor_two.ClassOneImplBase (\"" + namec31 + "\")");
			System.out.println("methodOne");
			System.out.println("param1 = \"" + c31param1m2_2 + "\" param2 = " + c31param2m2_2);
			System.out.println("return value = " + returnvalc31);
			System.out.println("------------------------------------------------------------------------------------");
			
			/**3.Test: Exception112-Test**/
			returnvalc31 = remoteObj31.methodTwo(c31param1m2_3, c31param2m2_3);
			System.out.println("accessor_two.ClassOneImplBase (\"" + namec31 + "\")");
			System.out.println("methodOne");
			System.out.println("param1 = \"" + c31param1m2_3 + "\" param2 = " + c31param2m2_3);
			System.out.println("return value = " + returnvalc31);
			System.out.println("------------------------------------------------------------------------------------");
			
		} catch (accessor_two.SomeException112 e) {
			System.out.println("accessor_two.ClassOneImplBase (\"" + namec31 + "\")");
			System.out.println("methodOne");
			System.out.println("param1 = \"" + c31param1m2_3 + "\" param2 = " + c31param2m2_3);
			System.out.println("accessor_two.SomeException112 with message \"" + e.getMessage() + "\"");
			System.out.println("------------------------------------------------------------------------------------");
		} catch (accessor_two.SomeException304 e) {
			System.out.println("accessor_two.ClassOneImplBase (\"" + namec31 + "\")");
			System.out.println("methodOne");
			System.out.println("param1 = \"" + c31param1m2_3 + "\" param2 = " + c31param2m2_3);
			System.out.println("accessor_two.SomeException304 with message \"" + e.getMessage() + "\"");
			System.out.println("------------------------------------------------------------------------------------");
		}
		
		try{
			/**4.Test: Exception304-Test**/
			returnvalc31 = remoteObj31.methodTwo(c31param1m2_4, c31param2m2_4);
			System.out.println("accessor_two.ClassOneImplBase (\"" + namec31 + "\")");
			System.out.println("methodTwo");
			System.out.println("param1 = \"" + c31param1m2_4 + "\" param2 = " + c31param2m2_4);
			System.out.println("return value = " + returnvalc31);
			System.out.println("------------------------------------------------------------------------------------");
		} catch (accessor_two.SomeException112 e) {
			System.out.println("accessor_two.ClassOneImplBase (\"" + namec31 + "\")");
			System.out.println("methodTwo");
			System.out.println("param1 = \"" + c31param1m2_3 + "\" param2 = " + c31param2m2_3);
			System.out.println("accessor_two.SomeException112 with message \"" + e.getMessage() + "\"");
			System.out.println("------------------------------------------------------------------------------------");
		} catch (accessor_two.SomeException304 e) {
			System.out.println("accessor_two.ClassOneImplBase (\"" + namec31 + "\")");
			System.out.println("methodTwo");
			System.out.println("param1 = \"" + c31param1m2_4 + "\" param2 = " + c31param2m2_4);
			System.out.println("accessor_two.SomeException304 with message \"" + e.getMessage() + "\"");
			System.out.println("------------------------------------------------------------------------------------");
		}	

		objBroker.shutDown();
		
	}
	
	public void accessor_two_concurrencyTest(int clientName){	
		String namec32 = "c32";	
		
		ObjectBroker objBroker = ObjectBroker.init(this.nameserviceHost, this.nameservicePort, false);
		NameService nameSvc = objBroker.getNameService();
		Object rawObjRef32 = nameSvc.resolve(namec32);
		accessor_two.ClassOneImplBase remoteObj2 = accessor_two.ClassOneImplBase.narrowCast(rawObjRef32);

		String c32param1m1 = "the monkey with a white hat";
		double c32param2m1 = 3.2;
		double returnvalc32m1;

		String c32param1m2 = "the monkey with a white hat";
		double c32param2m2 = 1.3;
		double returnvalc32m2;
		
		switch(clientName){
		case 0:
			try {
				returnvalc32m1 = remoteObj2.methodOne(c32param1m1, c32param2m1);
				System.out.println("accessor_two.ClassOneImplBase (\"" + namec32 + "\")");
				System.out.println("methodOne");
				System.out.println("param1 = \"" + c32param1m1 + "\" param2 = " + c32param2m1);
				System.out.println("return value = " + returnvalc32m1);
				System.out.println("------------------------------------------------------------------------------------");
			} catch (accessor_two.SomeException112 e) {
				System.out.println("accessor_two.ClassOneImplBase (\"" + namec32 + "\")");
				System.out.println("methodOne");
				System.out.println("param1 = \"" + c32param1m1 + "\" param2 = " + c32param2m1);
				System.out.println("accessor_two.SomeException112 with message \"" + e.getMessage() + "\"");
				System.out.println("------------------------------------------------------------------------------------");
			}
			break;
		case 1:
			try {
				returnvalc32m2 = remoteObj2.methodTwo(c32param1m2, c32param2m2);
				System.out.println("accessor_two.ClassOneImplBase (\"" + namec32 + "\")");
				System.out.println("methodTwo");
				System.out.println("param1 = \"" + c32param1m2 + "\" param2 = " + c32param2m2);
				System.out.println("return value = " + returnvalc32m2);
				System.out.println("------------------------------------------------------------------------------------");
			} catch (accessor_two.SomeException112 e) {
				System.out.println("accessor_two.ClassOneImplBase (\"" + namec32 + "\")");
				System.out.println("methodTwo");
				System.out.println("param1 = \"" + c32param1m2 + "\" param2 = " + c32param2m2);
				System.out.println("accessor_two.SomeException112 with message \"" + e.getMessage() + "\"");
				System.out.println("------------------------------------------------------------------------------------");
			} catch (SomeException304 e) {
				System.out.println("accessor_two.ClassOneImplBase (\"" + namec32 + "\")");
				System.out.println("methodTwo");
				System.out.println("param1 = \"" + c32param1m2 + "\" param2 = " + c32param2m2);
				System.out.println("accessor_two.SomeException304 with message \"" + e.getMessage() + "\"");
				System.out.println("------------------------------------------------------------------------------------");
			}
			break;
		}
		
		objBroker.shutDown();
	}
}
