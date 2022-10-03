package com.luxoft.learntoday.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.luxoft.learntoday.entity.Course;
import com.luxoft.learntoday.exception.ResourceAlreadyExistException;
import com.luxoft.learntoday.exception.ResourseNotFoundException;
import com.luxoft.learntoday.repository.AdminRepository;
import com.luxoft.learntoday.service.AdminServiceImpl;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

	@Mock
	private AdminRepository adminRepository;

	@InjectMocks
	private AdminServiceImpl adminServiceImpl;

	private MockMvc mockMvc;

	Course course1 = new Course(1, "Learn java", 4560.0f, "Core java to advanced",
			"Thu Sep 28 2022 16:17:53 GMT+0530 (India Standard Time)");
	Course course2 = new Course(2, "Learn Python", 5560.0f, "Python advanced",
			"Thu Sep 29 2022 12:17:53 GMT+0530 (India Standard Time)");

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(adminServiceImpl).build();
	}

	@Test
	void saveCourseDetails_success() {

		when(adminRepository.save(course1)).thenReturn(course1);
		assertEquals(course1, adminServiceImpl.saveCourseDetails(course1));
	}

	@Test
	void saveCourseDetails_exception() throws ResourceAlreadyExistException {

		lenient().when(adminRepository.findCourseByTitle(course1.getTitle())).thenReturn(course1);
		lenient().when(adminRepository.save(course1)).thenThrow(ResourceAlreadyExistException.class);

		ResourceAlreadyExistException exception = assertThrows(ResourceAlreadyExistException.class, () -> {
			adminServiceImpl.saveCourseDetails(course1);
		});
		assertEquals(ResourceAlreadyExistException.class, exception.getClass());
	}

	@Test
	void getAllCourses_success() {
		List<Course> courses = new ArrayList<>();
		courses.add(course1);
		courses.add(course2);

		when(adminRepository.findAll()).thenReturn(courses);
		assertEquals(2, adminServiceImpl.getAllCourses().size());

	}

	@Test
	void getAllCourses_exception() {

		when(adminRepository.findAll()).thenThrow(ResourseNotFoundException.class);
		ResourseNotFoundException exception = assertThrows(ResourseNotFoundException.class, () -> {
			adminServiceImpl.getAllCourses();
		});
		assertEquals(ResourseNotFoundException.class, exception.getClass());
	}

	@Test
	void getCourseById_success() {

		when(adminRepository.findById(course1.getCourseId())).thenReturn(Optional.of(course1));
		assertEquals(course1, adminServiceImpl.getCourseById(course1.getCourseId()));
	}

	@Test
	void getCourseById_exception() {

		lenient().when(adminRepository.findById(3)).thenThrow(ResourseNotFoundException.class);
		ResourseNotFoundException exception = assertThrows(ResourseNotFoundException.class, () -> {
			adminServiceImpl.getCourseById(course1.getCourseId());
		});

		assertEquals(ResourseNotFoundException.class, exception.getClass());
	}

}
