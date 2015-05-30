package com.mondula.training.spring.service.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mondula.training.spring.service.entities.User;
import com.mondula.training.spring.service.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	protected EntityManager entityManager;
	
	@Autowired
	protected UserRepository userRepository;
	 
    @PersistenceContext 
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
	
	@Override
	public void saveUser(User u) {
		entityManager.persist(u);
	}

	@Override
	public List<User> getUsers(String search) {
    	Query query;
    	if(search!=null) {
    		query = entityManager.createQuery("SELECT u FROM User AS u WHERE u.username LIKE ?1");
    		query.setParameter(1, "%"+search+"%");
    	} else {
    		query = entityManager.createQuery("SELECT u FROM User AS u"); 
    	}
    	@SuppressWarnings("unchecked")
		List<User> result = (List<User>) query.getResultList();
		return result;
	}

	@Override
	public User getUserById(long id) {
		return entityManager.find(User.class, id);
	}

}
