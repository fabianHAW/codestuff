package haw.aip3.haw.services;

import java.util.Date;

import haw.aip3.haw.entities.Angebot;
import haw.aip3.haw.entities.Bauteil;

public interface AngebotService {

	public void erstelleAngebot(Bauteil bauteil, Date gueltigBis, double preis);
	
	public Angebot getAngebot(long angebotNr);
}
