package haw.aip3.haw.web.boot;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import haw.aip3.haw.services.auftragsverwaltung.AuftragsService;
import haw.aip3.haw.services.auftragsverwaltung.AuftragsServiceImpl;
import haw.aip3.haw.web.Client.Commands.CommandType;
import haw.aip3.haw.web.boot.MultiApplication.BaseApplication;



import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@EnableAutoConfiguration
@ConfigurationProperties(prefix = "server1")
// the port property is prefixed by the application name
@PropertySource("classpath:application-nodump.properties")
// different properties for different spring contexts.
public class Server1 extends BaseApplication implements CommandLineRunner {

	IsAliveThread1 aliveThread;
	private static boolean isAlive;
	private ObjectOutputStream out;
	private static ServerSocket socket;
	private static AuftragsService auftragsService;

	public Server1() {
		// TODO Auto-generated constructor stub
		auftragsService = new AuftragsServiceImpl();
		try {
			socket = new ServerSocket(50001);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ServerSocket getSocket(){
		return socket;
	}

	public  void main(String[] args) {
		System.out.println(Server1.class + " started");
	
		while (isAlive) {
			
			try {
				Socket s = socket.accept();
				ObjectInputStream input = new ObjectInputStream(
						s.getInputStream());
				String request = (String) input.readObject();
				handleRequest(s, request);
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		}
	}
	
	public void print(){
	System.out.println("alive");
}

	public void handleRequest(Socket s, String request) {
		String command = request.substring(0, request.indexOf(";"));
		if (command.equals(CommandType.AUFTRAG_ERSTELLEN.toString())) {
			auftragErstellen(request.substring(request.indexOf(";"),
					request.length()));
		} else if (command.equals(CommandType.AUFTRAEGE_EINSEHEN.toString())) {
			auftraegeEinsehen(request.substring(request.indexOf(";"),
					request.length()));
		}
	}

	private void auftraegeEinsehen(String substring) {
		// TODO Auto-generated method stub
		System.out.println("Aufträge einsehen: " + substring);
	}

	private void auftragErstellen(String substring) {
		// TODO Auto-generated method stub
		System.out.println("Aufträge erstellen: " + substring);
	}


	public void run(String... arg0) throws Exception {
		// TODO Auto-generated method stub
		isAlive = true;
		aliveThread = new IsAliveThread1(this);
		aliveThread.start();
		main(arg0);

	}

	public boolean isAlive() {
		return isAlive;
	}

}
