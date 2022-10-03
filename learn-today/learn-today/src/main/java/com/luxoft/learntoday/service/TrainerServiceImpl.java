package com.luxoft.learntoday.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luxoft.learntoday.entity.Course;
import com.luxoft.learntoday.entity.Student;
import com.luxoft.learntoday.entity.Trainer;
import com.luxoft.learntoday.entity.TrainerRequest;
import com.luxoft.learntoday.exception.ResourceAlreadyExistException;
import com.luxoft.learntoday.exception.ResourseNotFoundException;
import com.luxoft.learntoday.repository.AdminRepository;
import com.luxoft.learntoday.repository.TrainerRepository;

@Service
public class TrainerServiceImpl implements TrainerService {

	@Autowired
	private TrainerRepository trainerRepository;

	@Autowired
	private AdminRepository adminRepository;

	@Override
	public Trainer saveTrainer(Trainer trainer) {
		Optional<Trainer> existTrainer = trainerRepository.findById(trainer.getTrainerId());
		if (!existTrainer.isEmpty()) {
			throw new ResourceAlreadyExistException("Trainer is found with trainer id: " + trainer.getTrainerId());
		}
		Trainer saved = trainerRepository.save(trainer);

		return saved;
	}

	@Override
	public boolean updatePassword(Trainer trainer, Integer trainerId) {

		Trainer existingTrainer = trainerRepository.findById(trainerId)
				.orElseThrow(() -> new ResourseNotFoundException("Trainer not found"));
		existingTrainer.setTrainerId(trainer.getTrainerId());
		existingTrainer.setTrainerName(trainer.getTrainerName());
		existingTrainer.setPassword(trainer.getPassword());
		trainerRepository.save(existingTrainer);
		return true;
	}

	@Override
	public Trainer addTrainer(TrainerRequest request) {
		Course course = adminRepository.findById(request.courseId)
				.orElseThrow(() -> new ResourseNotFoundException("Course not found"));
		Trainer trainer = new Trainer();
		trainer.setTrainerId(request.trainerId);
		trainer.setTrainerName(request.trainerName);
		trainer.setPassword(request.password);
		trainer.setCourse(course);
		Trainer savedTrainer = trainerRepository.save(trainer);
		return savedTrainer;
	}

}
