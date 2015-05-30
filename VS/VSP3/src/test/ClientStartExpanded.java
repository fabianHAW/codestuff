package test;

import java.util.ArrayList;
import java.util.Random;

import accessor_one.SomeException110;
import accessor_one.SomeException112;
import mware_lib.NameService;
import mware_lib.ObjectBroker;

public class ClientStartExpanded extends Thread{

	private int nameservicePort;
	private String nameserviceHost;
	private ArrayList<String> servantnames;
	private int objectcalls;

	public ClientStartExpanded(int nsport, String nshost, ArrayList<String> servantnames, int objectcalls) {
		// TODO Auto-generated constructor stub
		nameservicePort = nsport;
		nameserviceHost = nshost;
		this.servantnames = servantnames;
		System.out.println("sernvantnames: " + servantnames.size());
		this.objectcalls = objectcalls;
	}
	
	public void run(){
		accessor_one_two_test();
	}
	
	public void accessor_one_two_test(){
		ObjectBroker objBroker = ObjectBroker.init(this.nameserviceHost, this.nameservicePort, false);
		NameService nameSvc = objBroker.getNameService();
		Random rand = new Random();
		
		for(int i = 0; i < objectcalls; i++){
			String servant = servantnames.get(rand.nextInt(servantnames.size()));
			Object rawOb = nameSvc.resolve(servant);
			int servantid = Integer.parseInt(servant.substring(0, 1));
			
			if(servantid == 1){
				accessor_one.ClassOneImplBase remoteObj = accessor_one.ClassOneImplBase.narrowCast(rawOb);
				accessor_one_class_one(remoteObj, servant);
			}else if(servantid == 2){
				accessor_one.ClassTwoImplBase remoteObj = accessor_one.ClassTwoImplBase.narrowCast(rawOb);
				accessor_one_class_two(remoteObj, servant);
			}else if(servantid == 3){
				accessor_two.ClassOneImplBase remoteObj = accessor_two.ClassOneImplBase.narrowCast(rawOb);
				accessor_two_class_one(remoteObj, servant);
			}else{
				System.out.println("Can't parse Object id!");
			}
			
		}
		objBroker.shutDown();
		
	}
	
	public void accessor_one_class_one(accessor_one.ClassOneImplBase remoteObj, String name){
		Random rand = new Random();
		int testkind = rand.nextInt(2); //0 = Methodentest korrekt, 1 = Methodentest Exception
		String param1 = this.getName();
		if(testkind == 1){
			Random r = new Random();
			int param2 = r.nextInt()*2;
		try {
			String returnval= remoteObj.methodOne(param1, param2);
			System.out.println("accessor_one.ClassOneImplBase (\"" + name + "\")");
			System.out.println("methodOne");
			System.out.println("param1 = \"" + param1 + "\" param2 = " + param2);
			System.out.println("return value = " + returnval);
			System.out.println("------------------------------------------------------------------------------------");
		} catch (accessor_one.SomeException112 e) {
			System.out.println("accessor_one.ClassOneImplBase (\"" + name + "\"");
			System.out.println("methodOne");
			System.out.println("param1 = \"" + param1 + "\" param2 = " + param2);
			System.out.println("accessor_one.SomeException112 with message \"" + e.getMessage() + "\"");
			System.out.println("------------------------------------------------------------------------------------");
		}
		}else {
			Random r = new Random();
			int param2 = (r.nextInt()*2) - 1;
			try {
				String returnval= remoteObj.methodOne(param1, param2);
				System.out.println("accessor_one.ClassOneImplBase (\"" + name + "\")");
				System.out.println("methodOne");
				System.out.println("param1 = \"" + param1 + "\" param2 = " + param2);
				System.out.println("return value = " + returnval);
				System.out.println("------------------------------------------------------------------------------------");
			} catch (accessor_one.SomeException112 e) {
				System.out.println("accessor_one.ClassOneImplBase (\"" + name + "\"");
				System.out.println("methodOne");
				System.out.println("param1 = \"" + param1 + "\" param2 = " + param2);
				System.out.println("accessor_one.SomeException112 with message \"" + e.getMessage() + "\"");
				System.out.println("------------------------------------------------------------------------------------");
			}
		}
	}
	
