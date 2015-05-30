package haw.aip3.haw.services;

import haw.aip3.haw.entities.Angebot;
import haw.aip3.haw.entities.KundenAuftrag;
import haw.aip3.haw.repositories.AngebotRepository;
import haw.aip3.haw.repositories.KundenAuftragRepository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KundenAuftragServiceImpl implements KundenAuftragService{

	protected EntityManager entityManager;

	@Autowired
	private KundenAuftragRepository auftragRepo; 
	
	
    @PersistenceContext 
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
	@Override
	public void erzeugeKundenAuftrag(Angebot a) {
		KundenAuftrag ka = new KundenAuftrag(a, false, new Date());
		//auftragRepo.save(ka);
		//this.entityManager.persist(ka);
		this.auftragRepo.save(ka);
	}

	@Override
	public KundenAuftrag getAuftrag(Long id) {	
		return this.entityManager.find(KundenAuftrag.class, id);
	}

	@Override
	public void markiereAuftrag(KundenAuftrag a) {		
		a.setAbgeschlossen(true);
	}

}
