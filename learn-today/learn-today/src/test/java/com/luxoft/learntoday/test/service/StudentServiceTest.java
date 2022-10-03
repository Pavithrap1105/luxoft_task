
package com.luxoft.learntoday.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.luxoft.learntoday.entity.Course;
import com.luxoft.learntoday.entity.Student;
import com.luxoft.learntoday.entity.StudentDTO;
import com.luxoft.learntoday.exception.ResourceAlreadyExistException;
import com.luxoft.learntoday.exception.ResourseNotFoundException;
import com.luxoft.learntoday.repository.AdminRepository;
import com.luxoft.learntoday.repository.StudentRepository;
import com.luxoft.learntoday.service.AdminServiceImpl;
import com.luxoft.learntoday.service.StudentServiceImpl;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

	@Mock
	private StudentRepository studentRepository;

	@InjectMocks
	private StudentServiceImpl studentServiceImpl;

	@Mock
	private AdminRepository adminRepository;

	@InjectMocks
	private AdminServiceImpl adminServiceImpl;

	@Mock
	Student student;

	private MockMvc mockMvc;

	Course course1 = new Course(1, "Learn java", 4560.0f, "Core java to advanced",
			"Thu Sep 28 2022 16:17:53 GMT+0530 (India Standard Time)");
	Course course2 = new Course(2, "Learn Python", 5560.0f, "Python advanced",
			"Thu Sep 29 2022 12:17:53 GMT+0530 (India Standard Time)");

	Student student1 = new Student(1, 1001, "Joe");
	Student student2 = new Student(2, 1002, "Smith");
	Student student3 = new Student(3, 1003, "Vijay");

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(studentServiceImpl).build();
	}

	@Test
	void enrollStudent_success() {

		when(studentRepository.findById(student1.getEnrollmentId())).thenReturn(Optional.ofNullable(null));
		Mockito.when(studentRepository.save(student1)).thenReturn(student1);
		assertEquals(student1, studentServiceImpl.enrollStudent(student1));
	}

	@Test
	void enrollStudent_exception() {

		when(studentRepository.findById(student1.getEnrollmentId())).thenReturn(Optional.ofNullable(student1));
		ResourceAlreadyExistException throwable = assertThrows(ResourceAlreadyExistException.class, () -> {
			studentServiceImpl.enrollStudent(student1);
		});
		assertEquals(ResourceAlreadyExistException.class, throwable.getClass());

	}

	@Test
	void getAllCourses_success() {

		List<Course> courses = new ArrayList<>();
		courses.add(course1);
		courses.add(course2);
		List<Course> list = courses.stream()
				.sorted((stud1, stud2) -> stud1.getStartDate().compareTo(stud2.getStartDate()))
				.collect(Collectors.toList());

		when(adminRepository.findAll()).thenReturn(list);
		assertEquals(2, studentServiceImpl.getAllCourses().size());
	}

	@Test
	void getAllCourses_exception() {

		when(adminRepository.findAll()).thenThrow(ResourseNotFoundException.class);
		ResourseNotFoundException exception = assertThrows(ResourseNotFoundException.class, () -> {
			studentServiceImpl.getAllCourses();
		});

		assertEquals(ResourseNotFoundException.class, exception.getClass());
	}

	@Test
	void deleteStudentEnrollment_success() {

		when(studentRepository.findById(student1.getEnrollmentId())).thenReturn(Optional.of(student1));
		studentServiceImpl.deleteStudentEnrollment(student1.getEnrollmentId());
		verify(studentRepository).deleteById(student1.getEnrollmentId());

	}

	@Test
	void deleteStudentEnrollment_exception() {

		when(studentRepository.findById(anyInt())).thenThrow(ResourseNotFoundException.class);

		ResourseNotFoundException exception = assertThrows(ResourseNotFoundException.class,
				() -> studentServiceImpl.deleteStudentEnrollment(anyInt()));

		assertEquals(ResourseNotFoundException.class, exception.getClass());

	}

	@Test
	void addStudent_success() {
		StudentDTO studentDTO = new StudentDTO();
		student1.setEnrollmentId(studentDTO.enrollmentId);
		student1.setStudentId(studentDTO.studentId);
		student1.setStudentName(studentDTO.studentName);
		lenient().when(adminRepository.findById(studentDTO.courseId)).thenReturn(Optional.ofNullable(course1));
		student1.setCourse(course1);
		lenient().when(studentRepository.save(student1)).thenReturn(student1);

		assertEquals(student1, studentServiceImpl.addStudent(studentDTO));

	}

	@Test
	void addStudent_exception() {

		StudentDTO studentDTO = new StudentDTO();
		studentDTO.courseId = 3;
		studentDTO.enrollmentId = 4;
		studentDTO.studentId = 1004;
		studentDTO.studentName = "Pavi";

		given(adminRepository.findById(anyInt())).willReturn(Optional.ofNullable(null));

		ResourseNotFoundException exception = assertThrows(ResourseNotFoundException.class, () -> {
			studentServiceImpl.addStudent(studentDTO);
		});

		assertEquals(ResourseNotFoundException.class, exception.getClass());

	}

}
