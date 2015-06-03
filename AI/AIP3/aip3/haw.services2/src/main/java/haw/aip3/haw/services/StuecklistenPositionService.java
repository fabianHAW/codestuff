package haw.aip3.haw.services;

import haw.aip3.haw.entities.StuecklistenPosition;

public interface StuecklistenPositionService {

	public void erstelleStuecklistenPosition(String name, int menge);
	
	public StuecklistenPosition getStuecklistenPosition(String name);
}
