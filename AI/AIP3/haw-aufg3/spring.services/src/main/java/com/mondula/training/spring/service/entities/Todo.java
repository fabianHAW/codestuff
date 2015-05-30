package com.mondula.training.spring.service.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * A Todo
 */
@Entity
public class Todo {
	public Todo(){}
	
	public Todo(Topic topic, String title, String description, int time) {
		this.topic = topic;
		this.title = title;
		this.description = description;
		this.timeEstimate = time;
	}
	
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(fetch=FetchType.EAGER)
	private Topic topic;

	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false)
	private String description;
	
	@Column
	private int timeEstimate = 0;
	
	@Column
	private int timeActual = 0;
	

	public Long getId() {
		return id;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getTimeEstimate() {
		return timeEstimate;
	}

	public void setTimeEstimate(int timeEstimate) {
		this.timeEstimate = timeEstimate;
	}

	public int getTimeActual() {
		return timeActual;
	}

	public void setTimeActual(int timeActual) {
		this.timeActual = timeActual;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Todo)) return false;
		if(id==null) throw new RuntimeException("Entity has not been saved.");
		return id.equals(((Todo)other).getId());
	}
	
	@Override
	public int hashCode() {
		if(id==null) throw new RuntimeException("Entity has not been saved.");
		return id.hashCode();
	};

	@Override
	public String toString() {
		return "Todo [id=" + id + ", topic=" + topic + ", title=" + title
				+ ", description=" + description + ", timeEstimate="
				+ timeEstimate + ", timeActual=" + timeActual + "]";
	}
}
