package haw.aip3.haw.services.geschaeftspartner;


import haw.aip3.haw.entities.geschaeftspartner.Geschaeftspartner;

import haw.aip3.haw.repositories.geschaeftspartner.GeschaeftspartnerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeschaeftspartnerServiceImpl implements GeschaeftspartnerService {

	@Autowired
	private GeschaeftspartnerRepository repo;

	@Override
	public Geschaeftspartner createGeschaeftspartner(String name, String stadt) {
		// TODO Auto-generated method stub
		Geschaeftspartner gp = new Geschaeftspartner(name, stadt);
		return repo.save(gp);
	}

	@Override
	public Geschaeftspartner findGeschaeftspartner(long gpNr) {
		// TODO Auto-generated method stub
		return repo.findOne(gpNr);
	}

	@Override
	public boolean deleteGescheaftspartner(long gpNr) {
		// TODO Auto-generated method stub
		repo.delete(gpNr);
		if (repo.exists(gpNr))
			return false;

		return true;
	}

}
