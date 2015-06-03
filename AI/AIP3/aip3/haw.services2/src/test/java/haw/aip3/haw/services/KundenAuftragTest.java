package haw.aip3.haw.services;



import haw.aip3.haw.entities.auftragsverwaltung.Angebot;
import haw.aip3.haw.entities.auftragsverwaltung.KundenAuftrag;
import haw.aip3.haw.entities.produkt.Bauteil;
import haw.aip3.haw.services.auftragsverwaltung.AuftragsService;
import haw.aip3.haw.services.produkt.ProduktService;

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
	private AuftragsService auftragsService;
	
	@Autowired
	private ProduktService bauteilService;
	
	@Test
	public void findAuftragByID(){
		Long id1 = new Long(1);
		Long id2 = new Long(2);
		Long id3 = new Long(3);
		
		KundenAuftrag ka1 = auftragsService.getAuftrag(id1);
		Assert.notNull(ka1);
		
		auftragsService.markiereAuftrag(ka1);
		Assert.isTrue(ka1.isAbgeschlossen());
		
		KundenAuftrag ka2 = auftragsService.getAuftrag(id2);
		Assert.notNull(ka2);
		
		Angebot a1 = auftragsService.getAngebot(1);
		Assert.notNull(a1);
		Angebot ates = auftragsService.getAngebot(4);
		Assert.isNull(ates);
		
		auftragsService.erzeugeKundenAuftrag(a1);
		KundenAuftrag ka3 = auftragsService.getAuftrag(id3);
		Assert.notNull(ka3);
		
	}
	
	@Test
	public void testBauteile(){
		Bauteil b1 = bauteilService.findeBauteil("Bauteil1");
		Bauteil b2 = bauteilService.findeBauteil("Bauteil2");
		Assert.notNull(b1);
		Assert.notNull(b2);

		System.out.println(b1.getBauteilNr() + " " + b1.getName());
		System.out.println(b2.getBauteilNr() + " " + b2.getName());
	}
}
