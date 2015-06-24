package mps.kunde.services;

import mps.kunde.entities.Kunde;
import mps.kunde.repositories.KundeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KundeServiceImpl implements KundeService {
	
	@Autowired
	private KundeRepository kundeRepository;

	@Override
	public Kunde createKunde(String name, String stadt) {
		Kunde k = new Kunde(name);
		k.setStadt(stadt);
		kundeRepository.save(k);
		return k;
	}
}
