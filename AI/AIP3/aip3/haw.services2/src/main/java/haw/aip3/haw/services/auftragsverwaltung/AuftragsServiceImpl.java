package haw.aip3.haw.services.auftragsverwaltung;

import haw.aip3.haw.entities.auftragsverwaltung.Angebot;
import haw.aip3.haw.entities.auftragsverwaltung.KundenAuftrag;
import haw.aip3.haw.entities.produkt.Bauteil;
import haw.aip3.haw.repositories.auftragsverwaltung.AngebotRepository;
import haw.aip3.haw.repositories.auftragsverwaltung.KundenAuftragRepository;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuftragsServiceImpl implements AuftragsService{

	@Autowired
	protected AngebotRepository angebotRepo; 

	@Autowired
	private KundenAuftragRepository auftragRepo; 
	
	@Override
	public void erstelleAngebot(Bauteil bauteil, Date gueltigBis, double preis) {
		this.angebotRepo.save(new Angebot(bauteil, new Date(), gueltigBis, preis));		
	}

	@Override
	public Angebot getAngebot(long angebotNr) {
		return this.angebotRepo.findOne(angebotNr);
	}

	@Override
	public void erzeugeKundenAuftrag(Angebot a) {
		this.auftragRepo.save(new KundenAuftrag(a, false, new Date()));		
	}

	@Override
	public void markiereAuftrag(KundenAuftrag ka) {	
		ka.setAbgeschlossen(true);		
	}

	@Override
	public KundenAuftrag getAuftrag(Long id) {
		return this.auftragRepo.findOne(id);
	}

}
