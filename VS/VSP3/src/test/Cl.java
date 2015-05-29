package test;

import nameservice.NameserviceMain;

public class Cl extends Thread{

	public Cl() {
		// TODO Auto-generated constructor stub
	}
	
	public void run(){
		this.setName("Client Test-Thread");
		int nsport = 50000;
		String localhost = "127.0.0.1";

		String[] client = new String[3];
	
		client[0] = "1";
		client[1] = "" + localhost;
		client[2] = "" + nsport;
		
		MiddlewareTest.main(client);
	}

}
