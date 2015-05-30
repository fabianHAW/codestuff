package com.mondula.training.spring.service.services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mondula.training.spring.service.entities.Todo;
import com.mondula.training.spring.service.entities.Topic;
import com.mondula.training.spring.service.entities.User;
import com.mondula.training.spring.service.repositories.TodoRepository;
import com.mondula.training.spring.service.repositories.UserRepository;

@Service
public class TodoServiceImpl implements TodoService {

	@Autowired
	private TodoRepository todoRepository;
	
	@Autowired
	private UserRepository userService;
	
	@Override
	public Todo getTodo(long id) {
		return todoRepository.findOne(id);
	}

	@Override
	public void assign(User u, Todo todo) {
		Collection<Todo> todos = u.getTodos();
		todos.add(todo);
		userService.save(u);
	}

	@Override
	public Todo createTodo(Topic topic, String title, String description, int time) {
		Todo t = new Todo(topic, title, description, time);
		todoRepository.save(t);
		return t;
	}

	@Override
	public void save(Todo t) {
		todoRepository.save(t);
	}
}
