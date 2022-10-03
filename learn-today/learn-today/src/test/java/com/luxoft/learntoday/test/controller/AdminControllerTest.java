package com.luxoft.learntoday.test.controller;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.error.ShouldHaveSameSizeAs;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.luxoft.learntoday.controller.AdminController;
import com.luxoft.learntoday.entity.Course;
import com.luxoft.learntoday.entity.Student;
import com.luxoft.learntoday.exception.ResourceAlreadyExistException;
import com.luxoft.learntoday.exception.ResourseNotFoundException;
import com.luxoft.learntoday.repository.AdminRepository;
import com.luxoft.learntoday.service.AdminServiceImpl;

@SpringBootTest
class AdminControllerTest {

	@Mock
	private AdminServiceImpl adminServiceImpl;

	@InjectMocks
	private AdminController adminController;

	@Mock
	private AdminRepository adminRepository;

	ObjectMapper objMap = new ObjectMapper();
	ObjectWriter writer = objMap.writer();

	private MockMvc mockMvc;

	Course course1 = new Course(1, "Learn java", 4560.0f, "Core java to advanced",
			"Thu Sep 28 2022 16:17:53 GMT+0530 (India Standard Time)");
	Course course2 = new Course(2, "Learn Python", 5560.0f, "Python advanced",
			"Thu Sep 29 2022 12:17:53 GMT+0530 (India Standard Time)");

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
	}

	@Test
	void saveCourse_success() throws Exception {

		Course course = Course.builder().courseId(3).title("Testing").fees(5600.0f).description("Manual testing course")
				.startDate("Thu Sep 30 2022 12:17:53 GMT+0530 (India Standard Time)").build();

		Mockito.when(adminServiceImpl.saveCourseDetails(course)).thenReturn(course);

		String content = writer.writeValueAsString(course);

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/admin/save")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(content);

		mockMvc.perform(requestBuilder).andExpect(status().isCreated()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.title", is("Testing")));

	}

	@Test
	void saveCourse_exception() throws Exception {

		Course course = Course.builder().courseId(3).title("Testing").fees(5600.0f).description("Manual testing course")
				.startDate("Thu Sep 30 2022 12:17:53 GMT+0530 (India Standard Time)").build();

		lenient().when(adminServiceImpl.saveCourseDetails(any(Course.class))).thenReturn(null);

		String str = writer.writeValueAsString(course);

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/admin/save")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(str);

		mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());

	}

	@Test
	void getListOfCourses_success() throws Exception {
		List<Course> courses = new ArrayList<>();
		courses.add(course1);
		courses.add(course2);

		Mockito.when(adminServiceImpl.getAllCourses()).thenReturn(courses);

		mockMvc.perform(MockMvcRequestBuilders.get("/admin/getAllCourses").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].title", is("Learn java")))
				.andExpect(jsonPath("$[1].title", is("Learn Python")));
	}

	@Test
	void getListOfCourses_exception() throws Exception {
		List<Course> courses = null;

		Mockito.when(adminServiceImpl.getAllCourses()).thenThrow(new ResourseNotFoundException("Course not found"));

		assertThatThrownBy(() -> mockMvc.perform(MockMvcRequestBuilders.get("/admin/getAllCourses"))
				.andExpect(status().isNotFound())).hasCause(new ResourseNotFoundException("Course not found"));
	}

	@Test
	void getCourseById_success() throws Exception {

		Mockito.when(adminServiceImpl.getCourseById(course1.getCourseId())).thenReturn(course1);

		mockMvc.perform(MockMvcRequestBuilders.get("/admin/getCourse/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.title", is("Learn java")));

	}

	@Test
	void getCourseById_exception() throws Exception {

		Mockito.when(adminServiceImpl.getCourseById(3)).thenReturn(null);

		mockMvc.perform(MockMvcRequestBuilders.get("/admin/getCourse/3").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

	}

}
