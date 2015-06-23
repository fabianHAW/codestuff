package mps.auftrag.test;

import mps.auftrag.entities.Auftrag;
import mps.auftrag.services.AuftragService;
import mps.auftrag.values.PaymentOption;
import mps.kunde.entities.Kunde;
import mps.kunde.services.KundeService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfig.class)
public class UserCreatesAuftragTest {

	@Autowired
	private AuftragService auftragService;

	@Autowired
	private KundeService kundeService;
	
    @Test
    public void createAuftrag() {
    	Auftrag auftrag = auftragService.createAutrag();
    	Assert.notNull(auftrag.getId());
    	
    	int optionCount = 0;
    	for(PaymentOption p: auftragService.getPaymentOptionsForAuftrag(auftrag)) {
    		optionCount++;
    		System.out.println(p);
    	}
    	Assert.isTrue(optionCount==1);
    	
    	Kunde kunde = kundeService.createKunde("Kunde x", "Berlin");
    	Assert.notNull(kunde.getId());
    	auftrag.setKunde(kunde);
    	auftragService.save(auftrag);
    	
    	Iterable<Auftrag> auftraege = auftragService.getAuftragByKunde(kunde);
    	System.out.println(auftraege);
    	Auftrag result = null;
    	for(Auftrag a: auftraege)
    		result = a;
    	Assert.notNull(result);
    	Assert.isTrue(result.equals(auftrag));
    }
}


