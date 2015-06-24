package mps.kunde.test;

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
public class UserCreatesKundeTest {

	@Autowired
	private KundeService kundeService;
	
    @Test
    public void createKunde() {
    	Kunde kunde = kundeService.createKunde("Herbert Hamburg", "Hamburg");
    	Assert.notNull(kunde.getId());
    }
}


