package com.luxoft.learntoday.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luxoft.learntoday.entity.Course;
import com.luxoft.learntoday.exception.ResourceAlreadyExistException;
import com.luxoft.learntoday.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;

	@PostMapping("/save")
	public ResponseEntity<?> saveCourse(@Valid @RequestBody Course course) {
		Course courseDetails = adminService.saveCourseDetails(course);
		if (courseDetails != null) {
			return new ResponseEntity<Course>(courseDetails, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<String>("Course already exist with title :" + course.getTitle(),
					HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping("/getAllCourses")
	public ResponseEntity<List<Course>> getListOfCourses() {
		List<Course> allCourses = adminService.getAllCourses();
		return new ResponseEntity<List<Course>>(allCourses, HttpStatus.OK);
	}

	@GetMapping("/getCourse/{id}")
	public ResponseEntity<Course> getCourseById(@PathVariable("id") Integer courseId) {
		Course courseById = adminService.getCourseById(courseId);
		if (courseById != null) {
			return new ResponseEntity<Course>(courseById, HttpStatus.OK);
		} else {
			return new ResponseEntity<Course>(courseById, HttpStatus.NOT_FOUND);
		}

	}
}
