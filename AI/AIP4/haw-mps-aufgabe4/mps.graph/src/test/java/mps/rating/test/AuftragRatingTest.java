package mps.rating.test;

import java.util.Collection;

import mps.rating.dto.SalesData;
import mps.rating.graph.nodes.AuftragsPositionRelation;
import mps.rating.graph.nodes.KundeNode;
import mps.rating.graph.repository.AuftragsPositionGraphRepository;
import mps.rating.graph.repository.KundeGraphRepository;
import mps.rating.services.RatingService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RatingTestConfig.class)
public class AuftragRatingTest {

	@Autowired
	private RatingService ratingService;
	
	@Autowired
	private KundeGraphRepository kundeGraphRepository;
	
	@Autowired 
    private Neo4jTemplate neo4jTemplate;

	@Autowired
	private AuftragsPositionGraphRepository auftragGraphRepository;
	
    @Test
    public void testRating() {
    	String city = "Hamburg";
    	String product = "Produkt 3";
    	SalesData oldData = null;
    	SalesData newData = null;
    	Iterable<? extends SalesData> salesData = ratingService.showProductSalesByCity(city);
    	for(SalesData data: salesData) {
    		if(product.equals(data.getProdukt().getProduktName())) {
    			oldData = data;
    		}
		}
    	Assert.notNull(oldData);
    	System.out.println(city+"-->"+oldData.getProdukt().getProduktName()+": "+oldData.getCount());
    	
    	// order new product
    	int count = 0;
    	Collection<KundeNode> kunden = kundeGraphRepository.findByStadt(city);
    	for(KundeNode k: kunden) {
    		AuftragsPositionRelation r = k.addBestellung(oldData.getProdukt(), 2);
    		count++;
    		auftragGraphRepository.save(r);
    	}
    	
    	salesData = ratingService.showProductSalesByCity(city);
    	for(SalesData data: salesData) {
    		if(product.equals(data.getProdukt().getProduktName())) {
    			newData = data;
    		}
		}
    	System.out.println(city+"--"+newData+"--"+newData.getProdukt()+":"+newData.getCount());
    	Assert.notNull(newData);
    	Assert.isTrue(oldData.getCount()+(count*2) == newData.getCount()); // each added two so we should have count*2 more
    }
}


