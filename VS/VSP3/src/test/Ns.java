package test;

import nameservice.NameserviceMain;

public class Ns extends Thread{

	public Ns() {
		// TODO Auto-generated constructor stub
	}
	
	public void run(){
		this.setName("Nameservice Test-Thread");
		String[] r = new String[1];
		int nsport = 50000;
		r[0] = "" + nsport;
		NameserviceMain.main(r);
		
	}

}
