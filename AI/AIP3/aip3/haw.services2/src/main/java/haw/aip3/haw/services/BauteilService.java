package haw.aip3.haw.services;

import haw.aip3.haw.entities.Bauteil;

public interface BauteilService {
	
	public void erstelleKomplexesBauteil(String name);
	public void erstelleEinfachesBauteil(String name);
	public Bauteil findeBauteil(String name);

}
