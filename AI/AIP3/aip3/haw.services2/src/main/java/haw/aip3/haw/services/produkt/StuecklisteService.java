package haw.aip3.haw.services.produkt;

import haw.aip3.haw.entities.produkt.Stueckliste;
import haw.aip3.haw.entities.produkt.StuecklistenPosition;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface StuecklisteService {

	public void erstelleStueckliste(String name, Date gueltigAb,
			Date gueltigBis, Set<StuecklistenPosition> position);
	
	public Stueckliste getStueckliste(String name);

}
