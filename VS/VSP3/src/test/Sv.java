package test;

import nameservice.NameserviceMain;

public class Sv extends Thread{

	public Sv() {
		// TODO Auto-generated constructor stub
	}
	
	public void run(){
		this.setName("Server Test-Thread");
		int nsport = 50000;
		String localhost = "127.0.0.1";
	
		String[] server = new String[3];
		
		server[0] = "0";
		server[1] = "" + localhost;
		server[2] = "" + nsport;
	
		MiddlewareTest.main(server);
	}

}
