package com.luxoft.learntoday.test.controller;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.luxoft.learntoday.controller.AdminController;
import com.luxoft.learntoday.controller.StudentController;
import com.luxoft.learntoday.entity.Course;
import com.luxoft.learntoday.entity.Student;
import com.luxoft.learntoday.entity.StudentDTO;
import com.luxoft.learntoday.exception.ResourceAlreadyExistException;
import com.luxoft.learntoday.exception.ResourseNotFoundException;
import com.luxoft.learntoday.repository.AdminRepository;
import com.luxoft.learntoday.service.AdminServiceImpl;
import com.luxoft.learntoday.service.StudentServiceImpl;

@SpringBootTest
class StudentControllerTest {

	@Mock
	private StudentServiceImpl studentServiceImpl;

	@InjectMocks
	private StudentController studentController;

	ObjectMapper objMap = new ObjectMapper();
	ObjectWriter writer = objMap.writer();

	private MockMvc mockMvc;

	Student student1 = new Student(1, 1001, "Joe");
	Student student2 = new Student(2, 1002, "Smith");
	Student student3 = new Student(3, 1003, "Vijay");

	Course course1 = new Course(1, "Learn java", 4560.0f, "Core java to advanced",
			"Thu Sep 28 2022 16:17:53 GMT+0530 (India Standard Time)");
	Course course2 = new Course(2, "Learn Python", 5560.0f, "Python advanced",
			"Thu Sep 29 2022 12:17:53 GMT+0530 (India Standard Time)");

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
	}

	@Test
	void saveStudent_success() throws Exception {
		Student student = Student.builder().enrollmentId(4).studentId(1004).studentName("Sam").build();

		Mockito.when(studentServiceImpl.enrollStudent(student)).thenReturn(student);

		String content = writer.writeValueAsString(student);

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/student/save")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(content);

		mockMvc.perform(requestBuilder).andExpect(status().isCreated()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.studentName", is("Sam")));
	}

	@Test
	void saveStudent_exception() throws ResourceAlreadyExistException, Exception {

		Student student = Student.builder().enrollmentId(3).studentId(1003).studentName("Vijay").build();

		when(studentServiceImpl.enrollStudent(any(Student.class))).thenThrow(ResourceAlreadyExistException.class);
		String str = writer.writeValueAsString(student);

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/student/save")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(str);

		mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());

	}

	@Test
	void getListOfCourses_success() throws Exception {
		List<Course> courses = new ArrayList<>();
		courses.add(course1);
		courses.add(course2);

		List<Course> list = courses.stream()
				.sorted((stud1, stud2) -> stud1.getStartDate().compareTo(stud2.getStartDate()))
				.collect(Collectors.toList());

		Mockito.when(studentServiceImpl.getAllCourses()).thenReturn(list);

		mockMvc.perform(MockMvcRequestBuilders.get("/student/getAllCourses").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].title", is("Learn java")))
				.andExpect(jsonPath("$[1].title", is("Learn Python")));
	}

	@Test
	void getListOfCourses_exception() throws Exception {
		List<Course> courses = null;

		Mockito.when(studentServiceImpl.getAllCourses()).thenThrow(new ResourseNotFoundException("Course not found"));

		assertThatThrownBy(() -> mockMvc.perform(MockMvcRequestBuilders.get("/student/getAllCourses"))
				.andExpect(status().isNotFound())).hasCause(new ResourseNotFoundException("Course not found"));
	}

	@Test
	void deleteStudentEnrollment_success() throws Exception {

		Mockito.when(studentServiceImpl.deleteStudentEnrollment(student1.getEnrollmentId())).thenReturn(true);

		mockMvc.perform(MockMvcRequestBuilders.delete("/student/delete/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

	}

	@Test
	void deleteStudentEnrollment_exception() throws Exception {

		Mockito.when(studentServiceImpl.deleteStudentEnrollment(4)).thenThrow(ResourseNotFoundException.class);

		mockMvc.perform(MockMvcRequestBuilders.delete("/student/delete/4").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

	}

	@Test
	void deleteStudentEnrollment_exception1() throws Exception {

		Mockito.when(studentServiceImpl.deleteStudentEnrollment(anyInt())).thenThrow(new RuntimeException());

		mockMvc.perform(MockMvcRequestBuilders.delete("/student/delete/4").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void assignStudentToCourse_success() throws Exception {

		StudentDTO studentDTO = new StudentDTO();
		student1.setEnrollmentId(studentDTO.enrollmentId);
		student1.setStudentId(studentDTO.studentId);
		student1.setStudentName(studentDTO.studentName);

		Mockito.when(studentServiceImpl.addStudent(studentDTO)).thenReturn(student1);

		String content = writer.writeValueAsString(student1);

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/student/assignStudent")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(content);

		mockMvc.perform(requestBuilder).andExpect(status().isOk());

	}

}
