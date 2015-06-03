package haw.aip3.haw.services.produkt;

import java.util.List;

import haw.aip3.haw.entities.fertigungsverwaltung.Fertigungsauftrag;
import haw.aip3.haw.entities.produkt.Arbeitsplan;
import haw.aip3.haw.entities.produkt.Bauteil;
import haw.aip3.haw.entities.produkt.Stueckliste;

public interface ProduktService {
	
	public void erstelleKomplexesBauteil(String name, Stueckliste stueckliste);
	
	public void erstelleEinfachesBauteil(String name);
	
	public Bauteil findeBauteil(String name);
	
	public Bauteil getBauteil(Fertigungsauftrag fa);
	
	/*
	 * macht irgendwie nicht viel sinn...
	 */
	public Bauteil getBauteil(Bauteil b);
	
	public void komplexesBauteilBauen(List<Arbeitsplan> lap);

}
