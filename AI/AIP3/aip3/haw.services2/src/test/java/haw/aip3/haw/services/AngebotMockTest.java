package haw.aip3.haw.services;

import java.util.Date;

import haw.aip3.haw.entities.Angebot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = haw.aip3.haw.services.AngebotMockTest.ContextConfiguration.class)
public class AngebotMockTest {

	// no @Configuration, so it does only get picked up by this test
	@ComponentScan(basePackages = "haw.aip3.haw.services")
	// Note: there is no @PropertySource("classpath:application.properties") as
	// this is taken from DatabaseConfig.class
	static class ContextConfiguration {

		@Autowired
		private KundenAuftragService auftragRepo;

		@Bean(name = "angebotService")
		public AngebotService angebotService() {
			return new AngebotServiceImpl() {
				// this method is mocked by our fake implementation
				@Override
				public Angebot getAngebot(long id) {
					//2 days = 172800000 ms
					return new Angebot(new Date(), new Date(
							System.currentTimeMillis() + 172800000), 2.3d);
				}
			};
		}
	}
	
	@Autowired
	private AngebotService angebotService;
	
	@Test
	public void findeAngbeot(){
		Angebot a = angebotService.getAngebot(1);
		System.out.println(a.getGueltigAb());
		System.out.println(a.getGueltigBis());
	}
	
	
	
	
	
	
	

}
