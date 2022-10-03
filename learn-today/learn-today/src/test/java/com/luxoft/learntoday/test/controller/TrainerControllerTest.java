package com.luxoft.learntoday.test.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.luxoft.learntoday.controller.TrainerController;
import com.luxoft.learntoday.entity.StudentDTO;
import com.luxoft.learntoday.entity.Trainer;
import com.luxoft.learntoday.entity.TrainerRequest;
import com.luxoft.learntoday.repository.AdminRepository;
import com.luxoft.learntoday.service.TrainerServiceImpl;

@SpringBootTest
class TrainerControllerTest {

	@Mock
	private TrainerServiceImpl trainerServiceImpl;

	@InjectMocks
	private TrainerController trainerController;

	@Mock
	private AdminRepository adminRepository;

	ObjectMapper objMap = new ObjectMapper();
	ObjectWriter writer = objMap.writer();

	private MockMvc mockMvc;

	Trainer trainer1 = new Trainer(1, "Smitha", "Smit123");
	Trainer trainer2 = new Trainer(2, "Sam", "fgh");
	Trainer trainer3 = new Trainer(3, "Shourya", "sdff");

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(trainerController).build();
	}

	@Test
	void trainerSignUp_success() throws Exception {

		Mockito.when(trainerServiceImpl.saveTrainer(trainer1)).thenReturn(trainer1);

		String content = writer.writeValueAsString(trainer1);

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/trainer/save")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(content);

		mockMvc.perform(requestBuilder).andExpect(status().isCreated()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.trainerName", is("Smitha")));
	}

	@Test
	void trainerSignUp_exception() throws Exception {

		Mockito.when(trainerServiceImpl.saveTrainer(any(Trainer.class))).thenReturn(null);

		String content = writer.writeValueAsString(trainer1);

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/trainer/save")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(content);

		mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
	}

	@Test
	void updatePassword_success() throws Exception {
		Mockito.when(trainerServiceImpl.updatePassword(trainer1, trainer1.getTrainerId())).thenReturn(true);

		String content = writer.writeValueAsString(trainer1);

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/trainer/update/1")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(content);

		mockMvc.perform(requestBuilder).andExpect(status().isOk());

	}

	@Test
	void updatePassword_exception() throws Exception {
		Mockito.when(trainerServiceImpl.updatePassword(trainer1, trainer1.getTrainerId())).thenReturn(false);

		String content = writer.writeValueAsString(trainer1);

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/trainer/update/1")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(content);

		mockMvc.perform(requestBuilder).andExpect(status().isNotFound());

	}

	@Test
	void updatePassword_exception1() throws Exception {
		Mockito.when(trainerServiceImpl.updatePassword(any(Trainer.class), anyInt())).thenThrow(RuntimeException.class);

		String content = writer.writeValueAsString(trainer1);

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/trainer/update/1")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(content);

		mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());

	}

	@Test
	void assignTrainerToCourse_success() throws Exception {
		TrainerRequest trainerRequest = new TrainerRequest();
		trainer1.setTrainerId(trainerRequest.trainerId);
		trainer1.setTrainerName(trainerRequest.trainerName);
		trainer1.setPassword(trainerRequest.password);

		Mockito.when(trainerServiceImpl.addTrainer(trainerRequest)).thenReturn(trainer1);

		String content = writer.writeValueAsString(trainer1);

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/trainer/assignTrainer")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(content);

		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}

}
