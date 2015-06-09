package haw.aip3.haw.services;

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
@ContextConfiguration(classes = haw.aip3.haw.services.ProduktServiceTest.ContextConfiguration.class)
public class ProduktServiceTest {

	@ComponentScan(basePackages = "haw.aip3.haw")
	@Configuration
	static class ContextConfiguration {
	}

	@Autowired
	private ProduktService produktService;

	@Autowired
	private FertigungService fertigungsService;

	@Autowired
	private AuftragsService auftragService;

	// **********************************************************************
	// ************************BauteileTests*********************************
	// **********************************************************************
	@Test
	public void erstelleEinfachesProdukt() {
		Bauteil b = this.produktService
				.erstelleEinfachesBauteil("einfachesBauteil1");
		// Bauteil b = this.bauteilRepo.findByName("einfachesBauteil1").get(0);
		Assert.notNull(b);
	}

	@Test
	public void erstelleKomplexesProdukt() {
		Stueckliste stueckliste = this.produktService
				.getStueckliste("Stueckliste1");
		// Stueckliste stueckliste = new Stueckliste();
		// Arbeitsplan arbeitsplan = new Arbeitsplan();
		// this.stuecklisteRepo.save(stueckliste);
		Bauteil b = this.produktService.erstelleKomplexesBauteil(
				"komplexesBauteil1", stueckliste, null);
		// Bauteil b = this.bauteilRepo.findByName("komplexesBauteil1").get(0);
		Assert.notNull(b);
	}

	@Test
	public void findeBauteil() {
		this.produktService.erstelleEinfachesBauteil("einfachesBauteil2");

		// Stueckliste stueckliste = new Stueckliste();
		// Arbeitsplan arbeitsplan = new Arbeitsplan();
		// 100 days = 8640000000 ms
		StuecklistenPosition position = this.produktService
				.erstelleStuecklistenPosition("stuecklistenPosition24", 394,
						null);
		Set<StuecklistenPosition> setPostionen = new HashSet<StuecklistenPosition>();
		setPostionen.add(position);
		Stueckliste stueckliste = this.produktService.erstelleStueckliste(
				"stueckliste492", new Date(),
				new Date(System.currentTimeMillis() + 8640000000L),
				setPostionen);

		// this.stuecklisteRepo.save(stueckliste);
		this.produktService.erstelleKomplexesBauteil("komplexesBauteil2",
				stueckliste, null);

		Bauteil einfach = this.produktService.findeBauteil("einfachesBauteil2");
		Bauteil komplex = this.produktService.findeBauteil("komplexesBauteil2");

		Assert.notNull(einfach);
		Assert.notNull(komplex);
	}

	@Test
	public void getBauteilMitFertigungsauftrag() {
		// Fertigungsauftrag fa = new Fertigungsauftrag();
		Fertigungsauftrag fa = this.fertigungsService.findFertigungsauftrag(1L);
		Bauteil balt = this.produktService
				.erstelleEinfachesBauteil("einfachesBauteil3");
		fa.setBauteil(balt);
		// this.fertigungsRepo.save(fa);
		Bauteil bneu = this.produktService.getBauteil(fa);

		Assert.notNull(bneu);
	}

	// Sinnvoll???
	@Test
	public void getBauteilMitBauteil() {
		Bauteil b = this.produktService
				.erstelleEinfachesBauteil("einfachesBauteil4");
		// Bauteil b = this.produktService.findeBauteil("einfachesBauteil4");
		Bauteil bNeu = this.produktService.getBauteil(b);

		Assert.notNull(bNeu);
	}

	// **********************************************************************
	// ***********************StuecklisteTests*******************************
	// **********************************************************************
	@Test
	public void erstelleStuecklisteGetStueckliste() {
		// StuecklistenPosition stuecklistenPosition = new StuecklistenPosition(
		// "stuecklistenPosition1", 2, new EinfachesBauteil());
		StuecklistenPosition stuecklistenPosition = this.produktService
				.erstelleStuecklistenPosition("stuecklistenPosition1", 2, null);
		// this.stuecklistePostionRepo.save(stuecklistenPosition);

		// 100 days = 8640000000 ms
		this.produktService.erstelleStueckliste(
				"stueckliste1",
				new Date(),
				new Date(System.currentTimeMillis() + 8640000000L),
				new HashSet<StuecklistenPosition>(Arrays
						.asList(stuecklistenPosition)));

		Stueckliste s = this.produktService.getStueckliste("stueckliste1");

		boolean inside = false;
		if (s.getStuecklisteName().equals("stueckliste1"))
			inside = true;
		Assert.notNull(s);
		Assert.isTrue(inside);
	}

