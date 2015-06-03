package haw.aip3.haw.services;

import haw.aip3.haw.entities.Stueckliste;
import haw.aip3.haw.entities.StuecklistenPosition;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface StuecklisteService {

	public void erstelleStueckliste(String name, Date gueltigAb,
			Date gueltigBis, Set<StuecklistenPosition> position);
	
	public Stueckliste getStueckliste(String name);

}
