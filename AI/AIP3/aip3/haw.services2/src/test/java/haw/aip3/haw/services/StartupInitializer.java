package haw.aip3.haw.services;

import haw.aip3.haw.entities.Angebot;
import haw.aip3.haw.entities.KundenAuftrag;
import haw.aip3.haw.repositories.AngebotRepository;
import haw.aip3.haw.repositories.KundenAuftragRepository;

import java.util.Date;

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

	@Autowired
	private KundenAuftragRepository auftragRepo;
	
	@Autowired
	private AngebotRepository angebotRepo;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// startup of root context, refresh will trigger for initialization and refresh of context
		if (event.getApplicationContext().getParent() == null) {
			configure();
		}
	}
	
	public void configure(){

		Angebot a1 = new Angebot();
		angebotRepo.save(a1);
		

		KundenAuftrag ka1 = new KundenAuftrag(a1, false, new Date());
		auftragRepo.save(ka1);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Angebot a2 = new Angebot();
		angebotRepo.save(a2);
		
		KundenAuftrag ka2 = new KundenAuftrag(a2, false, new Date());
		auftragRepo.save(ka2);

		
	}
	

}
