package haw.aip3.haw.services;

import haw.aip3.haw.entities.StuecklistenPosition;
import haw.aip3.haw.repositories.StuecklistenPostionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StuecklistenPositionServiceImpl implements StuecklistenPositionService {

	@Autowired
	private StuecklistenPostionRepository stuecklistenPositionRepo;
	
	@Override
	public void erstelleStuecklistenPosition(String name, int menge) {
		this.stuecklistenPositionRepo.save(new StuecklistenPosition(name, menge));
	}

	@Override
	public StuecklistenPosition getStuecklistenPosition(String name) {
		return this.stuecklistenPositionRepo.findByName(name);		
	}

}
