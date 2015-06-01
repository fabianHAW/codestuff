package test;

import java.util.ArrayList;
import java.util.Random;

public class SmallConcurrencyTest {

	public SmallConcurrencyTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		if (args.length != 4) {
			usage();
			System.exit(-1);
		}
		
		int nsport = Integer.parseInt(args[0]);
		String nshost = args[1];
		
		int servercount = Integer.parseInt(args[2]);
		int clientcount = Integer.parseInt(args[3]);
		
		ArrayList<ServerStartExpanded> server = startServer(nshost, nsport, servercount);
		ArrayList<ClientStartExpanded> clients = startClients(nshost, nsport, clientcount, server);
		
		start(server, clients);
		
	}
	
	public static void start(ArrayList<ServerStartExpanded> server, ArrayList<ClientStartExpanded> clients){
		for(ServerStartExpanded s : server){
			s.start();
		}
		try {
			Thread.sleep(1000*server.size());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(ClientStartExpanded c : clients){
			c.start();
		}
	}
	
	public static ArrayList<ServerStartExpanded> startServer(String nshost, int nsport, int servercount){
		ArrayList<ServerStartExpanded> server = new ArrayList<ServerStartExpanded>();
		for(int i = 0; i < servercount; i++){
			server.add(new ServerStartExpanded(nsport, nshost));
		}
		return server;
	}
	
	public static ArrayList<ClientStartExpanded> startClients(String nshost, int nsport, int clientcount, ArrayList<ServerStartExpanded> servers){
		ArrayList<ClientStartExpanded> clients = new ArrayList<ClientStartExpanded>();
		Random rand = new Random();
		for(int i = 0; i < clientcount; i++){
			System.out.println("Servantid: " + rand.nextInt(servers.size()));
			System.out.println("Servantnames: "  + servers.get(rand.nextInt(servers.size())).getServantNames());
			clients.add(new ClientStartExpanded(nsport, nshost, servers.get(rand.nextInt(servers.size())).getServantNames()));
		}
		return clients;
	}
	
	private static void usage() {
		System.out
				.println("SmallConcurrencyTest java/SmallConcurrencyTest <nameservice-port> <nameservice-host> <number-of-servers> <number-of-clients>");
	}
	

}
