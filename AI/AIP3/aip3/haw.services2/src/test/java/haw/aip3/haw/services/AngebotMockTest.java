package haw.aip3.haw.services;

import java.util.Date;

import haw.aip3.haw.entities.Angebot;
import haw.aip3.haw.entities.Bauteil;
import haw.aip3.haw.repositories.AngebotRepository;

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
		
		@Autowired
		protected AngebotRepository angebotRepo;

		@Bean(name = "angebotService")
		public AngebotService angebotService() {
			return new AngebotServiceImpl() {
				
				@Override
				public void erstelleAngebot(Bauteil bauteil){
					//2 days = 172800000 ms
					this.angebotRepo.save(new Angebot(bauteil, new Date(), new Date(
							System.currentTimeMillis() + 172800000), 2.3d));
				}
				// this method is mocked by our fake implementation
				@Override
				public Angebot getAngebot(long id) {
					//2 days = 172800000 ms
					return new Angebot(null, new Date(), new Date(
							System.currentTimeMillis() + 172800000), 2.3d);
				}
			};
		}		
	}
	
	@Autowired
	private AngebotService angebotService;
	
	@Test
	public void findeAngbeot(){
		Angebot a1 = angebotService.getAngebot(1);
		angebotService.erstelleAngebot(null);
		Angebot a2 = angebotService.getAngebot(2);
		System.out.println(a1.getGueltigAb());
		System.out.println(a1.getGueltigBis());
		
		System.out.println(a2.getGueltigAb());
		System.out.println(a2.getGueltigBis());
	}
	
	
	
	
	
	
	

}
