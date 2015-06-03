package haw.aip3.haw.services;

import haw.aip3.haw.entities.Angebot;
import haw.aip3.haw.entities.Bauteil;
import haw.aip3.haw.repositories.AngebotRepository;
import haw.aip3.haw.repositories.BauteilRepository;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class StartupInitializer implements
		ApplicationListener<ContextRefreshedEvent> {
//	private static final Logger LOGGER = LoggerFactory
//			.getLogger(StartupInitializer.class);

	@Autowired
	private KundenAuftragService auftragService;

	@Autowired
	private AngebotRepository angebotRepo;

	@Autowired
	private BauteilRepository bauteilRepo;
	
	@Autowired
	private BauteilService bauteilService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// startup of root context, refresh will trigger for initialization and
		// refresh of context
		if (event.getApplicationContext().getParent() == null) {
			configure();
		}
	}

	public void configure() {

		bauteilService.erstelleKomplexesBauteil("Bauteil1");
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

		bauteilService.erstelleKomplexesBauteil("Bauteil2");
		Bauteil b2 = bauteilService.findeBauteil("Bauteil2");

		// 3 days = 259200000 ms
		Angebot a2 = new Angebot(b2, new Date(), new Date(
				System.currentTimeMillis() + 259200000), 333.3d);
		angebotRepo.save(a2);

		this.auftragService.erzeugeKundenAuftrag(a2);

	}

}
