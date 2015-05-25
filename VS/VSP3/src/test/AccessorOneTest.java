package test;

public class AccessorOneTest {

	public AccessorOneTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NameserviceStart nameservice = new NameserviceStart(50000);
		ServerStart server = new ServerStart();
		ClientStart client = new ClientStart();
		nameservice.start();
		AccessorOneTest.waiting();
		server.start();
		AccessorOneTest.waiting();
		client.start();
	

	}
	
	public static void waiting(){
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
