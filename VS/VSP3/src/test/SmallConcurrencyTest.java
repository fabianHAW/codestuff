package test;

import java.util.ArrayList;
import java.util.Random;

/**
 *  Verweise zum Entwurf:
 * <Klassendiagramm> : Implementierung der Klasse im Package test
 * 
 * Startet einen Nebenläufigkeitstest mit x Servern und y Clients wobei sichergestellt ist,
 * dass Alle Methoden von ClassOneImplBase (accessor_one und accessor_two) sowie ClassTwoImplBase
 * aufgerufen werden.
 * 
 * @author Francis
 *
 */
public class SmallConcurrencyTest {

	public SmallConcurrencyTest() {
	}

	/**
	 * args[0] = <nameservice-port>
	 * args[1] = <nameservice-host>
	 * args[2] = <anzahl-zu-startende-server>
	 * args[3] = <anzahl-zu-startende-clients>
	 * @param args
	 */
	public static void main(String[] args) {
		
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
	
	/**
	 * Startet erst die Server und wartet pro Server 1 Sekunde, damit der Nameservice Zeit hat die
	 * Objekte zu registrieren.
	 * Startet danach die Clients.
	 * @param server Liste mit Servern.
	 * @param clients Liste mi Clients.
	 */
	public static void start(ArrayList<ServerStartExpanded> server, ArrayList<ClientStartExpanded> clients){
		for(ServerStartExpanded s : server){
			s.start();
		}
		try {
			Thread.sleep(1000*server.size());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for(ClientStartExpanded c : clients){
			c.start();
		}
	}
	
	/**
	 * Erzeugt servercount viele Server.
	 * @param nshost Host des Nameservice
	 * @param nsport Port des Nameservice
	 * @param servercount Anzahl der zu startenden Server
	 * @return Liste mit Servern.
	 */
	public static ArrayList<ServerStartExpanded> startServer(String nshost, int nsport, int servercount){
		ArrayList<ServerStartExpanded> server = new ArrayList<ServerStartExpanded>();
		for(int i = 0; i < servercount; i++){
			server.add(new ServerStartExpanded(nsport, nshost));
		}
		return server;
	}
	
	/**
	 * Erzeugt clientcount viele Clients.
	 * @param nshost Host des Nameservice
	 * @param nsport Port des Nameservice
	 * @param clientcount Anzahl der zu startenden Clients
	 * @return Liste mit Clients.
	 */
	public static ArrayList<ClientStartExpanded> startClients(String nshost, int nsport, int clientcount, ArrayList<ServerStartExpanded> servers){
		ArrayList<ClientStartExpanded> clients = new ArrayList<ClientStartExpanded>();
		Random rand = new Random();
		for(int i = 0; i < clientcount; i++){
			clients.add(new ClientStartExpanded(nsport, nshost, servers.get(rand.nextInt(servers.size())).getServantNames()));
		}
		return clients;
	}
	
	private static void usage() {
		System.out
				.println("SmallConcurrencyTest java/SmallConcurrencyTest <nameservice-port> <nameservice-host> <number-of-servers> <number-of-clients>");
	}
	

}
