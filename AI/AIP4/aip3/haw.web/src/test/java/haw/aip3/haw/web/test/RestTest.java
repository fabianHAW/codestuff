package haw.aip3.haw.web.test;

import haw.aip3.haw.services.auftragsverwaltung.AuftragsService;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;


public class RestTest {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RestTest.class);

	private static final String URL1 = "http://localhost:8080";
	private static final String URL2 = "http://localhost:8081";

	@Test
    public void findUserById() {
    	LOGGER.debug("start test");
    	RestTemplate restTemplate = new RestTemplate();
    	
    	try{
    		AuftragsService as = restTemplate.getForObject(URL1, AuftragsService.class, 1L);
    		//User u = restTemplate.getForObject(URL1+"/users/{id}", User.class, 1L);
    		System.out.println(as);
    	} catch(ResourceAccessException e){
    		LOGGER.debug("server at address " + URL1 + " is not available");
    	}
    	try{
    		AuftragsService as = restTemplate.getForObject(URL2, AuftragsService.class, 1L);
    		//User u = restTemplate.getForObject(URL2+"/users/{id}", User.class, 1L);
    		System.out.println(as);
    	}catch(ResourceAccessException e){
    		LOGGER.debug("server at address " + URL2 + " is not available");
    	}
    	
    }
}
