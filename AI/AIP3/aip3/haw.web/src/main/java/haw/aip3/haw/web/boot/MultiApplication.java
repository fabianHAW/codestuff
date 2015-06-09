package haw.aip3.haw.web.boot;

import haw.aip3.haw.config.AppConfiguration;
import haw.aip3.haw.web.StartupInitializerWeb;
import haw.aip3.haw.web.controller.MainController;

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
	@ConfigurationProperties(prefix="application1") // the port property is prefixed by the application name
	@PropertySource("classpath:application-dump.properties") // different properties for different spring contexts.
	public static class Application1 extends BaseApplication {
//		public static Server1 server;
//		
//		public Application1(){
//			server = new Server1();
//		}
	}
	
	@Configuration
	@EnableAutoConfiguration
	@ConfigurationProperties(prefix="application2") // the port property is prefixed by the application name
	@PropertySource("classpath:application-nodump.properties") // different properties for different spring contexts.
	public static class Application2 extends BaseApplication {}
	
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
		startServer(Application1.class);
		startServer(Application2.class);
//		
//		startServer(Server1.class);
//		
//		
//		startServer(Server2.class);
		
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
//			 //see that this configuration does not drop our database
	  		String s = ctx.getEnvironment().getProperty("javax.persistence.schema-generation.database.action");
			System.out.println(s);
			
		});
		
		serverThread.start();
	}
}
