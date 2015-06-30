package haw.aip3.haw.test;

import haw.aip3.haw.auftragsverwaltung.entities.Angebot;
import haw.aip3.haw.auftragsverwaltung.entities.KundenAuftrag;
import haw.aip3.haw.auftragsverwaltung.services.AuftragsService;
import haw.aip3.haw.base.entities.IStueckliste;
import haw.aip3.haw.base.entities.IStuecklistenPosition;
import haw.aip3.haw.base.entities.IVorgang;
import haw.aip3.haw.fertigungsverwaltung.entities.Fertigungsauftrag;
import haw.aip3.haw.fertigungsverwaltung.services.FertigungService;
import haw.aip3.haw.produkt.entities.Arbeitsplan;
import haw.aip3.haw.produkt.entities.Bauteil;
import haw.aip3.haw.produkt.entities.Stueckliste;
import haw.aip3.haw.produkt.entities.Vorgang.VorgangArtTyp;
import haw.aip3.haw.produkt.repositories.BauteilRepository;
import haw.aip3.haw.produkt.services.ProduktService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class StartupInitializer implements
		ApplicationListener<ContextRefreshedEvent> {
	// private static final Logger LOGGER = LoggerFactory
	// .getLogger(StartupInitializer.class);

	@Autowired
	private AuftragsService auftragsService;

	@Autowired
	private BauteilRepository bauteilRepo;

	@Autowired
	private ProduktService produktService;

	@Autowired
	private FertigungService fertigungsService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// startup of root context, refresh will trigger for initialization and
		// refresh of context
		if (event.getApplicationContext().getParent() == null) {
			configure();
		}
	}

	public void configure() {

		System.out
				.println("Stuecklisten-Positionen initialisieren und Stuecklisten-Positionen 1 holen");
		Set<IStuecklistenPosition> posSet1 = initStuecklistenPositionen1();

		System.out.println("Stueckliste 1 erzeugen und holen");
		// 100 days = 8640000000 ms
		IStueckliste stueckliste1 = this.produktService.erstelleStueckliste(
				"Steuckliste1", new Date(), new Date(
						System.currentTimeMillis() + 8640000000L), posSet1);

		ArrayList<IVorgang> vorgaenge = erstelleVorgaenge();
		Arbeitsplan arbeitsplan = this.produktService
				.erstelleArbeitsplan(vorgaenge);

		System.out.println("Komplexes Bauteil 1 erzeugen und holen");
		Bauteil b1 = produktService.erstelleKomplexesBauteil("Bauteil1",
				stueckliste1, arbeitsplan);
		System.out.println(b1.toString());
		System.out.println("Angebot 1 erzeugen");
		// 2 days = 172800000 ms
		Angebot a1 = this.auftragsService.erstelleAngebot(b1,
				new Date(System.currentTimeMillis() + 172800000), 33.33d);

		System.out.println("Kundenauftrag 1 erzeugen");
		KundenAuftrag ka1 = this.auftragsService.erzeugeKundenAuftrag(a1);

		System.out.println("Fertigungsauftrag 1 erzeugen");
		Fertigungsauftrag fa1 = this.fertigungsService
				.createFertigungsAuftrag(ka1);
		this.fertigungsService.saveFertigungsAuftrag(fa1);

		System.out.println("Stuecklisten-Positionen 2 holen");
		Set<IStuecklistenPosition> posSet2 = initStuecklistenPositionen2();

		System.out.println("Stueckliste 2 erstellen und holen");
		// 100 days = 8640000000 ms
		Stueckliste stueckliste2 = this.produktService.erstelleStueckliste(
				"Steuckliste2", new Date(), new Date(
						System.currentTimeMillis() + 8640000000L), posSet2);

		System.out.println("Komplexes Bauteil 2 erzeugen und holen");
		Bauteil b2 = produktService.erstelleKomplexesBauteil("Bauteil2",
				stueckliste2, arbeitsplan);

		System.out.println("Angebot 2 erzeugen und holen");
		// 3 days = 259200000 ms
		Angebot a2 = this.auftragsService.erstelleAngebot(b2,
				new Date(System.currentTimeMillis() + 259200000), 44.44d);

		System.out.println("Kundenauftrag 2 erzeugen");
		KundenAuftrag ka2 = this.auftragsService.erzeugeKundenAuftrag(a2);

		System.out.println("Fertigungsauftrag 2 erzeugen");
		Fertigungsauftrag fa2 = this.fertigungsService
				.createFertigungsAuftrag(ka2);
		this.fertigungsService.saveFertigungsAuftrag(fa2);

	}

	private Set<IStuecklistenPosition> initStuecklistenPositionen1() {
		Set<IStuecklistenPosition> posList = new HashSet<IStuecklistenPosition>();
		posList.add(this.produktService.erstelleStuecklistenPosition(
				"Position1", 3, this.produktService
						.erstelleEinfachesBauteil("einfachesbauteil11")));
		posList.add(this.produktService.erstelleStuecklistenPosition(
				"Position2", 7, this.produktService
						.erstelleEinfachesBauteil("einfachesbauteil12")));
		posList.add(this.produktService.erstelleStuecklistenPosition(
				"Position3", 20, this.produktService
						.erstelleEinfachesBauteil("einfachesbauteil13")));
		return posList;
	}

	private Set<IStuecklistenPosition> initStuecklistenPositionen2() {
		Set<IStuecklistenPosition> posList = new HashSet<IStuecklistenPosition>();
		posList.add(this.produktService.erstelleStuecklistenPosition(
				"Position4", 29, this.produktService
						.erstelleEinfachesBauteil("einfachesbauteil21")));
		posList.add(this.produktService.erstelleStuecklistenPosition(
				"Position5", 37, this.produktService
						.erstelleEinfachesBauteil("einfachesbauteil21")));
		return posList;
	}

	private ArrayList<IVorgang> erstelleVorgaenge() {
		ArrayList<IVorgang> vorgangList = new ArrayList<IVorgang>();
		vorgangList.add(this.produktService.erstelleVorgang(
				VorgangArtTyp.MONTAGE, 2000, 3000, 5000));
		vorgangList.add(this.produktService.erstelleVorgang(
				VorgangArtTyp.BEREITSTELLUNG, 3000, 4000, 6000));
		return vorgangList;
	}
}
