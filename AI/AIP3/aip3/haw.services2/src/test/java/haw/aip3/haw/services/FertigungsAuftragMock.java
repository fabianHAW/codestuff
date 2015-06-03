package haw.aip3.haw.services;

import java.util.Date;

import haw.aip3.haw.entities.Angebot;
import haw.aip3.haw.entities.Bauteil;
import haw.aip3.haw.entities.KundenAuftrag;
import haw.aip3.haw.repositories.AngebotRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = haw.aip3.haw.services.FertigungsAuftragMock.ContextConfiguration.class)
public class FertigungsAuftragMock {

	// no @Configuration, so it does only get picked up by this test
	@ComponentScan(basePackages = "haw.aip3.haw.services")
	// Note: there is no @PropertySource("classpath:application.properties") as
	// this is taken from DatabaseConfig.class
	static class ContextConfiguration {

		
		@Autowired
		protected AngebotRepository angebotRepo;

		@Bean(name = "auftragService")
		public KundenAuftragService auftragService(){
			return new KundenAuftragServiceImpl(){
				
				@Override
				public KundenAuftrag getAuftrag(Long id){
					return new KundenAuftrag();
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
