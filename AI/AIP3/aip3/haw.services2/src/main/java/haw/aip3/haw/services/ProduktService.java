package haw.aip3.haw.services;

import haw.aip3.haw.entities.Bauteil;
import haw.aip3.haw.entities.Stueckliste;

public interface ProduktService {
	
	public void erstelleKomplexesBauteil(String name, Stueckliste stueckliste);
	public void erstelleEinfachesBauteil(String name);
	public Bauteil findeBauteil(String name);

}
