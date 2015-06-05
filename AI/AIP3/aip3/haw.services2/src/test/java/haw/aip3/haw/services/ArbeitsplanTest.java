package haw.aip3.haw.services;




import haw.aip3.haw.entities.auftragsverwaltung.KundenAuftrag;
import haw.aip3.haw.entities.fertigungsverwaltung.Fertigungsauftrag;
import haw.aip3.haw.services.auftragsverwaltung.AuftragsService;
import haw.aip3.haw.services.fertigungsverwaltung.FertigungService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=haw.aip3.haw.services.ArbeitsplanTest.ContextConfiguration.class)
public class ArbeitsplanTest {

	
	@ComponentScan(basePackages = "haw.aip3.haw")
	@Configuration
    static class ContextConfiguration {}

	@Autowired
	private FertigungService fertigungService;
	
	@Autowired
	private AuftragsService kundenAuftragService;

	
	@Test
	public void createFertigungsAuftrag(){
		KundenAuftrag ka = kundenAuftragService.getAuftrag((long)1);
		Fertigungsauftrag fa = fertigungService.createFertigungsAuftrag(ka);
		Assert.notNull(fa);
	}
	
	
	@Test
	public void findFertigungsAuftrag(){
		KundenAuftrag ka =  kundenAuftragService.getAuftrag((long)1);
		Fertigungsauftrag fa = fertigungService.createFertigungsAuftrag(ka);
		fertigungService.saveFertigungsAuftrag(fa);
		Assert.notNull(fertigungService.findFertigungsauftrag(fa.getNr()));
	}
	
	@Test
	public void saveFertigungsAuftrag(){
		KundenAuftrag ka = kundenAuftragService.getAuftrag((long)1);
		Fertigungsauftrag fa = fertigungService.createFertigungsAuftrag(ka);
		fertigungService.saveFertigungsAuftrag(fa);
		Assert.isTrue(fa.equals(fertigungService.findFertigungsauftrag(fa.getNr())));
	}
	
	@Test
	public void deleteFertigungsAuftrag(){
		KundenAuftrag ka =  kundenAuftragService.getAuftrag((long)1);
		Fertigungsauftrag fa = fertigungService.createFertigungsAuftrag(ka);
		fertigungService.saveFertigungsAuftrag(fa);
		Assert.notNull(fertigungService.findFertigungsauftrag(fa.getNr()));
		fertigungService.deleteFertigungsauftrag(fa.getNr());
		Assert.isNull(fertigungService.findFertigungsauftrag(fa.getNr()));
	}
	

}
