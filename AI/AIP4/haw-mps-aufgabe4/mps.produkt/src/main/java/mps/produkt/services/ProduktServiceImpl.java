package mps.produkt.services;

import mps.produkt.entities.Produkt;
import mps.produkt.repositories.ProduktRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProduktServiceImpl implements ProduktService {
	
	@Autowired
	private ProduktRepository auftragRepository;

	@Override
	public Produkt createProdukt() {
		Produkt a = new Produkt();
		auftragRepository.save(a);
		return a;
	}

	@Override
	public void save(Produkt auftrag) {
		auftragRepository.save(auftrag);
	}
}
