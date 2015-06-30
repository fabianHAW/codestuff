package haw.aip3.haw.test;

import haw.aip3.haw.auftragsverwaltung.services.AuftragsService;
import haw.aip3.haw.base.entities.IArbeitsplan;
import haw.aip3.haw.base.entities.IBauteil;
import haw.aip3.haw.base.entities.IStuecklistenPosition;
import haw.aip3.haw.base.entities.IVorgang;
import haw.aip3.haw.fertigungsverwaltung.entities.Fertigungsauftrag;
import haw.aip3.haw.fertigungsverwaltung.services.FertigungService;
import haw.aip3.haw.produkt.entities.Arbeitsplan;
import haw.aip3.haw.produkt.entities.Bauteil;
import haw.aip3.haw.produkt.entities.Stueckliste;
import haw.aip3.haw.produkt.entities.StuecklistenPosition;
import haw.aip3.haw.produkt.entities.Vorgang.VorgangArtTyp;
import haw.aip3.haw.produkt.services.ProduktService;

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
@ContextConfiguration(classes = TestConfig.class)
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
		Assert.notNull(b);
	}

	@Test
	public void erstelleKomplexesProdukt() {
		Stueckliste stueckliste = this.produktService
				.getStueckliste("Stueckliste1");
		Bauteil b = this.produktService.erstelleKomplexesBauteil(
				"komplexesBauteil1", stueckliste,
				this.produktService.getArbeitsplan(0L));
		Assert.notNull(b);
	}

	@Test
	public void findeBauteil() {
		IBauteil b = this.produktService
				.erstelleEinfachesBauteil("einfachesBauteil2");
		// 100 days = 8640000000 ms
		IStuecklistenPosition position = this.produktService
				.erstelleStuecklistenPosition("stuecklistenPosition24", 394, b);
		Set<IStuecklistenPosition> setPostionen = new HashSet<IStuecklistenPosition>();
		setPostionen.add(position);
		Stueckliste stueckliste = this.produktService.erstelleStueckliste(
				"stueckliste492", new Date(),
				new Date(System.currentTimeMillis() + 8640000000L),
				setPostionen);

		this.produktService.erstelleKomplexesBauteil("komplexesBauteil2",
				stueckliste, this.produktService.getArbeitsplan(0L));

		Bauteil einfach = this.produktService.findeBauteil("einfachesBauteil2");
		Bauteil komplex = this.produktService.findeBauteil("komplexesBauteil2");

		Assert.notNull(einfach);
		Assert.notNull(komplex);
	}

//	@Test
//	public void getBauteilMitFertigungsauftrag() {
//		Fertigungsauftrag fa = this.fertigungsService.findFertigungsauftrag(1L);
//		Bauteil balt = this.produktService
//				.erstelleEinfachesBauteil("einfachesBauteil3");
//		fa.setBauteil(balt);
//		Bauteil bneu = this.produktService.getBauteil(fa);
//
//		Assert.notNull(bneu);
//	}

	@Test
	public void getBauteilMitBauteil() {
		Bauteil b = this.produktService
				.erstelleEinfachesBauteil("einfachesBauteil4");
		Bauteil bNeu = this.produktService.getBauteil(b);

		Assert.notNull(bNeu);
	}

	// **********************************************************************
	// ***********************StuecklisteTests*******************************
	// **********************************************************************
	@Test
	public void erstelleStuecklisteGetStueckliste() {
		StuecklistenPosition stuecklistenPosition = this.produktService
				.erstelleStuecklistenPosition("stuecklistenPosition1", 2,
						this.produktService.findeBauteil("einfachesbauteil11"));

		// 100 days = 8640000000 ms
		this.produktService.erstelleStueckliste(
				"stueckliste1",
				new Date(),
				new Date(System.currentTimeMillis() + 8640000000L),
				new HashSet<IStuecklistenPosition>(Arrays
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
				"stuecklistenPosition2", 4,
				this.produktService.findeBauteil("einfachesbauteil12"));
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
		IVorgang v = this.produktService.erstelleVorgang(VorgangArtTyp.MONTAGE,
				20, 39, 49);

		ArrayList<IVorgang> vorgaenge = new ArrayList<IVorgang>(Arrays.asList(v));
		this.produktService.erstelleEinfachesBauteil("einfachesBauteil1");

		Arbeitsplan a1 = this.produktService.erstelleArbeitsplan(vorgaenge);

		Assert.notNull(a1.getNr());

		Arbeitsplan a2 = this.produktService.getArbeitsplan(a1.getNr());
		Assert.isTrue(a1.equals(a2));
	}

	@Test
	public void erstelleArbeitsplaeneAusFertigungsauftrag() {
		Fertigungsauftrag f = this.fertigungsService
				.createFertigungsAuftrag(this.auftragService.getAuftrag(1L));

		Bauteil b = this.produktService.erstelleEinfachesBauteil("einfachesBauteil1");

		StuecklistenPosition sp1 = this.produktService
				.erstelleStuecklistenPosition("Rasenmaeher-Motor", 1, b);
		StuecklistenPosition sp2 = this.produktService
				.erstelleStuecklistenPosition("Platte1", 2, b);

		StuecklistenPosition sp3 = this.produktService
				.erstelleStuecklistenPosition("Platte2", 42, b);

		Set<IStuecklistenPosition> positionen = new HashSet<IStuecklistenPosition>(
				Arrays.asList(sp1, sp2, sp3));

		Stueckliste sl = this.produktService.erstelleStueckliste("ABCD",
				new Date(), new Date(System.currentTimeMillis() + 200300),
				positionen);
		
		Bauteil komplex = this.produktService.erstelleKomplexesBauteil(
				"Rasenmaeher", sl, this.produktService.getArbeitsplan(0L));
		f.setBauteil(komplex);

		List<IArbeitsplan> a2 = this.produktService.erstelleArbeitsplaene(f);
		Assert.isTrue(a2.size() == 1);
	}
}
