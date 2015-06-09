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
import haw.aip3.haw.repositories.fertigungsverwaltung.VorgangRepository;
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
	
	@Autowired
	private VorgangRepository vorgangRepo;
	
	
	@Test 
	public void mpsSzenario(){
		//Erzeuge Angebot
		produktService.erstelleEinfachesBauteil("Rasenmaehermotorlöl");
		produktService.erstelleEinfachesBauteil("Rasenmaeher-Motor");
		EinfachesBauteil einfachesBauteil2 = /*(EinfachesBauteil) produktService.findeBauteil("Rasenmaehermotorlöl");//*/new EinfachesBauteil("Rasenmaehermotor1");
		EinfachesBauteil einfachesBauteil1 = /*(EinfachesBauteil) produktService.findeBauteil("Rasenmaeher-Motor");*/new EinfachesBauteil("Rasenmaehermotor2");
		
		StuecklistenPosition stuecklistePosition1 = new StuecklistenPosition("StuecklistenPosition1", 2, einfachesBauteil1);
		stuecklistePostionRepo.save(stuecklistePosition1);
		StuecklistenPosition stuecklistenPosition2 = new StuecklistenPosition("StuecklistenPosition2", 1, einfachesBauteil2);
		stuecklistePostionRepo.save(stuecklistenPosition2);
		
		Set<StuecklistenPosition> positionen = new HashSet<StuecklistenPosition>(Arrays.asList(stuecklistePosition1, stuecklistenPosition2));
		Stueckliste stueckliste = new Stueckliste("Stueckliste1", new Date(), new Date(System.currentTimeMillis() + (long)259200000), positionen);
		stuecklisteRepo.save(stueckliste);
		
		Vorgang vorgang1 = new Vorgang(VorgangArtTyp.BEREITSTELLUNG, 1, 2, 3);
		vorgangRepo.save(vorgang1);
		
		ArrayList<Vorgang> vorgaenge = new ArrayList<Vorgang>(Arrays.asList(vorgang1));
		Arbeitsplan arbeitsplan = new Arbeitsplan(null, vorgaenge);
		//arbeitsplanRepo.save(arbeitsplan);
		
		produktService.erstelleKomplexesBauteil("Rasenmaeher", stueckliste, arbeitsplan);//new KomplexesBauteil("Rasenmaeher", stueckliste, arbeitsplan);
		KomplexesBauteil bauteilKomplex = (KomplexesBauteil) produktService.findeBauteil("Rasenmaeher");
		
		Angebot angebot = auftragsService.erstelleAngebot(bauteilKomplex, new Date(), 199);//new Angebot(bauteilKomplex, new Date(), new Date(System.currentTimeMillis() + (long)259200000), 199);
		
		
		//Erzeuge KundenAuftrag
		KundenAuftrag kundenAuftrag = auftragsService.erzeugeKundenAuftrag(angebot);//new KundenAuftrag(angebot, false, new Date());
		
		
		//Erzeuge Fertigungsauftrag
		Fertigungsauftrag fertigungsauftrag = fertigungService.createFertigungsAuftrag(kundenAuftrag);
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