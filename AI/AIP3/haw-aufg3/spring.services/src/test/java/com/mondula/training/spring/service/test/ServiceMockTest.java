package com.mondula.training.spring.service.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.mondula.training.spring.service.entities.Todo;
import com.mondula.training.spring.service.repositories.TopicRepository;
import com.mondula.training.spring.service.services.TodoService;
import com.mondula.training.spring.service.services.TodoServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
// ApplicationContext will be loaded from the static inner ContextConfiguration class
// This way we can override our TodoService Implementation
@ContextConfiguration(classes=com.mondula.training.spring.service.test.ServiceMockTest.ContextConfiguration.class)
public class ServiceMockTest {

	// no @Configuration, so it does only get picked up by this test
	@ComponentScan(basePackages = "com.mondula.training.spring.service")
	// Note: there is no @PropertySource("classpath:application.properties") as this is taken from DatabaseConfig.class
    static class ContextConfiguration {

		@Autowired
		private TopicRepository topicRepository;
		
		@Bean(name="todoService")
		public TodoService todoService() {
			return new TodoServiceImpl(){
				// this method is mocked by our fake implementation
				@Override
				public Todo getTodo(long id) {
					return new Todo(topicRepository.findOne(1L),"Test","Test",0);
				}
			};
		}
	}

	@Autowired
	private TodoService todoService;

    @Test
    public void findTodo() {
    	Todo todo = todoService.getTodo(1L);
    	Assert.notNull(todo);
    	Assert.isNull(todo.getId());
    	Assert.isTrue(todo.getTopic().getId()==1L);
    	System.out.println(todo);
    }
}


