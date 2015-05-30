package com.mondula.training.spring.service.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.mondula.training.spring.service.entities.Topic;

/**
 * DAO for the Todo Entity.
 * Spring data implements all methods of the interfaces automatically through proxies.
 */
public interface TopicRepository extends PagingAndSortingRepository<Topic, Long> {
}
