package com.luxoft.learntoday.service;

import java.util.List;

import com.luxoft.learntoday.entity.Course;

public interface AdminService {
	
	public Course saveCourseDetails(Course course);
	
	public List<Course> getAllCourses();
	
	public Course getCourseById(Integer courseId);

}
