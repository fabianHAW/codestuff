package haw.aip3.haw.services;

import haw.aip3.haw.entities.Angebot;
import haw.aip3.haw.entities.Bauteil;
import haw.aip3.haw.entities.KundenAuftrag;

import java.util.Date;

public interface AuftragsService {
	public void erstelleAngebot(Bauteil bauteil, Date gueltigBis, double preis);
	
	public Angebot getAngebot(long angebotNr);
	
	public void erzeugeKundenAuftrag(Angebot a);
	
	public void markiereAuftrag(KundenAuftrag ka);
	
	public KundenAuftrag getAuftrag(Long id);
}
