package com.mondula.training.spring.service.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.mondula.training.spring.service.entities.Todo;
import com.mondula.training.spring.service.entities.User;
import com.mondula.training.spring.service.services.TodoService;
import com.mondula.training.spring.service.services.TopicService;
import com.mondula.training.spring.service.services.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=com.mondula.training.spring.service.test.UserCreatesTodoTest.ContextConfiguration.class)
public class UserCreatesTodoTest {

	@ComponentScan(basePackages = "com.mondula.training.spring.service")
	@Configuration
    static class ContextConfiguration {}

	@Autowired
	private TodoService todoService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private TopicService topicService;
	
    @Test
    public void createTodo() {
    	Todo todo = todoService.createTodo(topicService.getTopic(2L), "test", "testtodo", 2);
    	Assert.notNull(todo.getId());
    	boolean found = false;
    	List<User> users = userService.getUsers("m");
    	Assert.isTrue(users.size()==2); // manager and admin
    	for(User u: users) {
    		if("admin".equals(u.getUsername())) {
    			todoService.assign(u, todo);
    			found = true;
    		}
    	}
    	Assert.isTrue(found);
    	users = userService.getUsers("admin");
    	Assert.isTrue(users.size()==1); // admin
    	for(User u: users) {
    		if("admin".equals(u.getUsername())) {
    			// remember to implement equals and hash code for this!
    			Assert.isTrue(u.getTodos().contains(todo));
    		}
    	}
    	users = userService.getUsers("manager");
    	Assert.isTrue(users.size()==1); // admin
    }
}


