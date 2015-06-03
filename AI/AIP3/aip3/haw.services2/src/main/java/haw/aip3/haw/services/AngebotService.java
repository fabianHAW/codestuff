package haw.aip3.haw.services;

import haw.aip3.haw.entities.Angebot;
import haw.aip3.haw.entities.Bauteil;

public interface AngebotService {

	public void erstelleAngebot();
	
	public void erstelleAngebot(Bauteil bauteil);
	
	public Angebot getAngebot(long angebotNr);
}
