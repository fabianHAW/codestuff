package haw.aip3.haw.web.Client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;





import haw.aip3.haw.web.Client.Commands.CommandType;
import haw.aip3.haw.web.boot.IsAliveThread2;

//@Configuration
//@EnableAutoConfiguration
//@ConfigurationProperties(prefix = "client")
//// the port property is prefixed by the application name
//@PropertySource("classpath:application-nodump.properties")
//// different properties for different spring contexts.
public class Client extends Thread implements CommandLineRunner{

	private boolean stop;
	private RestTemplate restTemplate;
	private String dispatcherURL = "http://localhost:8083";
	
	
	public Client() {
		// TODO Auto-generated constructor stub
		stop = false;
		restTemplate = new RestTemplate();

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
		String success = restTemplate.getForObject(dispatcherURL + "/kundenauftrag/{" + id + ","+ bauteil +"}", String.class, 1L);
		System.out.println("Success: " + success);
	}
	
	public void auftraegeEinsehen(){
		System.out.println("Enter your customer id:");
		String id = System.console().readLine();
		//Aufträge einsehen
		String success = restTemplate.getForObject(dispatcherURL + "/kundenauftrag/{" + id + "}", String.class, 1L);
		System.out.println("Aufträge: " + success);
	}
	
	public void usage(){
		System.out.println("Invalid command! Enter AUFTRAG_ERSTELLEN or AUFTRAEGE_EINSEHEN:");
	}
	
	public void stopRunning(){
		this.stop = true;
	}

	@Override
	public void run(String... arg0) throws Exception {
		// TODO Auto-generated method stub
		while(!stop){
			System.out.print("Enter something:");
			String input = System.console().readLine();
			
			CommandType ctype = Commands.whichCommand(input);
			
			executeCommand(ctype);
		}
		
	}

}
