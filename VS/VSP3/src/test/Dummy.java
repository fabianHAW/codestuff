package test;

import nameservice.NameserviceMain;

public class Dummy {

	public Dummy() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Ns n = new Ns();
		Sv s = new Sv();
		Cl c = new Cl();
		n.start();
		s.start();
		c.start();

	}

}
