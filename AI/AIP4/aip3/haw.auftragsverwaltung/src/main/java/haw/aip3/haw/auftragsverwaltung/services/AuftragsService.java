package haw.aip3.haw.auftragsverwaltung.services;

import haw.aip3.haw.auftragsverwaltung.entities.Angebot;
import haw.aip3.haw.auftragsverwaltung.entities.KundenAuftrag;
import haw.aip3.haw.base.entities.IBauteil;

import java.util.Date;

public interface AuftragsService {
	public Angebot erstelleAngebot(IBauteil bauteil, Date gueltigBis, double preis);
	
	public Angebot getAngebot(long angebotNr);
	
	public KundenAuftrag erzeugeKundenAuftrag(Angebot a);
	
	public void markiereAuftrag(KundenAuftrag ka);
	
	public KundenAuftrag getAuftrag(Long id);
}
