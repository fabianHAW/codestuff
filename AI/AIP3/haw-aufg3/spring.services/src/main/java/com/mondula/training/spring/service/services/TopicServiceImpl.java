package com.mondula.training.spring.service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mondula.training.spring.service.entities.Topic;
import com.mondula.training.spring.service.repositories.TopicRepository;

@Service
public class TopicServiceImpl implements TopicService {

	@Autowired
	private TopicRepository topicRepository;

	@Override
	public Topic getTopic(long id) {
		return topicRepository.findOne(id);
	}

}
