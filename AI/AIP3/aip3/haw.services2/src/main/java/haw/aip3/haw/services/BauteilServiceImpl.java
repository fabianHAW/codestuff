package haw.aip3.haw.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import haw.aip3.haw.entities.Bauteil;
import haw.aip3.haw.entities.EinfachesBauteil;
import haw.aip3.haw.entities.KomplexesBauteil;
import haw.aip3.haw.entities.Stueckliste;
import haw.aip3.haw.repositories.BauteilRepository;

@Service
public class BauteilServiceImpl implements BauteilService {

	@Autowired
	private BauteilRepository bauteilRepo;

	@Override
	public void erstelleKomplexesBauteil(String name, Stueckliste stueckliste) {
		bauteilRepo.save(new KomplexesBauteil(name, stueckliste));
	}

	@Override
	public void erstelleEinfachesBauteil(String name) {
		this.bauteilRepo.save(new EinfachesBauteil(name));
	}

	@Override
	public Bauteil findeBauteil(String name) {
		return this.bauteilRepo.findByName(name);
	}

}
