package haw.aip3.haw.services.auftragsverwaltung;

import haw.aip3.haw.entities.auftragsverwaltung.Angebot;
import haw.aip3.haw.entities.auftragsverwaltung.KundenAuftrag;
import haw.aip3.haw.entities.produkt.Bauteil;

import java.util.Date;

public interface AuftragsService {
	public Angebot erstelleAngebot(Bauteil bauteil, Date gueltigBis, double preis);
	
	public void erstelleAngebot(int kundenID, String bauteil);
	
	public Angebot getAngebot(long angebotNr);
	
	public KundenAuftrag erzeugeKundenAuftrag(Angebot a);
	
	public void markiereAuftrag(KundenAuftrag ka);
	
	public KundenAuftrag getAuftrag(Long id);
	
	public void updateAuftrag(KundenAuftrag ka);
}
