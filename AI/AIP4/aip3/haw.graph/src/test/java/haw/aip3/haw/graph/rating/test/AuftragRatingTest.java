package haw.aip3.haw.graph.rating.test;

import haw.aip3.haw.graph.rating.dto.SalesData;
import haw.aip3.haw.graph.rating.dto.SalesDataForOftenSalesPerRegion;
import haw.aip3.haw.graph.rating.dto.SalesDataForRelatedProducts;
import haw.aip3.haw.graph.rating.nodes.AuftragsRelation;
import haw.aip3.haw.graph.rating.nodes.GeschaeftspartnerNode;
import haw.aip3.haw.graph.rating.repositories.AuftragsRelationGraphRepository;
import haw.aip3.haw.graph.rating.repositories.GeschaeftspartnerGraphRepository;
import haw.aip3.haw.graph.rating.repositories.GeschaeftspartnerGraphRepository.SalesDataForRelatedProductsImpl;
import haw.aip3.haw.graph.rating.services.RatingService;

import java.util.Collection;
import java.util.Iterator;

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
	private GeschaeftspartnerGraphRepository geschaeftspartnerGraphRepository;
	
	@Autowired 
    private Neo4jTemplate neo4jTemplate;

	@Autowired
	private AuftragsRelationGraphRepository auftragGraphRepository;
	
    @Test
    public void testRating() {
    	//System.out.println("start***********");
    	String city = "Hamburg";
    	String product = "Produkt 3";
    	SalesData oldData = null;
    	SalesData newData = null;
    	Iterable<? extends SalesData> salesData = ratingService.showProductSalesByCity(city);
    	//System.out.println("after salesdata");

    	
    	
    	for(SalesData data: salesData) {
    		//System.out.println(data.getBauteil());
    		if(product.equals(data.getBauteil().getProduktName())) {
    			oldData = data;
    		}
		}
    //	System.out.println("after foreach");
    	Assert.notNull(oldData);
    //	System.out.println(city+"-->"+oldData.getBauteil().getProduktName()+": "+oldData.getCount());
    	
    	// order new product
    	int count = 0;
    	Collection<GeschaeftspartnerNode> kunden = geschaeftspartnerGraphRepository.findByStadt(city);
    	for(GeschaeftspartnerNode k: kunden) {
    		AuftragsRelation r = k.addBestellung(oldData.getBauteil(), 2);
    		count++;
    		auftragGraphRepository.save(r);
    	}
    	
    	salesData = ratingService.showProductSalesByCity(city);
    	for(SalesData data: salesData) {
    		if(product.equals(data.getBauteil().getProduktName())) {
    			newData = data;
    		}
		}
    	System.out.println(city+"--"+newData+"--"+newData.getBauteil()+":"+newData.getCount());
    	Assert.notNull(newData);
    	Assert.isTrue(oldData.getCount()+(count*2) == newData.getCount()); // each added two so we should have count*2 more
    
    	Iterable<? extends SalesDataForOftenSalesPerRegion> sales1 = ratingService.showOftenProductsalesByCity();
    	System.out.println("");
    	System.out.println("Produkte, die sich in bestimmten Regionen häufiger verkaufen als in anderen:");
    	for(SalesDataForOftenSalesPerRegion data : sales1){
    		System.out.println(data);
    	}
    	
    	Iterable<? extends SalesDataForRelatedProducts> sales2 = ratingService.showRelatedProducts();
    	System.out.println("");
    	System.out.println("Produkte, die sich häufig mit anderen zusammen verkaufen:");
    	for(SalesDataForRelatedProducts data : sales2){
    		System.out.println(data);
    	}
    }
    
}


