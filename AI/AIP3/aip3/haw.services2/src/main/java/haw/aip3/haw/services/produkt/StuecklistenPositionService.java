package haw.aip3.haw.services.produkt;

import haw.aip3.haw.entities.produkt.Bauteil;
import haw.aip3.haw.entities.produkt.StuecklistenPosition;

public interface StuecklistenPositionService {

	public void erstelleStuecklistenPosition(String name, int menge, Bauteil bauteil);
	
	public StuecklistenPosition getStuecklistenPosition(String name);
}
