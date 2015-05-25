package test;

import nameservice.NameserviceMain;

public class NameserviceStart extends Thread {

	private int port;
	public NameserviceStart(int port) {
		// TODO Auto-generated constructor stub
		this.port = port;
	}
	
	public void run(){
		nameservice_test();
	}
	
	public void nameservice_test(){
		String[] args = new String[1];
		args[0] = "" + port;
		NameserviceMain.main(args);
	}

}
