package com.luxoft.learntoday.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luxoft.learntoday.entity.Student;
import com.luxoft.learntoday.entity.StudentDTO;
import com.luxoft.learntoday.entity.Trainer;
import com.luxoft.learntoday.entity.TrainerRequest;
import com.luxoft.learntoday.service.TrainerService;

@RestController
@RequestMapping("/trainer")
public class TrainerController {

	@Autowired
	private TrainerService trainerService;

	@PostMapping("/save")
	public ResponseEntity<?> trainerSignUp(@Valid @RequestBody Trainer trainer) {
		Trainer saved = trainerService.saveTrainer(trainer);
		if (saved != null) {
			return new ResponseEntity<Trainer>(saved, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<Exception>(HttpStatus.BAD_REQUEST);
		}

	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> updatePassword(@Valid @RequestBody Trainer trainer,
			@PathVariable("id") Integer trainerId) {
		boolean trainerUpdated = false;
		try {
			trainerUpdated = trainerService.updatePassword(trainer, trainerId);
			if (trainerUpdated) {
				return new ResponseEntity<String>("Data updated successfully", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Searched data not found", HttpStatus.NOT_FOUND);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity<Exception>(HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("/assignTrainer")
	public ResponseEntity<Trainer> assignTrainerToCourse(@RequestBody TrainerRequest request) {
		Trainer trainer = trainerService.addTrainer(request);
		return new ResponseEntity<Trainer>(trainer, HttpStatus.OK);
	}

}
