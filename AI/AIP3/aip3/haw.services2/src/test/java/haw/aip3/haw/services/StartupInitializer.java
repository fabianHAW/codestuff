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
import haw.aip3.haw.repositories.produkt.BauteilRepository;
import haw.aip3.haw.services.auftragsverwaltung.AuftragsService;
import haw.aip3.haw.services.fertigungsverwaltung.FertigungService;
import haw.aip3.haw.services.produkt.ProduktService;

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
		Set<StuecklistenPosition> posSet1 = initStuecklistenPositionen1();

		System.out.println("Stueckliste 1 erzeugen und holen");
		// 100 days = 8640000000 ms
		Stueckliste stueckliste1 = this.produktService.erstelleStueckliste(
				"Steuckliste1", new Date(), new Date(
						System.currentTimeMillis() + 8640000000L), posSet1);

		ArrayList<Vorgang> vorgaenge = erstelleVorgaenge();
		Arbeitsplan arbeitsplan = this.produktService
				.erstelleArbeitsplan(vorgaenge);

		System.out.println("Komplexes Bauteil 1 erzeugen und holen");
		Bauteil b1 = produktService.erstelleKomplexesBauteil("Bauteil1",
				stueckliste1, arbeitsplan);

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
		Set<StuecklistenPosition> posSet2 = initStuecklistenPositionen2();

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

	private Set<StuecklistenPosition> initStuecklistenPositionen1() {
		Set<StuecklistenPosition> posList = new HashSet<StuecklistenPosition>();
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

	private Set<StuecklistenPosition> initStuecklistenPositionen2() {
		Set<StuecklistenPosition> posList = new HashSet<StuecklistenPosition>();
		posList.add(this.produktService.erstelleStuecklistenPosition(
				"Position4", 29, this.produktService
						.erstelleEinfachesBauteil("einfachesbauteil21")));
		posList.add(this.produktService.erstelleStuecklistenPosition(
				"Position5", 37, this.produktService
						.erstelleEinfachesBauteil("einfachesbauteil21")));
		return posList;
	}

	private ArrayList<Vorgang> erstelleVorgaenge() {
		ArrayList<Vorgang> vorgangList = new ArrayList<Vorgang>();
		vorgangList.add(this.produktService.erstelleVorgang(
				VorgangArtTyp.MONTAGE, 2000, 3000, 5000));
		vorgangList.add(this.produktService.erstelleVorgang(
				VorgangArtTyp.BEREITSTELLUNG, 3000, 4000, 6000));
		return vorgangList;
	}
}
