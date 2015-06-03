package haw.aip3.haw.services;

import haw.aip3.haw.repositories.produkt.BauteilRepository;
import haw.aip3.haw.services.auftragsverwaltung.AuftragsService;
import haw.aip3.haw.services.produkt.ProduktService;
import haw.aip3.haw.services.produkt.StuecklisteService;
import haw.aip3.haw.services.produkt.StuecklistenPositionService;
import haw.aip3.haw.entities.produkt.Bauteil;
import haw.aip3.haw.entities.produkt.Stueckliste;
import haw.aip3.haw.entities.produkt.StuecklistenPosition;

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
	private ProduktService bauteilService;

	@Autowired
	private StuecklisteService stuecklisteService;

	@Autowired
	private StuecklistenPositionService stuecklistenPositionService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// startup of root context, refresh will trigger for initialization and
		// refresh of context
		if (event.getApplicationContext().getParent() == null) {
			configure();
		}
	}

	public void configure() {

		System.out.println("Stuecklisten-Positionen initialisieren");
		initStuecklistenPositionen();

		System.out.println("Stuecklisten-Positionen 1 holen");
		Set<StuecklistenPosition> posSet1 = getStuecklistenPostionenSet1();
		
		System.out.println("Stueckliste 1 erzeugen");
		// 100 days = 8640000000 ms
		this.stuecklisteService.erstelleStueckliste("Steuckliste1", new Date(),
				new Date(System.currentTimeMillis() + 8640000000L), posSet1);

		System.out.println("Stueckliste 1 holen");
		Stueckliste stueckliste1 = this.stuecklisteService
				.getStueckliste("Stueckliste1");
		
		System.out.println("Komplexes Bauteil 1 erzeugen");
		bauteilService.erstelleKomplexesBauteil("Bauteil1", stueckliste1);
		
		System.out.println("Komplexes Bauteil 1 holen");
		Bauteil b1 = bauteilService.findeBauteil("Bauteil1");

		System.out.println("Angebot 1 erzeugen");
		// 2 days = 172800000 ms
		this.auftragsService.erstelleAngebot(b1,
				new Date(System.currentTimeMillis() + 172800000), 33.33d);
		
		System.out.println("Kundenauftrag 1 erzeugen");
		this.auftragsService.erzeugeKundenAuftrag(this.auftragsService.getAngebot(1));

		System.out.println("Stuecklisten-Positionen 2 holen");
		Set<StuecklistenPosition> posSet2 = getStuecklistenPostionenSet2();
		// 100 days = 8640000000 ms
		this.stuecklisteService.erstelleStueckliste("Steuckliste2", new Date(),
				new Date(System.currentTimeMillis() + 8640000000L), posSet2);

		System.out.println("Stueckliste 2 holen");
		Stueckliste stueckliste2 = this.stuecklisteService
				.getStueckliste("Stueckliste2");
		
		System.out.println("Komplexes Bauteil 2 erzeugen");
		bauteilService.erstelleKomplexesBauteil("Bauteil2", stueckliste2);
		
		System.out.println("Komplexes Bauteil 2 holen");
		Bauteil b2 = bauteilService.findeBauteil("Bauteil2");
		System.out.println("bauteil2: " + b2);

		System.out.println("Angebot 2 erzeugen");
		// 3 days = 259200000 ms
		this.auftragsService.erstelleAngebot(b2,
				new Date(System.currentTimeMillis() + 259200000), 44.44d);

		System.out.println("Kundenauftrag 2 erzeugen");
		this.auftragsService.erzeugeKundenAuftrag(this.auftragsService.getAngebot(2));

		System.out.println(this.auftragsService.getAngebot(1));
		System.out.println(this.auftragsService.getAngebot(2));
	}

	private void initStuecklistenPositionen() {
		this.stuecklistenPositionService.erstelleStuecklistenPosition(
				"Position1", 3);
		this.stuecklistenPositionService.erstelleStuecklistenPosition(
				"Position2", 7);
		this.stuecklistenPositionService.erstelleStuecklistenPosition(
				"Position3", 20);
		this.stuecklistenPositionService.erstelleStuecklistenPosition(
				"Position4", 29);
		this.stuecklistenPositionService.erstelleStuecklistenPosition(
				"Position5", 37);
	}

	private Set<StuecklistenPosition> getStuecklistenPostionenSet1() {
		Set<StuecklistenPosition> posList = new HashSet<StuecklistenPosition>();
		posList.add(this.stuecklistenPositionService
				.getStuecklistenPosition("Position1"));
		posList.add(this.stuecklistenPositionService
				.getStuecklistenPosition("Position2"));
		posList.add(this.stuecklistenPositionService
				.getStuecklistenPosition("Position3"));
		return posList;
	}

	private Set<StuecklistenPosition> getStuecklistenPostionenSet2() {
		Set<StuecklistenPosition> posList = new HashSet<StuecklistenPosition>();
		posList.add(this.stuecklistenPositionService
				.getStuecklistenPosition("Position4"));
		posList.add(this.stuecklistenPositionService
				.getStuecklistenPosition("Position5"));
		return posList;
	}
}
