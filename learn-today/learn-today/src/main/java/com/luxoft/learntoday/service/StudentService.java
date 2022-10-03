package com.luxoft.learntoday.service;

import java.util.List;

import com.luxoft.learntoday.entity.Course;
import com.luxoft.learntoday.entity.Student;
import com.luxoft.learntoday.entity.StudentDTO;

public interface StudentService {
	
	public Student enrollStudent(Student student);
	
	public List<Course> getAllCourses();
	
	public boolean deleteStudentEnrollment(Integer enrollmentId);
	
	public Student addStudent(StudentDTO dto);

}
