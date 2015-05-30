package com.mondula.training.spring.web.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mondula.training.spring.service.entities.User;
import com.mondula.training.spring.service.repositories.UserRepository;
import com.mondula.training.spring.service.services.UserService;
import com.mondula.training.spring.web.util.Mapper;

@RestController
public class MainController {

	@Autowired
	private UserRepository userRepository;
	
	// Java 8 convenience in case we need a different format or different object keys in our json
	private static final Mapper<User> mapper = new Mapper<User>()  
			.map("name", User::getUsername)
			.map("id", User::getId)
			.map("label", u -> "Label: "+u.getUsername());

	@Autowired
	private UserService userService;
		
    @RequestMapping(value="/users", 
            method=RequestMethod.GET, 
            produces=MediaType.APPLICATION_JSON_VALUE)
    public List<Map<String,? extends Object>> getUsers(@RequestParam(required = false, value = "search") String needle) {
        return mapper.apply(userService.getUsers(needle)); // our object have name, id, label
    }
    

    @RequestMapping(value="/users/{id}", 
            method=RequestMethod.GET, 
            produces=MediaType.APPLICATION_JSON_VALUE)
    public User getUser(@PathVariable("id") Long id) {
        return userService.getUserById(id); // our object have name, id, label
    }
    

}