package com.mondula.training.spring.service.services;

import java.util.List;

import com.mondula.training.spring.service.entities.User;

public interface UserService {
	public List<User> getUsers(String needle);

	void saveUser(User u);

	public User getUserById(long id);
}
