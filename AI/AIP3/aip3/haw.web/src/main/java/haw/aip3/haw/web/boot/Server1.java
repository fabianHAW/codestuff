package haw.aip3.haw.web.boot;

import haw.aip3.haw.web.boot.MultiApplication.BaseApplication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@EnableAutoConfiguration
@ConfigurationProperties(prefix="server1") // the port property is prefixed by the application name
@PropertySource("classpath:application-nodump.properties") // different properties for different spring contexts.
public class Server1 extends BaseApplication implements CommandLineRunner{

	IsAliveThread1 aliveThread;
	
	public Server1() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args){
		System.out.println(Server1.class + " started");
	}

	@Override
	public void run(String... arg0) throws Exception {
		// TODO Auto-generated method stub
		aliveThread = new IsAliveThread1();
		main(arg0);
		
	}


}
