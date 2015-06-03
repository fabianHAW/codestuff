package haw.aip3.haw.services.produkt;

import haw.aip3.haw.entities.produkt.Bauteil;
import haw.aip3.haw.entities.produkt.Stueckliste;

public interface ProduktService {
	
	public void erstelleKomplexesBauteil(String name, Stueckliste stueckliste);
	public void erstelleEinfachesBauteil(String name);
	public Bauteil findeBauteil(String name);

}
