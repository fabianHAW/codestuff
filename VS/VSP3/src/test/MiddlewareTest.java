package test;

public class MiddlewareTest {

	public MiddlewareTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		switch(Integer.valueOf(args[0])){
		case 0:
			ServerStart server = new ServerStart();
			server.start();
			break;
		case 1:
			ClientStart client = new ClientStart();
			client.start();
			break;
		}
	}
}
