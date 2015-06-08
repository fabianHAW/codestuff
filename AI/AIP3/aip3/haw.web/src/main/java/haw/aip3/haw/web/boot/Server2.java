package haw.aip3.haw.web.boot;

import haw.aip3.haw.web.boot.MultiApplication.BaseApplication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableAutoConfiguration
@ConfigurationProperties(prefix="server2") // the port property is prefixed by the application name
@PropertySource("classpath:application-nodump.properties") // different properties for different spring contexts.
public class Server2 extends BaseApplication implements CommandLineRunner{

	IsAliveThread2 aliveThread;
	private boolean isAlive;
	public Server2() {
		// TODO Auto-generated constructor stub
	}
	

	public static void main(String[] args){
		System.out.println(Server2.class + " started");
		
		
	}
	
	@Override
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
