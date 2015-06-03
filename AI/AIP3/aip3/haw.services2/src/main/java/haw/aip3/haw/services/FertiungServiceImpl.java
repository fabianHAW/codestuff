package haw.aip3.haw.services;

import haw.aip3.haw.entities.Fertigungsauftrag;
import haw.aip3.haw.entities.KundenAuftrag;
import haw.aip3.haw.repositories.FertigungsauftragRepository;

public class FertiungServiceImpl implements FertigungService {

	private FertigungsauftragRepository repo;
	public FertiungServiceImpl(FertigungsauftragRepository faRepo) {
		// TODO Auto-generated constructor stub
		repo = faRepo;
	}

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
