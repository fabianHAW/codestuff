package haw.aip3.haw.services.produkt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import haw.aip3.haw.entities.produkt.Bauteil;
import haw.aip3.haw.entities.produkt.EinfachesBauteil;
import haw.aip3.haw.entities.produkt.KomplexesBauteil;
import haw.aip3.haw.entities.produkt.Stueckliste;
import haw.aip3.haw.repositories.produkt.BauteilRepository;

@Service
public class ProduktServiceImpl implements ProduktService {

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
