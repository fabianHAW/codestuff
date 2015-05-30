package com.mondula.training.spring.web.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import com.mondula.training.spring.service.entities.User;

public class RestTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(RestTest.class);

	private static final String URL1 		= "http://localhost:8080";
	private static final String URL2 		= "http://localhost:8081";

    
    @Test
    public void findUserById() {
    	LOGGER.debug("start test");
    	RestTemplate restTemplate = new RestTemplate();
    	User u = restTemplate.getForObject(URL1+"/users/{id}", User.class, 1L);
    	System.out.println(u);
    	u = restTemplate.getForObject(URL2+"/users/{id}", User.class, 1L);
    	System.out.println(u);
    }
}
