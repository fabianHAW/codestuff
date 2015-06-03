package haw.aip3.haw.services;

import haw.aip3.haw.entities.Angebot;
import haw.aip3.haw.entities.Bauteil;
import haw.aip3.haw.repositories.AngebotRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AngebotServiceImpl implements AngebotService{
	
	@Autowired
	protected AngebotRepository angebotRepo; 
    
	@Override
	public void erstelleAngebot(Bauteil bauteil) {
	}
	
	@Override
	public void erstelleAngebot() {
		this.angebotRepo.save(new Angebot());
	}

	@Override
	public Angebot getAngebot(long angebotNr) {
		return this.angebotRepo.findOne(angebotNr);
	}

}
