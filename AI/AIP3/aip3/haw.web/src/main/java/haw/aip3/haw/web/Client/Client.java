package haw.aip3.haw.web.Client;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import haw.aip3.haw.web.Client.Commands.CommandType;

public class Client extends Thread{

	private boolean stop;
	private ObjectOutputStream ooutput;
	private Socket socket;
	private OutputStream output;
	
	public Client() {
		// TODO Auto-generated constructor stub
		stop = false;
		Socket = new Socket("127.0.0.1", 50000);
	}
	
	@Override
	public void run(){
		while(!stop){
			System.out.print("Enter something:");
			String input = System.console().readLine();
			
			CommandType ctype = Commands.whichCommand(input);
			
			executeCommand(ctype);
		}
	}
	
	public void executeCommand(CommandType ctype){
		switch (ctype) {
		case AUFTRAG_ERSTELLEN : auftragErstellen();
			break;
		case AUFTRAEGE_EINSEHEN : auftraegeEinsehen();
			break;
		case STOP : stopRunning();
			break;
		case UNGUELTIG : usage();
			break;
		default:
			break;
		}
	}
	
	public void auftragErstellen(){
		System.out.println("Enter your customer id:");
		String id = System.console().readLine();
		System.out.println("Enter the Bauteil");
		String bauteil = System.console().readLine();
		//Auftrag erstellen
		
	}
	
	public void auftraegeEinsehen(){
		System.out.println("Enter your customer id:");
		String id = System.console().readLine();
		//Aufträge einsehen
	}
	
	public void usage(){
		System.out.println("Invalid command! Enter AUFTRAG_ERSTELLEN or AUFTRAEGE_EINSEHEN:");
	}
	
	public void stopRunning(){
		this.stop = true;
	}

}
