package mps.rating.services;

import mps.rating.dto.SalesData;
import mps.rating.graph.nodes.AuftragsPositionRelation;
import mps.rating.graph.nodes.KundeNode;
import mps.rating.graph.nodes.ProduktNode;
import mps.rating.graph.repository.AuftragsPositionGraphRepository;
import mps.rating.graph.repository.KundeGraphRepository;
import mps.rating.graph.repository.ProduktGraphRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RatingServiceImpl implements RatingService {

    @Autowired 
    private KundeGraphRepository kundeGraphRepository;
    
    @Autowired
	private ProduktGraphRepository produktGraphRepository;
    
    @Autowired
	private AuftragsPositionGraphRepository auftragsPositionGraphRepository;
	
    @Autowired
    private Neo4jTemplate template;

	@Override
	public Iterable<? extends SalesData> showProductSalesByCity(String stadt) {
		Iterable<? extends SalesData> salesData = kundeGraphRepository.showProductSalesByCity(stadt);
		return salesData;
	}

	@Override
	public KundeNode getOrCreateKunde(Long id, String name, String stadt) {
		KundeNode k = kundeGraphRepository.findByDbid(id);
		if(k==null) {
			k = new KundeNode(id, name, stadt);
			kundeGraphRepository.save(k);
			
//			k = kundeGraphRepository.findOne(k.getId()); // check if it was saved
//			if(k.getDbid()==null) throw new NullPointerException();
//			if(k.getStadt()==null) throw new NullPointerException();
		}
		return k;
	}

	@Override
	public ProduktNode getOrCreateProdukt(Long id, String name) {
		ProduktNode p = produktGraphRepository.findByDbid(id);
		if(p==null) {
			p = new ProduktNode(id, name);
	    	produktGraphRepository.save(p);
		}
		return p;
	}

	@Override
	public void addBestellung(KundeNode k, ProduktNode produkt, int anzahl) {
		AuftragsPositionRelation r = k.addBestellung(produkt, anzahl);
		auftragsPositionGraphRepository.save(r);
	}
}
