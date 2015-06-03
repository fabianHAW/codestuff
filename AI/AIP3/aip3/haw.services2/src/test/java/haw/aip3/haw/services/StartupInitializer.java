package haw.aip3.haw.services;

import haw.aip3.haw.entities.Angebot;
import haw.aip3.haw.entities.Bauteil;
import haw.aip3.haw.entities.Stueckliste;
import haw.aip3.haw.repositories.AngebotRepository;
import haw.aip3.haw.repositories.BauteilRepository;
import haw.aip3.haw.entities.StuecklistenPosition;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
	private KundenAuftragService auftragService;

	@Autowired
	private AngebotRepository angebotRepo;

	@Autowired
	private BauteilRepository bauteilRepo;

	@Autowired
	private BauteilService bauteilService;

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

		this.stuecklistenPositionService.erstelleStuecklistenPosition(
				"Position1", 3);
		this.stuecklistenPositionService.erstelleStuecklistenPosition(
				"Position2", 7);
		this.stuecklistenPositionService.erstelleStuecklistenPosition(
				"Position3", 20);

		Set<StuecklistenPosition> posList1 = new HashSet<StuecklistenPosition>();
		posList1.add(this.stuecklistenPositionService
				.getStuecklistenPosition("Position1"));
		posList1.add(this.stuecklistenPositionService
				.getStuecklistenPosition("Position2"));
		posList1.add(this.stuecklistenPositionService
				.getStuecklistenPosition("Position3"));

		// 100 days = 8640000000 ms
		this.stuecklisteService.erstelleStueckliste("Steuckliste1", new Date(),
				new Date(System.currentTimeMillis() + 8640000000L), posList1);
		
		
		
		this.stuecklistenPositionService.erstelleStuecklistenPosition(
				"Position4", 29);
		this.stuecklistenPositionService.erstelleStuecklistenPosition(
				"Position5", 37);

		Set<StuecklistenPosition> posList2 = new HashSet<StuecklistenPosition>();
		posList2.add(this.stuecklistenPositionService
				.getStuecklistenPosition("Position4"));
		posList2.add(this.stuecklistenPositionService
				.getStuecklistenPosition("Position5"));

		// 100 days = 8640000000 ms
		this.stuecklisteService.erstelleStueckliste("Steuckliste2", new Date(),
				new Date(System.currentTimeMillis() + 8640000000L), posList2);

		Stueckliste stueckliste1 = this.stuecklisteService.getStueckliste("Stueckliste1");
		bauteilService.erstelleKomplexesBauteil("Bauteil1", stueckliste1);
		Bauteil b1 = bauteilService.findeBauteil("Bauteil1");

		// 2 days = 172800000 ms
		Angebot a1 = new Angebot(b1, new Date(), new Date(
				System.currentTimeMillis() + 172800000), 333.3d);
		angebotRepo.save(a1);

		this.auftragService.erzeugeKundenAuftrag(a1);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Stueckliste stueckliste2 = this.stuecklisteService.getStueckliste("Stueckliste2");
		bauteilService.erstelleKomplexesBauteil("Bauteil2", stueckliste2);
		Bauteil b2 = bauteilService.findeBauteil("Bauteil2");

		// 3 days = 259200000 ms
		Angebot a2 = new Angebot(b2, new Date(), new Date(
				System.currentTimeMillis() + 259200000), 333.3d);
		angebotRepo.save(a2);

		this.auftragService.erzeugeKundenAuftrag(a2);

	}

}
