package com.mondula.training.spring.service.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.mondula.training.spring.service.entities.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

	User findByUsername(String username);

}
