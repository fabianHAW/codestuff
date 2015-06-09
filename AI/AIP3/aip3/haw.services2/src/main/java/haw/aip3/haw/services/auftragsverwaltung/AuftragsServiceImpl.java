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
	public Angebot erstelleAngebot(Bauteil bauteil, Date gueltigBis, double preis) {
		return this.angebotRepo.save(new Angebot(bauteil, new Date(), gueltigBis, preis));		
	}

	@Override
	public Angebot getAngebot(long angebotNr) {
		return this.angebotRepo.findOne(angebotNr);
	}

	@Override
	public KundenAuftrag erzeugeKundenAuftrag(Angebot a) {
		return this.auftragRepo.save(new KundenAuftrag(a, false, new Date()));		
	}

	@Override
	public void markiereAuftrag(KundenAuftrag ka) {	
		ka.setAbgeschlossen(true);		
	}

	@Override
	public KundenAuftrag getAuftrag(Long id) {
		return this.auftragRepo.findOne(id);
	}

	@Override
	public void erstelleAngebot(int kundenID, String bauteil) {
		// TODO Auto-generated method stub
		this.angebotRepo.save(new Angebot(kundenID, bauteil));
	}

	@Override
	public void updateAuftrag(KundenAuftrag ka) {
		// TODO Auto-generated method stub
		
	}

}
