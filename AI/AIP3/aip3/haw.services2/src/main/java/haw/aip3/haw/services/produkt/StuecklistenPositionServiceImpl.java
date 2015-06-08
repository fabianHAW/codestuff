package haw.aip3.haw.services.produkt;

import haw.aip3.haw.entities.produkt.Bauteil;
import haw.aip3.haw.entities.produkt.StuecklistenPosition;
import haw.aip3.haw.repositories.produkt.StuecklistenPostionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StuecklistenPositionServiceImpl implements StuecklistenPositionService {

	@Autowired
	private StuecklistenPostionRepository stuecklistenPositionRepo;
	
	@Override
	public void erstelleStuecklistenPosition(String name, int menge, Bauteil b) {
		this.stuecklistenPositionRepo.save(new StuecklistenPosition(name, menge, b));
	}

	@Override
	public StuecklistenPosition getStuecklistenPosition(String name) {
		return this.stuecklistenPositionRepo.findByName(name).get(0);		
	}

}
