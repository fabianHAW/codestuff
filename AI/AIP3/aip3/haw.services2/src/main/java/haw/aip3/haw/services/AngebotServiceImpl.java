package haw.aip3.haw.services;

import haw.aip3.haw.entities.Angebot;
import haw.aip3.haw.repositories.AngebotRepository;
import haw.aip3.haw.repositories.KundenAuftragRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AngebotServiceImpl implements AngebotService{
	
	@Autowired
	private AngebotRepository angebotRepo; 
    
	@Override
	public Angebot erstelleAngebot() {
		Angebot a = new Angebot();
		this.angebotRepo.save(a);
		return a;
	}

	@Override
	public Angebot getAngebot(long angebotNr) {
		return this.angebotRepo.findOne(angebotNr);
	}

}
