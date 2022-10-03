package com.luxoft.learntoday.entity;

public class StudentDTO {

	public Integer courseId;
	public Integer enrollmentId;
	public Integer studentId;
	public String studentName;
	
	public StudentDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public StudentDTO(Integer courseId, Integer enrollmentId, Integer studentId, String studentName) {
		super();
		this.courseId = courseId;
		this.enrollmentId = enrollmentId;
		this.studentId = studentId;
		this.studentName = studentName;
	}
	
	
	
	
}
