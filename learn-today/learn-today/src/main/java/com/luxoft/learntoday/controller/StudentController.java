package com.luxoft.learntoday.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luxoft.learntoday.entity.Course;
import com.luxoft.learntoday.entity.Student;
import com.luxoft.learntoday.entity.StudentDTO;
import com.luxoft.learntoday.exception.ResourseNotFoundException;
import com.luxoft.learntoday.service.StudentService;

@RestController
@RequestMapping("/student")
public class StudentController {

	@Autowired
	private StudentService studentService;

	@PostMapping("/save")
	public ResponseEntity<?> saveStudent(@Valid @RequestBody Student student) {
		try {
			Student savedStudent = studentService.enrollStudent(student);
			return new ResponseEntity<Student>(savedStudent, HttpStatus.CREATED);
		} catch (Exception ex) {
			return new ResponseEntity<Exception>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/getAllCourses")
	public ResponseEntity<List<Course>> getListOfCourses() {
		List<Course> listOfCourses = studentService.getAllCourses();
		return new ResponseEntity<List<Course>>(listOfCourses, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteStudentEnrollment(@PathVariable("id") Integer enrollmentId) {
		try {
			Boolean status = studentService.deleteStudentEnrollment(enrollmentId);
			return new ResponseEntity<Boolean>(HttpStatus.OK);
		} catch (ResourseNotFoundException ex) {
			ex.printStackTrace();
			return new ResponseEntity<String>("No enrollment information found", HttpStatus.NOT_FOUND);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity<Exception>(HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("/assignStudent")
	public ResponseEntity<Student> assignStudentToCourse(@Valid @RequestBody StudentDTO dto) {
		Student student = studentService.addStudent(dto);
		return new ResponseEntity<Student>(student, HttpStatus.OK);
	}

}
