package mps.auftrag.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import mps.auftrag.entities.Auftrag;
import mps.auftrag.entities.Auftragsposition;
import mps.auftrag.services.AuftragService;
import mps.kunde.entities.Kunde;
import mps.kunde.repositories.KundeRepository;
import mps.produkt.entities.Produkt;
import mps.produkt.services.ProduktService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class StartupInitializer implements ApplicationListener<ContextRefreshedEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(StartupInitializer.class);

	private static final int CUSTOMER_COUNT = 5;
	
	private static final int PRODUCT_COUNT 	= 10;
	
	@Autowired
	private AuftragService auftragService;
	
	@Autowired
	private ProduktService produktRepository;
	
	@Autowired
	private KundeRepository kundeRepository;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// startup of root context, refresh will trigger for initialization and refresh of context
		if (event.getApplicationContext().getParent() == null) {
			configure();
		}
	}

	private void configure() {
		LOGGER.info("setup db.");
		
		List<Kunde> kunden = new ArrayList<>();
		List<Produkt> produkte = new ArrayList<>();
		List<Auftrag> auftraege = new ArrayList<>();
		
		// fixture
		for(int i=0; i<PRODUCT_COUNT; i++) {
			Produkt p = new Produkt();
			p.setName("Produkt "+i);
			produktRepository.save(p);
			produkte.add(p);
		}
		for(int i=0; i<CUSTOMER_COUNT; i++) {
			Kunde k = new Kunde("Kunde "+i);
			String stadt = i%2==0?"Berlin":"Hamburg";
			k.setStadt(stadt);
			kundeRepository.save(k);
			kunden.add(k);
		}
		
		for(int i=0; i<10; i++) {
			Auftrag a = new Auftrag();
			a.setKunde(kunden.get(i%CUSTOMER_COUNT));
			List<Auftragsposition> ps = a.getPosten();
			for(int j=0; j<(i%5); j++) {
				Auftragsposition p = new Auftragsposition(a,produkte.get((i+j)%PRODUCT_COUNT));
				p.setAnzahl((i+j)%7);
				p.setPreis(new BigDecimal(i+"."+j));
				ps.add(p);
			}
			// this saves Auftragsposition as well via Cascade
			// we use the service here for node4j integration
			auftragService.save(a); 
			auftraege.add(a);
		}

	}
}
