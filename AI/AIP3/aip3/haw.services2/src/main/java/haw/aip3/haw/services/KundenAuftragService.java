package haw.aip3.haw.services;


import haw.aip3.haw.entities.Angebot;
import haw.aip3.haw.entities.KundenAuftrag;

public interface KundenAuftragService {

	public void erzeugeKundenAuftrag(Angebot a);
	
	public void markiereAuftrag(KundenAuftrag ka);
	
	public KundenAuftrag getAuftrag(Long id);

}
