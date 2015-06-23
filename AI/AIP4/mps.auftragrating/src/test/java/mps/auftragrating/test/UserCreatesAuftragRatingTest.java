package mps.auftragrating.test;

import java.math.BigDecimal;
import java.util.List;

import mps.auftrag.entities.Auftrag;
import mps.auftrag.entities.Auftragsposition;
import mps.auftrag.services.AuftragService;
import mps.kunde.entities.Kunde;
import mps.kunde.services.KundeService;
import mps.produkt.entities.Produkt;
import mps.produkt.repositories.ProduktRepository;
import mps.rating.dto.SalesData;
import mps.rating.services.RatingService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RatingTestConfig.class)
public class UserCreatesAuftragRatingTest {

	@Autowired
	private AuftragService auftragService;
	
	@Autowired
	private ProduktRepository produktRepository;

	@Autowired
	private KundeService kundeService;

	@Autowired
	private RatingService ratingService;
	
    @Test
    public void createAuftrag() {
    	String city = "Berlin";
    	
    	Auftrag auftrag = auftragService.createAutrag();
    	Assert.notNull(auftrag.getId()); // if this fails, something is wrong with the TransactionManager
    	
    	Kunde kunde = kundeService.createKunde("Testkunde", city);
    	Assert.notNull(kunde.getId());
    	auftrag.setKunde(kunde);
    	
    	List<Auftragsposition> ps = auftrag.getPosten();
    	Iterable<Produkt> produkte = produktRepository.findAll();
    	Produkt produkt = null;
		for(Produkt pp: produkte) {
    		produkt = pp;
    		break;
    	}
    	Assert.notNull(produkt);
    	
    	SalesData oldData = null, newData = null;
    	Iterable<? extends SalesData> salesData = ratingService.showProductSalesByCity(city);
		for(SalesData data: salesData) {
    		if(produkt.getId().equals(data.getProdukt().getDbid())) {
    			oldData = data;
    		}
		}
    	Assert.notNull(oldData);
    	// add items
		for(int j=0; j<4; j++) {
			Auftragsposition p = new Auftragsposition(auftrag, produkt);
			p.setAnzahl(j+1);
			p.setPreis(new BigDecimal(j+".99"));
			ps.add(p);
		}
    	auftragService.save(auftrag);
    	
    	salesData = ratingService.showProductSalesByCity(city);
		for(SalesData data: salesData) {
    		if(produkt.getId().equals(data.getProdukt().getDbid())) {
    			newData = data;
    		}
		}
    	Assert.notNull(newData);
    	System.out.println("xxx");
    	System.out.println(oldData);
    	System.out.println(newData);
    	Assert.isTrue(oldData.getCount()==newData.getCount()-10);
    }
}


