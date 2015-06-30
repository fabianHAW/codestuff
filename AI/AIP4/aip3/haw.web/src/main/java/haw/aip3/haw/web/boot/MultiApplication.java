package haw.aip3.haw.web.boot;

import haw.aip3.haw.config.AppConfiguration;
import haw.aip3.haw.services.StartupInitializer;
import haw.aip3.haw.services.auftragsverwaltung.AuftragsService;
import haw.aip3.haw.services.auftragsverwaltung.AuftragsServiceImpl;
import haw.aip3.haw.services.fertigungsverwaltung.FertigungService;
import haw.aip3.haw.services.produkt.ProduktService;
import haw.aip3.haw.web.StartupInitializerWeb;
import haw.aip3.haw.web.Client.Commands.CommandType;
import haw.aip3.haw.web.controller.MainController;
import haw.aip3.haw.web.dispatcher.Dispatcher;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;




public class MultiApplication {
	
	@Configuration
	@PropertySource("classpath:application-dump.properties") // this allows us to configure our database via the application.properties file
	public static class DatabaseSetupConfiguration{}
	
	public static class BaseApplication {
		private int port = 8080;
		public void setPort(int port) {this.port = port;}
		public int getPort(){return this.port;}
	
		// This is necessary to programmatically define the port of the application
		@Bean
		public EmbeddedServletContainerFactory servletContainer() {
			System.out.println("Starting server at port: "+port);
			return new JettyEmbeddedServletContainerFactory(getPort());
		}
	}
	
	@Configuration
	@EnableAutoConfiguration
	@ConfigurationProperties(prefix="server1") // the port property is prefixed by the application name
	@PropertySource("classpath:application-nodump.properties") // different properties for different spring contexts.
	public static class Server1 extends BaseApplication {
		IsAliveThread1 aliveThread;
		private static boolean isAlive;
		private ObjectOutputStream out;
		private static ServerSocket socket;
		private static AuftragsService auftragsService;

		public Server1() {
			// TODO Auto-generated constructor stub
			auftragsService = new AuftragsServiceImpl();
//			try {
//				socket = new ServerSocket(50001);
//				
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		
		public ServerSocket getSocket(){
			return socket;
		}

		public void main(String[] args) {
			System.out.println("Application1 started");
		
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

		@RequestMapping(value="/kundenauftrag", 
	            method=RequestMethod.GET, 
	            produces=MediaType.APPLICATION_JSON_VALUE)
		private void auftraegeEinsehen(@RequestParam(required = false, value = "*,*") String request) {
			// TODO Auto-generated method stub
			System.out.println("Aufträge einsehen: " + request);
			
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
	
	@Configuration
	@EnableAutoConfiguration
	@ConfigurationProperties(prefix="server2") // the port property is prefixed by the application name
	@PropertySource("classpath:application-nodump.properties") // different properties for different spring contexts.
	public static class Server2 extends BaseApplication {

		IsAliveThread2 aliveThread;
		private static boolean isAlive;
		private ObjectOutputStream out;
		private static ServerSocket socket;
		private static AuftragsService auftragsService;
		public Server2() {
			// TODO Auto-generated constructor stub
			auftragsService = new AuftragsServiceImpl();
//			try {
//				socket = new ServerSocket(50002);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		
		public ServerSocket getSocket(){
			return socket;
		}
		

		public void main(String[] args){
			System.out.println("Application2 started");
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
		
		@RequestMapping(value="/kundenauftrag", 
	            method=RequestMethod.GET, 
	            produces=MediaType.APPLICATION_JSON_VALUE)
		private void auftraegeEinsehen(@RequestParam(required = false, value = "*,*") String request) {
			// TODO Auto-generated method stub
			System.out.println("Aufträge einsehen: " + request);
		}

		private void auftragErstellen(String substring) {
			// TODO Auto-generated method stub
			System.out.println("Aufträge erstellen: " + substring);
		}
		

		public void run(String... arg0) throws Exception {
			// TODO Auto-generated method stub
			isAlive = true;
			aliveThread = new IsAliveThread2(this);
			aliveThread.start();
			main(arg0);
		}
		
		public boolean isAlive(){
			return isAlive;
		}


	}
	
	public static void main(String[] args) throws SQLException {
		// setup database server
		try {
			new Server().runTool(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Database Server started");
		
		// populate data with configuration application.properties
		try(ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(
				DatabaseSetupConfiguration.class, // the application
				AppConfiguration.class, // the configuration of this application services and entities (see spring.services)
				StartupInitializerWeb.class // the data population
			)) {
//			UserService us = ctx.getBean(UserService.class);
//	  		System.out.println("users with 'm': "+us.getUsers("m"));
	  		String s = ctx.getEnvironment().getProperty("javax.persistence.schema-generation.database.action");
			System.out.println(s);
		}
		
		//startServer(Dispatcher.class);
		
//		Dispatcher d = new Dispatcher();
//		Server1 s1 = new Server1();
//		Server2 s2 = new Server2();
//		d.start();
//		try {
//			s1.run(new String[0]);
//			s1.run(new String[0]);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		startServer(Server1.class);
		startServer(Server2.class);
//		
//		startServer(Server1.class);
//		startServer(Server2.class);
//		startServer(Dispatcher.class);
//		startServer(Client.class);
		
	//	startServer(Client.class);
	}

	private static void startServer(Class<?/* extends BaseApplication*/> config) {
		
		Thread serverThread = new Thread(()->{
	    	ConfigurableApplicationContext ctx = SpringApplication.run(
	    			new Object[]{
	    					config, // the application
	    					AppConfiguration.class, // the configuration of this application services and entities (see spring.services)
	    					MainController.class // the main controller to supply the rest interface to the outside world
	    			}, new String[0]); 
	  	      
	    	// Through this you can test if beans are available and 
	    	// what result they return.
//			 UserService us = ctx.getBean(UserService.class);
//			 System.out.println("users with 'm': "+us.getUsers("m"));
	    	ProduktService f = ctx.getBean(ProduktService.class);
	    	f.erstelleEinfachesBauteil("BLABLABLA");
	    	
	    	System.out.println("PRODUKTSERVICE: " + f.findeBauteil("BLABLABLA").getName());
//			 //see that this configuration does not drop our database
	  		String s = ctx.getEnvironment().getProperty("javax.persistence.schema-generation.database.action");
			System.out.println(s);
			
		});
		
		serverThread.start();
	}
}
