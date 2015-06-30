package haw.aip3.haw.test;

import haw.aip3.haw.auftragsverwaltung.entities.Angebot;
import haw.aip3.haw.auftragsverwaltung.entities.KundenAuftrag;
import haw.aip3.haw.auftragsverwaltung.services.AuftragsService;
import haw.aip3.haw.base.entities.IArbeitsplan;
import haw.aip3.haw.base.entities.IStueckliste;
import haw.aip3.haw.base.entities.IStuecklistenPosition;
import haw.aip3.haw.base.entities.IVorgang;
import haw.aip3.haw.fertigungsverwaltung.entities.Fertigungsauftrag;
import haw.aip3.haw.fertigungsverwaltung.services.FertigungService;
import haw.aip3.haw.produkt.entities.Arbeitsplan;
import haw.aip3.haw.produkt.entities.Bauteil;
import haw.aip3.haw.produkt.entities.StuecklistenPosition;
import haw.aip3.haw.produkt.entities.Vorgang.VorgangArtTyp;
import haw.aip3.haw.produkt.services.ProduktService;

import java.util.ArrayList;
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
@ContextConfiguration(classes = TestConfig.class)
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
		// // Erzeuge Angebot
		Bauteil einfachesBauteil1 = this.produktService
				.erstelleEinfachesBauteil("Rasenmaehermotorlöl");
		Bauteil einfachesBauteil2 = this.produktService
				.erstelleEinfachesBauteil("Rasenmaeher-Motor");

		// Bauteil einfachesBauteil1 = this.produktService
		// .erstelleEinfachesBauteil("Rasenmaehermotor1");
		// Bauteil einfachesBauteil2 = this.produktService
		// .erstelleEinfachesBauteil("Rasenmaehermotor2");

		StuecklistenPosition stuecklistePosition1 = this.produktService
				.erstelleStuecklistenPosition("StuecklistenPosition1", 2,
						einfachesBauteil1);
		StuecklistenPosition stuecklistenPosition2 = this.produktService
				.erstelleStuecklistenPosition("StuecklistenPosition2", 1,
						einfachesBauteil2);

		Set<IStuecklistenPosition> positionen = new HashSet<IStuecklistenPosition>();

		positionen.add(stuecklistePosition1);
		positionen.add(stuecklistenPosition2);

		IStueckliste stueckliste = this.produktService.erstelleStueckliste(
				"Stueckliste111", new Date(),
				new Date(System.currentTimeMillis() + (long) 259200000),
				positionen);

		ArrayList<IVorgang> vorgaenge = new ArrayList<IVorgang>();
		vorgaenge.add(this.produktService.erstelleVorgang(
				VorgangArtTyp.MONTAGE, 4, 5, 6));
		vorgaenge.add(this.produktService.erstelleVorgang(
				VorgangArtTyp.BEREITSTELLUNG, 1, 2, 3));

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
		List<IArbeitsplan> arbeitsplaene = produktService
				.erstelleArbeitsplaene(fertigungsauftrag);
		Assert.isTrue(arbeitsplaene.size() == 1);
		// Komplexe Bauteile bauen
		this.produktService.komplexesBauteilBauen(arbeitsplaene);

		kundenAuftrag.setAbgeschlossen(true);
		Assert.isTrue(kundenAuftrag.isAbgeschlossen());
	}
}