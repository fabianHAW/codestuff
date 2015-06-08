package haw.aip3.haw.web.dispatcher;

import haw.aip3.haw.services.auftragsverwaltung.AuftragsService;
import haw.aip3.haw.web.Client.Commands.CommandType;
import haw.aip3.haw.web.boot.RequestADT;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Configuration
@EnableAutoConfiguration
@ConfigurationProperties(prefix = "dispatcher")
// the port property is prefixed by the application name
@PropertySource("classpath:application-nodump.properties")
public class Dispatcher extends Thread implements CommandLineRunner{

	private Monitor monitor;
	private ServerSocket serversocket;
	private ObjectInputStream input;
	private static final int LISTENPORT = 50003;
	private boolean isAlive;
	private String request;
	private ObjectOutputStream output;
	
	
	public Dispatcher() {
		this.monitor = new Monitor();
		this.monitor.start();
		try {
			this.serversocket = new ServerSocket(LISTENPORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.isAlive = true;
	}
	
	public static void main(String[] args){
		
	}

	@Override
	public void run(String... arg0) throws Exception {
//		Socket s = null;
//		while(this.isAlive){
////			s = this.serversocket.accept();
////			this.input = new ObjectInputStream(s.getInputStream());
////			new DispatcherThread(this.monitor, (RequestADT) this.input.readObject()).run();
//		}
		main(arg0);
	}
	
	@RequestMapping(value="/kundenauftrag", 
            method=RequestMethod.GET, 
            produces=MediaType.APPLICATION_JSON_VALUE)
    public String auftragAnlegen(@RequestParam(required = false, value = "*,*") String request) 
    {
		Socket s = this.monitor.getAliveServer();
		String result = null;
		try {
			this.output = new ObjectOutputStream(s.getOutputStream());
			this.output.writeObject(CommandType.AUFTRAG_ERSTELLEN.toString() + ";" + request);
			
			input = new ObjectInputStream(s.getInputStream());
			result = (String)input.readObject();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return result; // our object have name, id, label
    }
	
	
	public void dispatcherShutdown(){
		this.isAlive = false;
		try {
			this.serversocket.close();
		} catch (IOException e) {
			System.out.println(this.getClass() + ": dispatcher closed");
		}
	}

	
	private class DispatcherThread extends Thread{
		
		private Monitor monitor;
		private String request;
		private ServerSocket serversocket;
		private ObjectInputStream input;
		private ObjectOutputStream output;
		
		public DispatcherThread(Monitor monitor, String request){
			this.monitor = monitor;
			this.request = request;
		}
		
		@Override
		public void run(){
			Socket s = this.monitor.getAliveServer();
			try {
				this.output = new ObjectOutputStream(s.getOutputStream());
				this.output.writeObject(this.request);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
