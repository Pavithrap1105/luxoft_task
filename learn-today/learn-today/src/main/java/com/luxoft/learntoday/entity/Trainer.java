package com.luxoft.learntoday.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Trainer {

	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer trainerId;
	private String trainerName;
	private String password;
	
	@ManyToOne
	@JsonIgnore
	private Course course;

	public Trainer(Integer trainerId, String trainerName, String password) {
		super();
		this.trainerId = trainerId;
		this.trainerName = trainerName;
		this.password = password;
	}
	
	
}
