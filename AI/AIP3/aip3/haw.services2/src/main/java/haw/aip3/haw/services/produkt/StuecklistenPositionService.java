package haw.aip3.haw.services.produkt;

import haw.aip3.haw.entities.produkt.StuecklistenPosition;

public interface StuecklistenPositionService {

	public void erstelleStuecklistenPosition(String name, int menge);
	
	public StuecklistenPosition getStuecklistenPosition(String name);
}