	// **********************************************************************
	// ******************StuecklistenPositionTests***************************
	// **********************************************************************

	@Test
	public void erstelleStuecklistenPositionGetStuecklistenPosition() {
		this.produktService.erstelleStuecklistenPosition(
				"stuecklistenPosition2", 4, null);
		StuecklistenPosition s = this.produktService
				.getStuecklistenPosition("stuecklistenPosition2");

		boolean menge = false;
		if (s.getMenge() == 4)
			menge = true;
		Assert.notNull(s);
		Assert.isTrue(menge);

	}

	@Test
	public void erstelleArbeitsplan() {
		// Vorgang v = new Vorgang(VorgangArtTyp.BEREITSTELLUNG, 1, 2, 3);
		Vorgang v = this.produktService.erstelleVorgang(VorgangArtTyp.MONTAGE,
				20, 39, 49);
		// vorgangRepo.save(v);
		ArrayList<Vorgang> vorgaenge = new ArrayList<Vorgang>(Arrays.asList(v));
		this.produktService.erstelleEinfachesBauteil("einfachesBauteil1");
		// Bauteil b = this.bauteilRepo.findByName("einfachesBauteil1").get(0);

		Arbeitsplan a1 = this.produktService.erstelleArbeitsplan(vorgaenge);
		// Arbeitsplan a1 = new Arbeitsplan(b, vorgaenge);
		// Arbeitsplan a1 = new Arbeitsplan(vorgaenge);

		// arbeitsplanRepo.save(a1);

		Assert.notNull(a1.getNr());

		Arbeitsplan a2 = this.produktService.getArbeitsplan(a1.getNr());
		Assert.isTrue(a1.equals(a2));
	}

	@Test
	public void erstelleArbeitsplaeneAusFertigungsauftrag() {
		// Fertigungsauftrag f = new Fertigungsauftrag(
		// auftragService.getAuftrag((long) 1));
		Fertigungsauftrag f = this.fertigungsService
				.createFertigungsAuftrag(this.auftragService.getAuftrag(1L));

		// Vorgang v = new Vorgang(VorgangArtTyp.BEREITSTELLUNG, 1, 2, 3);
		//Vorgang v = this.produktService.erstelleVorgang(
		//		VorgangArtTyp.BEREITSTELLUNG, 1, 2, 3);
		//ArrayList<Vorgang> vorgaenge = new ArrayList<Vorgang>(Arrays.asList(v));
		this.produktService.erstelleEinfachesBauteil("einfachesBauteil1");

		// StuecklistenPosition sp1 = new StuecklistenPosition(
		// "Rasenmaeher-Motor", 1, new EinfachesBauteil());

		StuecklistenPosition sp1 = this.produktService
				.erstelleStuecklistenPosition("Rasenmaeher-Motor", 1, null);
		StuecklistenPosition sp2 = this.produktService
				.erstelleStuecklistenPosition("Platte1", 2, null);

		StuecklistenPosition sp3 = this.produktService
				.erstelleStuecklistenPosition("Platte2", 42, null);
		// positionen1.add(sp1);

		Set<StuecklistenPosition> positionen = new HashSet<StuecklistenPosition>(
				Arrays.asList(sp1, sp2, sp3));

		Stueckliste sl = this.produktService.erstelleStueckliste("ABCD",
				new Date(), new Date(System.currentTimeMillis() + 200300),
				positionen);
		// Stueckliste sl = new Stueckliste("ABCD", new Date(), new Date(
		// System.currentTimeMillis() + 200300), positionen2);
		// Falscher Entwurf: Arbeitsplan soll Bauteil im Konstruktor bekommen,
		// aber
		// Komplexes Bauteil soll Arbeitsplan im Konstruktor bekommen: Henne -
		// Ei Problem.
		// Arbeitsplan a1 = new Arbeitsplan(vorgaenge);
		// Bauteil komplex = new KomplexesBauteil("Rasenmaeher", sl, a1);
		Bauteil komplex = this.produktService.erstelleKomplexesBauteil(
				"Rasenmaeher", sl, null);
		f.setBauteil(komplex);

		List<Arbeitsplan> a2 = this.produktService.erstelleArbeitsplaene(f);
		Assert.isTrue(a2.size() == 1);
	}
}
