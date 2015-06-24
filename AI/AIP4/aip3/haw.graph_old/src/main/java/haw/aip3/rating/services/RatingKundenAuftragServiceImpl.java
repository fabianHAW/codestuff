package haw.aip3.rating.services;

import haw.aip3.haw.entities.auftragsverwaltung.Angebot;
import haw.aip3.haw.entities.auftragsverwaltung.KundenAuftrag;
import haw.aip3.haw.entities.geschaeftspartner.Geschaeftspartner;
import haw.aip3.haw.entities.produkt.Bauteil;
import haw.aip3.haw.services.auftragsverwaltung.AuftragsServiceImpl;
import haw.aip3.haw.services.geschaeftspartner.GeschaeftspartnerService;
import haw.aip3.rating.graph.nodes.BauteilNode;
import haw.aip3.rating.graph.nodes.GeschaeftspartnerNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingKundenAuftragServiceImpl extends AuftragsServiceImpl {
	
	@Autowired
	private RatingService ratingService;
	
	@Autowired 
	GeschaeftspartnerService gpService;

	@Override
	public KundenAuftrag erzeugeKundenAuftrag(Angebot angebot) {
		KundenAuftrag kundenAuftrag = super.erzeugeKundenAuftrag(angebot);
		Geschaeftspartner gP = gpService.findGeschaeftspartner(angebot.getKundenID());
		
		GeschaeftspartnerNode k = ratingService.getOrCreateGeschaeftspartner(gP.getId(), gP.getName(), gP.getStadt());
	
		
		Bauteil b = angebot.getBauteil();
		BauteilNode bN = ratingService.getOrCreateBauteil(b.getBauteilNr(), b.getName());
		ratingService.addBestellung(k, bN, angebot.getPreis());
		
		return kundenAuftrag;
	}

}
