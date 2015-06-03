package haw.aip3.haw.services;

import haw.aip3.haw.entities.Angebot;
import haw.aip3.haw.entities.KundenAuftrag;
import haw.aip3.haw.repositories.KundenAuftragRepository;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KundenAuftragServiceImpl implements KundenAuftragService{

	@Autowired
	private KundenAuftragRepository auftragRepo; 
    
	@Override
	public void erzeugeKundenAuftrag(Angebot a) {
		this.auftragRepo.save(new KundenAuftrag(a, false, new Date()));
	}

	@Override
	public KundenAuftrag getAuftrag(Long id) {	
		return this.auftragRepo.findOne(id);
	}

	@Override
	public void markiereAuftrag(KundenAuftrag a) {		
		a.setAbgeschlossen(true);
	}

}
