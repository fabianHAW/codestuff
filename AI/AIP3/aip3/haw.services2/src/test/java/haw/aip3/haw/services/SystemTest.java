package haw.aip3.haw.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import haw.aip3.haw.entities.auftragsverwaltung.Angebot;
import haw.aip3.haw.entities.auftragsverwaltung.KundenAuftrag;
import haw.aip3.haw.entities.fertigungsverwaltung.Fertigungsauftrag;
import haw.aip3.haw.entities.produkt.Arbeitsplan;
import haw.aip3.haw.entities.produkt.Bauteil;
import haw.aip3.haw.entities.produkt.EinfachesBauteil;
import haw.aip3.haw.entities.produkt.KomplexesBauteil;
import haw.aip3.haw.entities.produkt.Stueckliste;
import haw.aip3.haw.entities.produkt.StuecklistenPosition;
import haw.aip3.haw.entities.produkt.Vorgang;
import haw.aip3.haw.entities.produkt.Vorgang.VorgangArtTyp;
import haw.aip3.haw.repositories.fertigungsverwaltung.FertigungsauftragRepository;
import haw.aip3.haw.repositories.produkt.ArbeitsplanRepository;
import haw.aip3.haw.repositories.produkt.BauteilRepository;
import haw.aip3.haw.repositories.produkt.StuecklisteRepository;
import haw.aip3.haw.repositories.produkt.StuecklistenPostionRepository;
import haw.aip3.haw.services.auftragsverwaltung.AuftragsService;
import haw.aip3.haw.services.fertigungsverwaltung.FertigungService;
import haw.aip3.haw.services.produkt.ProduktService;
import haw.aip3.haw.services.produkt.StuecklisteService;
import haw.aip3.haw.services.produkt.StuecklistenPositionService;

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
	private StuecklisteService stuecklisteService;

	@Autowired
	private StuecklistenPositionService stuecklistePositionService;

	@Autowired
	private BauteilRepository bauteilRepo;

	@Autowired
	private StuecklisteRepository stuecklisteRepo;
	
	@Autowired
	private StuecklistenPostionRepository stuecklistePostionRepo;

	@Autowired
	private FertigungsauftragRepository fertigungsRepo;
	
	@Autowired
	private ArbeitsplanRepository arbeitsplanRepo;

	@Autowired
	private AuftragsService auftragsService;

	@Autowired
	private FertigungService fertigungService;
	
	@Test 
	public void mpsSzenario(){
		
		/***
		 * 
		 */
		//Erstelle Angebot
		this.auftragsService.erstelleAngebot(
				produktService.findeBauteil("Bauteil2"),
				new Date(System.currentTimeMillis() + 172800000), 2000);
		
		Angebot a = this.auftragsService.getAngebot(3);

		boolean preis = false;
		if (a.getPreis() == 2000)
			preis = true;

		Assert.notNull(a);
		Assert.isTrue(preis);
		
		//Erzeuge KundenAuftrag
		this.auftragsService.erzeugeKundenAuftrag(this.auftragsService
				.getAngebot(3));
		KundenAuftrag ka = this.auftragsService.getAuftrag(3L);
		
		Assert.notNull(ka);
		
		//Erstelle Fertigungsplan
		Fertigungsauftrag f = new Fertigungsauftrag(ka);
		Vorgang v = new Vorgang(VorgangArtTyp.BEREITSTELLUNG, 1, 2, 3);
		ArrayList<Vorgang> vorgaenge = new ArrayList<Vorgang>(Arrays.asList(v));
		this.produktService.erstelleEinfachesBauteil("einfachesBauteil1");
		Set<StuecklistenPosition> positionen1 = new HashSet<StuecklistenPosition>(); 
		
		StuecklistenPosition sp1 = new StuecklistenPosition("Rasenmaeher-Motor", 1, new EinfachesBauteil());
		positionen1.add(sp1);
		
		
		Set<StuecklistenPosition> positionen2 = new HashSet<StuecklistenPosition>(
				Arrays.asList(new StuecklistenPosition("Platte1", 2, new EinfachesBauteil()),
						new StuecklistenPosition("Platte2", 42, new EinfachesBauteil()))); 
		
		Stueckliste sl = new Stueckliste("ABCD", new Date(), new Date(System.currentTimeMillis() + 200300), positionen2);
		//Falscher Entwurf: Arbeitsplan soll Bauteil im Konstruktor bekommen, aber
		//Komplexes Bauteil soll Arbeitsplan im Konstruktor bekommen: Henne - Ei Problem.
		Arbeitsplan a1 = new Arbeitsplan(null, vorgaenge);
		Bauteil komplex = new KomplexesBauteil("Rasenmaeher", sl, a1);
		f.setBauteil(komplex);
		fertigungService.saveFertigungsAuftrag(f);
		//False da List ein Bauteil verglichen wird?
		Assert.isTrue(f.equals(fertigungService.findFertigungsauftrag(f.getNr())));
		
		List<Arbeitsplan> a2 = produktService.erstelleArbeitsplaene(f);
		Assert.isTrue(a2.size() == 1);
		/**
		 * 
		 */
		//Erzeuge Angebot
		// 2 days = 172800000 ms

		
	
		
		//Erzeuge Fertigungsauftrag
		Fertigungsauftrag fa = fertigungService.createFertigungsAuftrag(ka);
		Assert.notNull(fa);
		
		List<Arbeitsplan> arbeitsplaene = produktService.erstelleArbeitsplaene(fa);
		Assert.isTrue(arbeitsplaene.size() != 0);
		
		//Markiere Auftrag als abgeschlossen
		this.auftragsService.markiereAuftrag(ka);
		Assert.isTrue(ka.isAbgeschlossen());
		
		//ToDo: Die Nr. 8 u. 9 des Anwendungsfalls.
		
	}
}