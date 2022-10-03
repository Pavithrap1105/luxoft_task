package com.luxoft.learntoday.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer courseId;
	
	@NotNull(message = "Title should not be null")
	@NotEmpty(message = "Title should not be empty")
	private String title;
	private Float fees;
	private String description;
	private String startDate;
	
	@OneToMany(mappedBy = "course")
	private List<Student> student;
	
	@OneToMany(mappedBy = "course")
	private List<Trainer> trainer;

	public Course(Integer courseId,
			@NotNull(message = "Title should not be null") @NotEmpty(message = "Title should not be empty") String title,
			Float fees, String description, String startDate) {
		super();
		this.courseId = courseId;
		this.title = title;
		this.fees = fees;
		this.description = description;
		this.startDate = startDate;
	}
	
	

}
