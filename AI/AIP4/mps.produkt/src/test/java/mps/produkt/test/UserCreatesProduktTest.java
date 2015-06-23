package mps.produkt.test;

import mps.produkt.entities.Produkt;
import mps.produkt.services.ProduktService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfig.class)
public class UserCreatesProduktTest {

	@Autowired
	private ProduktService produktService;

	
    @Test
    public void createAuftrag() {
    	Produkt produkt = produktService.createProdukt();
    	Assert.notNull(produkt.getId());
    }
}