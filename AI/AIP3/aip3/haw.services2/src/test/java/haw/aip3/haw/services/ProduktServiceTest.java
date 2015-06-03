package haw.aip3.haw.services;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import haw.aip3.haw.entities.fertigungsverwaltung.Fertigungsauftrag;
import haw.aip3.haw.entities.produkt.Bauteil;
import haw.aip3.haw.entities.produkt.Stueckliste;
import haw.aip3.haw.entities.produkt.StuecklistenPosition;
import haw.aip3.haw.repositories.fertigungsverwaltung.FertigungsauftragRepository;
import haw.aip3.haw.repositories.produkt.BauteilRepository;
import haw.aip3.haw.repositories.produkt.StuecklisteRepository;
import haw.aip3.haw.repositories.produkt.StuecklistenPostionRepository;
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
@ContextConfiguration(classes = haw.aip3.haw.services.ProduktServiceTest.ContextConfiguration.class)
public class ProduktServiceTest {

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

	// **********************************************************************
	// ************************BauteileTests*********************************
	// **********************************************************************
	@Test
	public void erstelleEinfachesProdukt() {
		this.produktService.erstelleEinfachesBauteil("einfachesBauteil1");
		Bauteil b = this.bauteilRepo.findByName("einfachesBauteil1");
		Assert.notNull(b);
	}

	@Test
	public void erstelleKomplexesProdukt() {
		Stueckliste stueckliste = new Stueckliste();
		this.stuecklisteRepo.save(stueckliste);
		this.produktService.erstelleKomplexesBauteil("komplexesBauteil1",
				stueckliste);
		Bauteil b = this.bauteilRepo.findByName("komplexesBauteil1");
		Assert.notNull(b);
	}

	@Test
	public void findeBauteil() {
		this.produktService.erstelleEinfachesBauteil("einfachesBauteil2");

		Stueckliste stueckliste = new Stueckliste();
		this.stuecklisteRepo.save(stueckliste);
		this.produktService.erstelleKomplexesBauteil("komplexesBauteil2",
				stueckliste);

		Bauteil einfach = this.produktService.findeBauteil("einfachesBauteil2");
		Bauteil komplex = this.produktService.findeBauteil("komplexesBauteil2");

		Assert.notNull(einfach);
		Assert.notNull(komplex);
	}

	@Test
	public void getBauteilMitFertigungsauftrag() {
		Fertigungsauftrag fa = new Fertigungsauftrag();
		this.produktService.erstelleEinfachesBauteil("einfachesBauteil3");
		fa.setBauteil(this.produktService.findeBauteil("einfachesBauteil3"));
		this.fertigungsRepo.save(fa);
		Bauteil b = this.produktService.getBauteil(fa);

		Assert.notNull(b);
	}

	// Sinnvoll???
	@Test
	public void getBauteilMitBauteil() {
		this.produktService.erstelleEinfachesBauteil("einfachesBauteil4");
		Bauteil b = this.produktService.findeBauteil("einfachesBauteil4");
		Bauteil bNeu = this.produktService.getBauteil(b);

		Assert.notNull(bNeu);
	}

	// **********************************************************************
	// ***********************StuecklisteTests*******************************
	// **********************************************************************
	@Test
	public void erstelleStuecklisteGetStueckliste() {
		StuecklistenPosition stuecklistenPosition = new StuecklistenPosition(
				"stuecklistenPosition1", 2);
		this.stuecklistePostionRepo.save(stuecklistenPosition);

		// 100 days = 8640000000 ms
		this.stuecklisteService.erstelleStueckliste("stueckliste1", new Date(),
				new Date(System.currentTimeMillis() + 8640000000L),
				new HashSet<StuecklistenPosition>(
						Arrays.asList(stuecklistenPosition)));
		
		Stueckliste s = this.stuecklisteService.getStueckliste("stueckliste1");
		
		boolean inside = false;
		if(s.getStuecklisteName().equals("stueckliste1")) inside = true;
		Assert.notNull(s);
		Assert.isTrue(inside);
	}

	// **********************************************************************
	// ******************StuecklistenPositionTests***************************
	// **********************************************************************
	
	@Test
	public void erstelleStuecklistenPositionGetStuecklistenPosition(){
		this.stuecklistePositionService.erstelleStuecklistenPosition("stuecklistenPosition2", 4);
		StuecklistenPosition s = this.stuecklistePositionService.getStuecklistenPosition("stuecklistenPosition2");
		
		boolean menge = false;
		if(s.getMenge() == 4) menge = true;
		Assert.notNull(s);
		Assert.isTrue(menge);
		
	}
}
