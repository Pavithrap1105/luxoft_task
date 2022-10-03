package com.luxoft.learntoday.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.luxoft.learntoday.entity.Course;
import com.luxoft.learntoday.entity.Student;
import com.luxoft.learntoday.entity.StudentDTO;
import com.luxoft.learntoday.entity.Trainer;
import com.luxoft.learntoday.entity.TrainerRequest;
import com.luxoft.learntoday.exception.ResourceAlreadyExistException;
import com.luxoft.learntoday.exception.ResourseNotFoundException;
import com.luxoft.learntoday.repository.AdminRepository;
import com.luxoft.learntoday.repository.TrainerRepository;
import com.luxoft.learntoday.service.AdminServiceImpl;
import com.luxoft.learntoday.service.TrainerServiceImpl;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

	@Mock
	private TrainerRepository trainerRepository;

	@InjectMocks
	private TrainerServiceImpl trainerServiceImpl;

	@Mock
	private AdminRepository adminRepository;

	@InjectMocks
	private AdminServiceImpl adminServiceImpl;

	@Mock
	Trainer trainer;

	private MockMvc mockMvc;

	Course course1 = new Course(1, "Learn java", 4560.0f, "Core java to advanced",
			"Thu Sep 28 2022 16:17:53 GMT+0530 (India Standard Time)");
	Course course2 = new Course(2, "Learn Python", 5560.0f, "Python advanced",
			"Thu Sep 29 2022 12:17:53 GMT+0530 (India Standard Time)");

	Trainer trainer1 = new Trainer(1, "Smitha", "Smit123");
	Trainer trainer2 = new Trainer(2, "Sam", "fgh");
	Trainer trainer3 = new Trainer(3, "Shourya", "sdff");

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(trainerServiceImpl).build();
	}

	@Test
	void saveTrainer_success() {

		when(trainerRepository.findById(trainer1.getTrainerId())).thenReturn(Optional.ofNullable(null));
		Mockito.when(trainerRepository.save(trainer1)).thenReturn(trainer1);
		assertEquals(trainer1, trainerServiceImpl.saveTrainer(trainer1));
	}

	@Test
	void saveTrainer_exception() {

		when(trainerRepository.findById(trainer1.getTrainerId())).thenReturn(Optional.ofNullable(trainer1));
		ResourceAlreadyExistException throwable = assertThrows(ResourceAlreadyExistException.class, () -> {
			trainerServiceImpl.saveTrainer(trainer1);
		});
		assertEquals(ResourceAlreadyExistException.class, throwable.getClass());

	}

	@Test
	void updatePassword_success() {

		lenient().when(trainerRepository.findById(trainer1.getTrainerId())).thenReturn(Optional.ofNullable(trainer1));
		lenient().when(trainerRepository.updatePassword(trainer1.getPassword(), trainer1.getTrainerId())).thenReturn(1);
		assertEquals(true, trainerServiceImpl.updatePassword(trainer1, trainer1.getTrainerId()));
	}

	@Test
	void updatePassword_exception() {

		given(trainerRepository.findById(anyInt())).willReturn(Optional.ofNullable(null));
		ResourseNotFoundException throwable = assertThrows(ResourseNotFoundException.class, () -> {
			trainerServiceImpl.updatePassword(trainer2, trainer1.getTrainerId());
		});
		assertEquals(ResourseNotFoundException.class, throwable.getClass());
	}

	@Test
	void addTrainer_success() {

		TrainerRequest request = new TrainerRequest();
		trainer1.setTrainerId(request.trainerId);
		trainer1.setTrainerName(request.trainerName);
		trainer1.setPassword(request.password);
		lenient().when(adminRepository.findById(request.courseId)).thenReturn(Optional.ofNullable(course1));
		trainer1.setCourse(course1);
		lenient().when(trainerRepository.save(trainer1)).thenReturn(trainer1);
		assertEquals(trainer1, trainerServiceImpl.addTrainer(request));

	}

	@Test
	void addTrainer_exception() {

		TrainerRequest request = new TrainerRequest();
		request.courseId = 3;
		request.trainerId = 4;
		request.trainerName = "Pavi";
		request.password = "Pavi123";

		given(adminRepository.findById(anyInt())).willReturn(Optional.ofNullable(null));

		ResourseNotFoundException exception = assertThrows(ResourseNotFoundException.class, () -> {
			trainerServiceImpl.addTrainer(request);
		});

		assertEquals(ResourseNotFoundException.class, exception.getClass());

	}

}
