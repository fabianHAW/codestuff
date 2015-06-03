package haw.aip3.haw.services;



import haw.aip3.haw.entities.Angebot;
import haw.aip3.haw.entities.Bauteil;
import haw.aip3.haw.entities.Fertigungsauftrag;
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
public class FertigungsauftragTest {

	
	@ComponentScan(basePackages = "haw.aip3.haw")
	@Configuration
    static class ContextConfiguration {}

	@Autowired
	private FertigungService fertigungService;

	
	@Test
	public void createFertigungsAuftrag(){
		KundenAuftrag ka = new KundenAuftrag();
		Fertigungsauftrag fa = fertigungService.createFertigungsAuftrag(ka);
		Assert.notNull(fa);
	}
	
	
	@Test
	public void findFertigungsAuftrag(){
		KundenAuftrag ka = new KundenAuftrag();
		Fertigungsauftrag fa = fertigungService.createFertigungsAuftrag(ka);
		fertigungService.saveFertigungsAuftrag(fa);
		Assert.notNull(fertigungService.findFertigungsauftrag(fa.getNr()));
	}
	
	@Test
	public void saveFertigungsAuftrag(){
		KundenAuftrag ka = new KundenAuftrag();
		Fertigungsauftrag fa = fertigungService.createFertigungsAuftrag(ka);
		fertigungService.saveFertigungsAuftrag(fa);
		Assert.isTrue(fa.equals(fertigungService.findFertigungsauftrag(fa.getNr())));
	}
	
	@Test
	public void deleteFertigungsAuftrag(){
		KundenAuftrag ka = new KundenAuftrag();
		Fertigungsauftrag fa = fertigungService.createFertigungsAuftrag(ka);
		fertigungService.saveFertigungsAuftrag(fa);
		Assert.notNull(fertigungService.findFertigungsauftrag(fa.getNr()));
		fertigungService.deleteFertigungsauftrag(fa.getNr());
		Assert.isNull(fertigungService.findFertigungsauftrag(fa.getNr()));
	}
	

}
