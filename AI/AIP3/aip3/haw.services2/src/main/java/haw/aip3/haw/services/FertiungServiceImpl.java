package haw.aip3.haw.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import haw.aip3.haw.entities.Fertigungsauftrag;
import haw.aip3.haw.entities.KundenAuftrag;
import haw.aip3.haw.repositories.FertigungsauftragRepository;

@Service
public class FertiungServiceImpl implements FertigungService {

	@Autowired
	private FertigungsauftragRepository repo;

	@Override
	public Fertigungsauftrag createFertigungsAuftrag(KundenAuftrag a) {
		// TODO Auto-generated method stub
		return new Fertigungsauftrag(a);
	}

	@Override
	public boolean saveFertigungsAuftrag(Fertigungsauftrag f) {
		// TODO Auto-generated method stub
		repo.save(f);
		if(repo.exists(f.getNr()))
			return true;
	
		return false;
	}

	@Override
	public Fertigungsauftrag findFertigungsauftrag(long faNr) {
		// TODO Auto-generated method stub
		return repo.findOne(faNr);
	}

	@Override
	public boolean deleteFertigungsauftrag(long faNr) {
		// TODO Auto-generated method stub
		repo.delete(faNr);
		if(repo.exists(faNr))
			return false;
		
		return true;
	}

}
