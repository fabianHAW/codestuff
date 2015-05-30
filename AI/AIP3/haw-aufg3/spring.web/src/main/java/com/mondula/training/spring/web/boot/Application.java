package com.mondula.training.spring.web.boot;

import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.mondula.training.spring.service.config.AppConfiguration;
import com.mondula.training.spring.service.services.UserService;
import com.mondula.training.spring.web.controller.MainController;


@Configuration
@EnableAutoConfiguration
@PropertySource("classpath:application-dump.properties") // this allows us to configure our database via the application.properties file
public class Application {

    public static void main(String[] args) throws SQLException {

    	ConfigurableApplicationContext ctx = SpringApplication.run(
    			new Object[]{
    					Application.class, // the application
    					AppConfiguration.class, // the configuration of this application services and entities (see spring.services)
    					StartupInitializerWeb.class, // the data population
    					MainController.class // the main controller to supply the rest interface to the outside world
    			}, args); 
  	        
    	// Through this you can test if beans are available and 
    	// what result they return.
    	UserService us = ctx.getBean(UserService.class);
  		System.out.println("users with 'm': "+us.getUsers("m"));
    }
}
