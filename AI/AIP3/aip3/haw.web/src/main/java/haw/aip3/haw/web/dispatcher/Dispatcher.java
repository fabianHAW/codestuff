package haw.aip3.haw.web.dispatcher;

import org.springframework.boot.CommandLineRunner;

public class Dispatcher implements CommandLineRunner{

	Monitor monitor;
	
	public Dispatcher() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(String... arg0) throws Exception {
		// TODO Auto-generated method stub
		monitor = new Monitor();
	}
	


}
