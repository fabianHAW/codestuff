package haw.aip3.haw.services;

import haw.aip3.haw.entities.Stueckliste;

import java.util.Date;

public interface StuecklisteService {

	public void erstelleStueckliste(String name, Date gueltigAb,
			Date gueltigBis, int menge);
	
	public Stueckliste getStueckliste(String name);

}