	public void accessor_one_class_two(accessor_one.ClassTwoImplBase remoteObj, String name){
		Random rand = new Random();
		int testkind = rand.nextInt(2); //0 = Methodentest korrekt, 1 = Methodentest Exception
		int method = rand.nextInt(2); //0 = methodOne, 1 = methodTwo
		if(method == 0){
		if(testkind == 0){
			double param1 = Integer.MAX_VALUE / 4;
		try {
			int returnval = remoteObj.methodOne(param1);
			System.out.println("accessor_one.ClassTwoImplBase (\"" + name + "\")");
			System.out.println("methodOne");
			System.out.println("param1 = " + param1);
			System.out.println("return value = " + returnval);
			System.out.println("------------------------------------------------------------------------------------");
		} catch (SomeException110 e) {
			System.out.println("accessor_one.ClassTwoImplBase (\"" + name + "\"");
			System.out.println("methodOne");
			System.out.println("param1 = " + param1);
			System.out.println("accessor_one.SomeException110 with message \"" + e.getMessage() + "\"");
			System.out.println("------------------------------------------------------------------------------------");
		}
		}else {
			double param1 = Double.MAX_VALUE;
			try {
				int returnval = remoteObj.methodOne(param1);
				System.out.println("accessor_one.ClassTwoImplBase (\"" + name + "\")");
				System.out.println("methodOne");
				System.out.println("param1 = " + param1);
				System.out.println("return value = " + returnval);
				System.out.println("------------------------------------------------------------------------------------");
			} catch (SomeException110 e) {
				System.out.println("accessor_one.ClassTwoImplBase (\"" + name + "\"");
				System.out.println("methodOne");
				System.out.println("param1 = " + param1);
				System.out.println("accessor_one.SomeException110 with message \"" + e.getMessage() + "\"");
				System.out.println("------------------------------------------------------------------------------------");
			}
		}}else{
			try {
				double returnval = remoteObj.methodTwo();
				System.out.println("accessor_one.ClassTwoImplBase (\"" + name + "\")");
				System.out.println("methodTwo");
				System.out.println("no params");
				System.out.println("return value = " + returnval);
				System.out.println("------------------------------------------------------------------------------------");
			} catch (SomeException112 e) {
				System.out.println("accessor_one.ClassTwoImplBase (\"" + name + "\"");
				System.out.println("methodTwo");
				System.out.println("no params");
				System.out.println("accessor_one.SomeException112 with message \"" + e.getMessage() + "\"");
				System.out.println("------------------------------------------------------------------------------------");
			}
		}
		
		
	}
	
