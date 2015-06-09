package haw.aip3.haw.services;

import haw.aip3.haw.entities.auftragsverwaltung.Angebot;
import haw.aip3.haw.entities.auftragsverwaltung.KundenAuftrag;
import haw.aip3.haw.entities.fertigungsverwaltung.Fertigungsauftrag;
import haw.aip3.haw.entities.produkt.Arbeitsplan;
import haw.aip3.haw.entities.produkt.Bauteil;
import haw.aip3.haw.entities.produkt.Stueckliste;
import haw.aip3.haw.entities.produkt.StuecklistenPosition;
import haw.aip3.haw.entities.produkt.Vorgang;
import haw.aip3.haw.entities.produkt.Vorgang.VorgangArtTyp;
import haw.aip3.haw.services.auftragsverwaltung.AuftragsService;
import haw.aip3.haw.services.fertigungsverwaltung.FertigungService;
import haw.aip3.haw.services.produkt.ProduktService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = haw.aip3.haw.services.SystemTest.ContextConfiguration.class)
public class SystemTest {

	@ComponentScan(basePackages = "haw.aip3.haw")
	@Configuration
	static class ContextConfiguration {
	}

	@Autowired
	private ProduktService produktService;

	@Autowired
	private AuftragsService auftragsService;

	@Autowired
	private FertigungService fertigungService;

	@Test
	public void mpsSzenario() {
		// Erzeuge Angebot
		produktService.erstelleEinfachesBauteil("Rasenmaehermotorlöl");
		produktService.erstelleEinfachesBauteil("Rasenmaeher-Motor");
		Bauteil einfachesBauteil2 = this.produktService
				.erstelleEinfachesBauteil("Rasenmaehermotor1");
		Bauteil einfachesBauteil1 = this.produktService
				.erstelleEinfachesBauteil("Rasenmaehermotor2");

		StuecklistenPosition stuecklistePosition1 = this.produktService
				.erstelleStuecklistenPosition("StuecklistenPosition1", 2,
						einfachesBauteil1);
		StuecklistenPosition stuecklistenPosition2 = this.produktService
				.erstelleStuecklistenPosition("StuecklistenPosition2", 1,
						einfachesBauteil2);

		Set<StuecklistenPosition> positionen = new HashSet<StuecklistenPosition>(
				Arrays.asList(stuecklistePosition1, stuecklistenPosition2));
		Stueckliste stueckliste = this.produktService.erstelleStueckliste(
				"Stueckliste111", new Date(),
				new Date(System.currentTimeMillis() + (long) 259200000),
				positionen);

		Vorgang vorgang1 = this.produktService.erstelleVorgang(
				VorgangArtTyp.BEREITSTELLUNG, 1, 2, 3);

		ArrayList<Vorgang> vorgaenge = new ArrayList<Vorgang>(
				Arrays.asList(vorgang1));

		Arbeitsplan arbeitsplan = this.produktService
				.erstelleArbeitsplan(vorgaenge);

		Bauteil bauteilKomplex = this.produktService.erstelleKomplexesBauteil(
				"Rasenmaeher", stueckliste, arbeitsplan);
		// new KomplexesBauteil("Rasenmaeher", stueckliste,
		// arbeitsplan);
		// KomplexesBauteil bauteilKomplex = (KomplexesBauteil) produktService
		// .findeBauteil("Rasenmaeher");

		Angebot angebot = this.auftragsService.erstelleAngebot(bauteilKomplex,
				new Date(), 199);// new Angebot(bauteilKomplex, new Date(), new
									// Date(System.currentTimeMillis() +
									// (long)259200000), 199);

		// Erzeuge KundenAuftrag
		KundenAuftrag kundenAuftrag = this.auftragsService
				.erzeugeKundenAuftrag(angebot);// new KundenAuftrag(angebot,
												// false, new Date());

		// Erzeuge Fertigungsauftrag
		Fertigungsauftrag fertigungsauftrag = this.fertigungService
				.createFertigungsAuftrag(kundenAuftrag);
		this.fertigungService.saveFertigungsAuftrag(fertigungsauftrag);
		Assert.isTrue(fertigungsauftrag.equals(fertigungService
				.findFertigungsauftrag(fertigungsauftrag.getNr())));

		// Erzeuge Arbeitspläne
		List<Arbeitsplan> arbeitsplaene = produktService
				.erstelleArbeitsplaene(fertigungsauftrag);
		Assert.isTrue(arbeitsplaene.size() == 1);
		// Komplexe Bauteile bauen
		this.produktService.komplexesBauteilBauen(arbeitsplaene);

		kundenAuftrag.setAbgeschlossen(true);
		Assert.isTrue(kundenAuftrag.isAbgeschlossen());
	}
}