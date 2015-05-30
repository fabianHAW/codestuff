package com.mondula.training.spring.service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.mondula.training.spring.service.entities.Todo;

/**
 * DAO for the Todo Entity.
 * Spring data implements all method of the interfaces automatically through proxies.
 */
public interface TodoRepository extends 
	PagingAndSortingRepository<Todo, Long> { 
	@Query(
            "Select t FROM Todo t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(t.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))"
    )
    public List<Todo> searchWithoutPaging(@Param("searchTerm") String searchTerm);
}
