package haw.aip3.haw.services;

import java.util.Date;

import haw.aip3.haw.entities.auftragsverwaltung.Angebot;
import haw.aip3.haw.entities.auftragsverwaltung.KundenAuftrag;
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
@ContextConfiguration(classes = haw.aip3.haw.services.AuftragsverwaltungTest.ContextConfiguration.class)
public class AuftragsverwaltungTest {

	@ComponentScan(basePackages = "haw.aip3.haw")
	@Configuration
	static class ContextConfiguration {
	}

	@Autowired
	private AuftragsService auftragsService;

	@Autowired
	private ProduktService bauteilService;

	// **********************************************************************
	// ************************AngebotTests**********************************
	// **********************************************************************

	@Test
	public void erstelleAngebot() {
		// 2 days = 172800000 ms
		Angebot a = this.auftragsService.erstelleAngebot(
				this.bauteilService.findeBauteil("Bauteil1"),
				new Date(System.currentTimeMillis() + 172800000), 2000);

		boolean preis = false;
		if (a.getPreis() == 2000)
			preis = true;
		Assert.notNull(a);
		Assert.isTrue(preis);
	}

	@Test
	public void getAngebot() {
		Angebot a = this.auftragsService.getAngebot(1);
		Assert.notNull(a);
	}

	// **********************************************************************
	// ***********************KundenauftragTests*****************************
	// **********************************************************************
	@Test
	public void erzeugeKundenAuftragGetAuftrag() {
		KundenAuftrag ka = this.auftragsService
				.erzeugeKundenAuftrag(this.auftragsService.getAngebot(2));

		Assert.notNull(ka);
	}

	@Test
	public void markiereAuftrag() {
		KundenAuftrag ka = this.auftragsService.getAuftrag(3L);
		this.auftragsService.markiereAuftrag(ka);

		Assert.isTrue(ka.isAbgeschlossen());
	}
}
