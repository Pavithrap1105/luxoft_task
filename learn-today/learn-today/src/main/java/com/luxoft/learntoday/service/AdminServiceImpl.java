package com.luxoft.learntoday.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luxoft.learntoday.entity.Course;
import com.luxoft.learntoday.exception.ResourceAlreadyExistException;
import com.luxoft.learntoday.exception.ResourseNotFoundException;
import com.luxoft.learntoday.repository.AdminRepository;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminRepository adminRepository;

	@Override
	public Course saveCourseDetails(Course course) {
		Course savedCourse = null;
		Course courseByTitle = adminRepository.findCourseByTitle(course.getTitle());
		if (courseByTitle != null) {
			throw new ResourceAlreadyExistException(
					"Course with the title :" + course.getTitle() + " already exist in database");
		} else {
			savedCourse = adminRepository.save(course);
		}
		return savedCourse;
	}

	@Override
	public List<Course> getAllCourses() {
		List<Course> listOfCourses = new ArrayList<>();
		try {
			listOfCourses = adminRepository.findAll();
		} catch (ResourseNotFoundException resourseNotFoundException) {
			throw new ResourseNotFoundException("Courses not found in database");
		}

		return listOfCourses;
	}

	@Override
	public Course getCourseById(Integer courseId) {
		Course course = adminRepository.findById(courseId)
				.orElseThrow(() -> new ResourseNotFoundException("Course not found with Id :" + courseId));
		return course;
	}

}
