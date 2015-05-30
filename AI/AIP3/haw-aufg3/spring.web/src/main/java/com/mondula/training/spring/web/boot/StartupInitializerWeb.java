package com.mondula.training.spring.web.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mondula.training.spring.service.entities.Todo;
import com.mondula.training.spring.service.entities.Topic;
import com.mondula.training.spring.service.entities.User;
import com.mondula.training.spring.service.repositories.TodoRepository;
import com.mondula.training.spring.service.repositories.TopicRepository;
import com.mondula.training.spring.service.repositories.UserRepository;

@Component
@Transactional
public class StartupInitializerWeb implements ApplicationListener<ContextRefreshedEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(StartupInitializerWeb.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TodoRepository todoRepository;
	
	@Autowired
	private TopicRepository topicRepository;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// startup of root context, refresh will trigger for initialization and refresh of context
		if (event.getApplicationContext().getParent() == null) {
			configure();
		}
	}

	private void configure() {
		LOGGER.info("setup db.");
		User adminUser = new User();
		adminUser.setUsername("admin");
		adminUser.setPassword("test");
		adminUser.setEnabled(true);

		User managerUser = new User();
		managerUser.setUsername("manager");
		managerUser.setPassword("test");
		managerUser.setEnabled(true);
		
		User conventionalUser = new User();
		conventionalUser.setUsername("user");
		conventionalUser.setPassword("test");
		conventionalUser.setEnabled(true);
		
		User[] users = new User[]{adminUser, managerUser, conventionalUser};
		
		Topic topicDevelopment = new Topic();
		topicDevelopment.setTitle("Development");
		topicDevelopment.setDescription("Developing something important");
		topicRepository.save(topicDevelopment);
		
		Topic topicTest = new Topic();
		topicTest.setTitle("Testing");
		topicTest.setDescription("Testing something important");
		topicRepository.save(topicTest);

		for(int i=0; i<10; i++) {
			Todo todo = new Todo();
			todo.setTitle("Do something "+i);
			todo.setDescription("Do the "+i+" Task in a set of tasks.");
			todo.setTimeEstimate(i);
			todo.setTopic(i%2==0?topicTest:topicDevelopment);
			users[i%3].getTodos().add(todo);
			todoRepository.save(todo);
		}
		
		for(User user: users)
			userRepository.save(user);
	}
}
