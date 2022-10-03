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
public class Student {

	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer enrollmentId;
	private Integer studentId;
	private String studentName;
//	private Integer courseId;
	
	@ManyToOne()
	@JsonIgnore
	private Course course;

	public Student(Integer enrollmentId, Integer studentId, String studentName) {
		super();
		this.enrollmentId = enrollmentId;
		this.studentId = studentId;
		this.studentName = studentName;
	}
	
	
	
	
}
