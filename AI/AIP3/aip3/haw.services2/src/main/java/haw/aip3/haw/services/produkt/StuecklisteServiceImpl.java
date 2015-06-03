package haw.aip3.haw.services.produkt;

import haw.aip3.haw.entities.produkt.Stueckliste;
import haw.aip3.haw.entities.produkt.StuecklistenPosition;
import haw.aip3.haw.repositories.produkt.StuecklisteRepository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StuecklisteServiceImpl implements StuecklisteService {

	@Autowired
	private StuecklisteRepository stuecklisteRepo;

	@Override
	public void erstelleStueckliste(String name, Date gueltigAb,
			Date gueltigBis, Set<StuecklistenPosition> position) {
		this.stuecklisteRepo.save(new Stueckliste(name, gueltigAb, gueltigBis,
				position));
	}

	@Override
	public Stueckliste getStueckliste(String name) {
		return this.stuecklisteRepo.findByName(name);
	}

}
