package haw.aip3.haw.services;

import haw.aip3.haw.entities.Stueckliste;
import haw.aip3.haw.repositories.StuecklisteRepository;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StuecklisteServiceImpl implements StuecklisteService {

	@Autowired
	private StuecklisteRepository stuecklisteRepo;

	@Override
	public void erstelleStueckliste(String name, Date gueltigAb,
			Date gueltigBis, int menge) {
		this.stuecklisteRepo.save(new Stueckliste(name, gueltigAb, gueltigBis,
				menge));
	}

	@Override
	public Stueckliste getStueckliste(String name) {
		return this.stuecklisteRepo.findByName(name);
	}

}
