package applications.server;

import java.net.InetAddress;
import java.net.UnknownHostException;

import accessor_one.ClassOneAO;
import accessor_one.ClassOneImplBase;
import accessor_one.ClassTwoAO;
import accessor_one.ClassTwoImplBase;
import mware_lib.NameService;
import mware_lib.ObjectBroker;

public class Server {

	public Server() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String address = null;
		int port = Integer.parseInt(args[0]);
		//int port = 50001;
		try {
			address = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ObjectBroker broker = ObjectBroker.init(address, port, false);
		NameService nameservice = broker.getNameService();
		ClassOneImplBase c1 = new ClassOneAO();
		ClassTwoImplBase c2 = new ClassTwoAO();
		nameservice.rebind(c1, "c1");
		nameservice.rebind(c2, "c2");

	}
	


}
