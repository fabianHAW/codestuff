package com.mondula.training.spring.service.services;

import com.mondula.training.spring.service.entities.Todo;
import com.mondula.training.spring.service.entities.Topic;
import com.mondula.training.spring.service.entities.User;

public interface TodoService {
	public Todo getTodo(long id);

	public void assign(User u, Todo todo);

	public Todo createTodo(Topic topic, String title, String description, int time);
	
	public void save(Todo t);
}
