package haw.aip3.haw.services;



import haw.aip3.haw.entities.Angebot;
import haw.aip3.haw.entities.KundenAuftrag;
import haw.aip3.haw.services.KundenAuftragService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=haw.aip3.haw.services.KundenAuftragTest.ContextConfiguration.class)
public class KundenAuftragTest {

	
	@ComponentScan(basePackages = "haw.aip3.haw")
	@Configuration
    static class ContextConfiguration {}

	@Autowired
	private KundenAuftragService auftragsService;
	
	@Autowired
	private AngebotService angebotService;
	
	@Test
	public void findAuftragByID(){
		Long id1 = new Long(1);
		Long id2 = new Long(2);
		Long id3 = new Long(3);
		
		KundenAuftrag ka1 = auftragsService.getAuftrag(id1);
		Assert.notNull(ka1);
		
		auftragsService.markiereAuftrag(ka1);
		System.out.println(ka1.isAbgeschlossen());
		
		KundenAuftrag ka2 = auftragsService.getAuftrag(id2);
		Assert.notNull(ka2);
		
		Angebot a1 = angebotService.erstelleAngebot();
		Angebot ates = angebotService.getAngebot(4);
		System.out.println(ates);
		
		auftragsService.erzeugeKundenAuftrag(a1);
		KundenAuftrag ka3 = auftragsService.getAuftrag(id3);
		Assert.notNull(ka3);
		
	}
}
