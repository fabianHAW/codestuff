package haw.aip3.haw.auftragsverwaltung;

import haw.aip3.haw.auftragsverwaltung.entities.Angebot;
import haw.aip3.haw.auftragsverwaltung.services.AuftragsService;
import haw.aip3.haw.produkt.entities.Bauteil;
import haw.aip3.haw.produkt.services.ProduktService;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class StartupInitializerAuftrag implements
		ApplicationListener<ContextRefreshedEvent> {
	// private static final Logger LOGGER = LoggerFactory
	// .getLogger(StartupInitializer.class);

	@Autowired
	private AuftragsService auftragsService;

	@Autowired
	private ProduktService produktService;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// startup of root context, refresh will trigger for initialization and
		// refresh of context
		if (event.getApplicationContext().getParent() == null) {
			configure();
		}
	}

	public void configure() {

		Bauteil b1 = produktService.erstelleKomplexesBauteil("Bauteil1",
				null, null);
		
		System.out.println("Angebot 1 erzeugen");
		// 2 days = 172800000 ms
		Angebot a1 = this.auftragsService.erstelleAngebot(b1,
				new Date(System.currentTimeMillis() + 172800000), 33.33d);

		System.out.println("Kundenauftrag 1 erzeugen");
		this.auftragsService.erzeugeKundenAuftrag(a1);

		Bauteil b2 = produktService.erstelleKomplexesBauteil("Bauteil2",
				null, null);

		System.out.println("Angebot 2 erzeugen und holen");
		// 3 days = 259200000 ms
		Angebot a2 = this.auftragsService.erstelleAngebot(b2,
				new Date(System.currentTimeMillis() + 259200000), 44.44d);

		System.out.println("Kundenauftrag 2 erzeugen");
		this.auftragsService.erzeugeKundenAuftrag(a2);


	}

}
