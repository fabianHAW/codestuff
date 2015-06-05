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
		//Erzeuge Angebot
		EinfachesBauteil einfachesBauteil2 = new EinfachesBauteil("Rasenmaehermotoröl");
		EinfachesBauteil einfachesBauteil1 = new EinfachesBauteil("Rasenmaeher-Motor");
		StuecklistenPosition stuecklistePosition1 = new StuecklistenPosition("StuecklistenPosition1", 2, einfachesBauteil1);
		StuecklistenPosition stuecklistenPosition2 = new StuecklistenPosition("StuecklistenPosition2", 1, einfachesBauteil2);
		Set<StuecklistenPosition> positionen = new HashSet<StuecklistenPosition>(Arrays.asList(stuecklistePosition1, stuecklistenPosition2));
		Stueckliste stueckliste = new Stueckliste("Stueckliste1", new Date(), new Date(System.currentTimeMillis() + (long)259200000), positionen);
		Vorgang vorgang1 = new Vorgang(VorgangArtTyp.BEREITSTELLUNG, 1, 2, 3);
		ArrayList<Vorgang> vorgaenge = new ArrayList<Vorgang>(Arrays.asList(vorgang1));
		Arbeitsplan arbeitsplan = new Arbeitsplan(null, vorgaenge);
		KomplexesBauteil bauteilKomplex = new KomplexesBauteil("Rasenmaeher", stueckliste, arbeitsplan);
		Angebot angebot = new Angebot(bauteilKomplex, new Date(), new Date(System.currentTimeMillis() + (long)259200000), 199);
		
		//Erzeuge KundenAuftrag
		KundenAuftrag kundenAuftrag = new KundenAuftrag(angebot, false, new Date());
		
		//Erzeuge Fertigungsauftrag
		Fertigungsauftrag fertigungsauftrag = new Fertigungsauftrag(kundenAuftrag);
		
		fertigungService.saveFertigungsAuftrag(fertigungsauftrag);
		Assert.isTrue(fertigungsauftrag.equals(fertigungService.findFertigungsauftrag(fertigungsauftrag.getNr())));
		
		//Erzeuge Arbeitspläne
		List<Arbeitsplan> arbeitsplaene = produktService.erstelleArbeitsplaene(fertigungsauftrag);
		Assert.isTrue(arbeitsplaene.size() == 1);
		//Komplexe Bauteile bauen
		produktService.komplexesBauteilBauen(arbeitsplaene);
		
		kundenAuftrag.setAbgeschlossen(true);
		Assert.isTrue(kundenAuftrag.isAbgeschlossen());
	}
}