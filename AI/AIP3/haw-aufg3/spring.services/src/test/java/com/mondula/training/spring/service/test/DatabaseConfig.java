package com.mondula.training.spring.service.test;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties") // this allows us to configure our database via the application.properties file
public class DatabaseConfig {}
