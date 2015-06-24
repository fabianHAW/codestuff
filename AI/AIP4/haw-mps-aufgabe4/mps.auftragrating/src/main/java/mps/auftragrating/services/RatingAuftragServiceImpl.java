package mps.auftragrating.services;

import mps.auftrag.entities.Auftrag;
import mps.auftrag.entities.Auftragsposition;
import mps.auftrag.services.AuftragServiceImpl;
import mps.kunde.entities.Kunde;
import mps.produkt.entities.Produkt;
import mps.rating.graph.nodes.KundeNode;
import mps.rating.graph.nodes.ProduktNode;
import mps.rating.services.RatingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingAuftragServiceImpl extends AuftragServiceImpl {
	
	@Autowired
	private RatingService ratingService;
	

	@Override
	public void save(Auftrag auftrag) {
		super.save(auftrag);
		Kunde kk = auftrag.getKunde();
		KundeNode k = ratingService.getOrCreateKunde(kk.getId(), kk.getName(), kk.getStadt());
		for(Auftragsposition ap: auftrag.getPosten()) {
			int anzahl = ap.getAnzahl();
			Produkt pp = ap.getProdukt();
			ProduktNode p = ratingService.getOrCreateProdukt(pp.getId(), pp.getName());
			ratingService.addBestellung(k,p,anzahl);
		}
	}

}
