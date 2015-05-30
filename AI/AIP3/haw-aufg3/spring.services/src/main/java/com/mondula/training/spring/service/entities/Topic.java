package com.mondula.training.spring.service.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class Topic {
	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false)
	private String description;

	public Long getId() {
		return id;
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

	@Override
	public String toString() {
		return "Topic [id=" + id + ", title=" + title + ", description="
				+ description + "]";
	}

}