	public void accessor_two_class_one(accessor_two.ClassOneImplBase remoteObj, String name){
		Random rand = new Random();
		int methodKind = rand.nextInt(2);//0 = methodOne, 1 = methodTwo
		if(methodKind == 0){
				double returnval;
				String param1 = name;
				double param2 = 3;
			try {
				/**1.Test: normale-Test**/
				 returnval = remoteObj.methodOne(param1, 3);
				System.out.println("accessor_two.ClassOneImplBase (\"" + name + "\")");
				System.out.println("methodOne");
				System.out.println("param1 = \"" + param1 + "\" param2 = " + param2);
				System.out.println("return value = " + returnval);
				System.out.println("------------------------------------------------------------------------------------");
				
				/**2.Test: null-Test**/
				returnval = remoteObj.methodOne(null, param2);
				System.out.println("accessor_two.ClassOneImplBase (\"" + name + "\")");
				System.out.println("methodOne");
				System.out.println("param1 = \"" + param1 + "\" param2 = " + param2);
				System.out.println("return value = " + returnval);
				System.out.println("------------------------------------------------------------------------------------");
				
				/**3.Test: Exception112-Test**/
				param2 = 1;
				returnval = remoteObj.methodOne(param1, param2);
				System.out.println("accessor_two.ClassOneImplBase (\"" + name + "\")");
				System.out.println("methodOne");
				System.out.println("param1 = \"" + param1 + "\" param2 = " + param2);
				System.out.println("return value = " + returnval);
				System.out.println("------------------------------------------------------------------------------------");
			} catch (accessor_two.SomeException112 e) {
				System.out.println("accessor_two.ClassOneImplBase (\"" + name + "\")");
				System.out.println("methodOne");
				System.out.println("param1 = \"" + param1 + "\" param2 = " + param2);
				System.out.println("accessor_one.SomeException112 with message \"" + e.getMessage() + "\"");
				System.out.println("------------------------------------------------------------------------------------");
			}
		}else{
			double returnval;
			String param1 = name;
			double param2 = 1; 
			try {
				/**1.Test: Normal-Test**/
				returnval = remoteObj.methodTwo(param1, param2);
				System.out.println("accessor_two.ClassOneImplBase (\"" + name + "\")");
				System.out.println("methodOne");
				System.out.println("param1 = \"" + param1 + "\" param2 = " + param2);
				System.out.println("return value = " + returnval);
				System.out.println("------------------------------------------------------------------------------------");
				
				/**2.Test: null-Test**/
				returnval = remoteObj.methodTwo(null, param2);
				System.out.println("accessor_two.ClassOneImplBase (\"" + name + "\")");
				System.out.println("methodOne");
				System.out.println("param1 = \"" + null + "\" param2 = " + param2);
				System.out.println("return value = " + returnval);
				System.out.println("------------------------------------------------------------------------------------");
				
				/**3.Test: Exception112-Test**/
				param2 = 3;
				returnval = remoteObj.methodTwo(param1, param2);
				System.out.println("accessor_two.ClassOneImplBase (\"" + name + "\")");
				System.out.println("methodOne");
				System.out.println("param1 = \"" + param1 + "\" param2 = " + param2);
				System.out.println("return value = " + returnval);
				System.out.println("------------------------------------------------------------------------------------");
				
			} catch (accessor_two.SomeException112 e) {
				System.out.println("accessor_two.ClassOneImplBase (\"" + name + "\")");
				System.out.println("methodOne");
				System.out.println("param1 = \"" + param1 + "\" param2 = " + param2);
				System.out.println("accessor_two.SomeException112 with message \"" + e.getMessage() + "\"");
				System.out.println("------------------------------------------------------------------------------------");
			} catch (accessor_two.SomeException304 e) {
				System.out.println("accessor_two.ClassOneImplBase (\"" + name + "\")");
				System.out.println("methodOne");
				System.out.println("param1 = \"" + param1 + "\" param2 = " + param2);
				System.out.println("accessor_two.SomeException304 with message \"" + e.getMessage() + "\"");
				System.out.println("------------------------------------------------------------------------------------");
			}
			
			try{
				/**4.Test: Exception304-Test**/
				param1 = "the mokey without shoes";
				param2 = 1;
				returnval = remoteObj.methodTwo(param1, param2);
				System.out.println("accessor_two.ClassOneImplBase (\"" + name + "\")");
				System.out.println("methodTwo");
				System.out.println("param1 = \"" + param1 + "\" param2 = " + param2);
				System.out.println("return value = " + returnval);
				System.out.println("------------------------------------------------------------------------------------");
			} catch (accessor_two.SomeException112 e) {
				System.out.println("accessor_two.ClassOneImplBase (\"" + name + "\")");
				System.out.println("methodTwo");
				System.out.println("param1 = \"" + param1 + "\" param2 = " + param2);
				System.out.println("accessor_two.SomeException112 with message \"" + e.getMessage() + "\"");
				System.out.println("------------------------------------------------------------------------------------");
			} catch (accessor_two.SomeException304 e) {
				System.out.println("accessor_two.ClassOneImplBase (\"" + name + "\")");
				System.out.println("methodTwo");
				System.out.println("param1 = \"" + param1 + "\" param2 = " + param2);
				System.out.println("accessor_two.SomeException304 with message \"" + e.getMessage() + "\"");
				System.out.println("------------------------------------------------------------------------------------");
			}	
		}
	}

}
