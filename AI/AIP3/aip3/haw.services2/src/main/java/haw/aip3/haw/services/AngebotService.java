package haw.aip3.haw.services;

import haw.aip3.haw.entities.Angebot;

public interface AngebotService {

	public Angebot erstelleAngebot();
	
	public Angebot getAngebot(long angebotNr);
}
