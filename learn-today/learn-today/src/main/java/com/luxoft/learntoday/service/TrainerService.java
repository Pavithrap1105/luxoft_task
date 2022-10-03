package com.luxoft.learntoday.service;

import com.luxoft.learntoday.entity.Student;
import com.luxoft.learntoday.entity.StudentDTO;
import com.luxoft.learntoday.entity.Trainer;
import com.luxoft.learntoday.entity.TrainerRequest;

public interface TrainerService {

	public Trainer saveTrainer(Trainer trainer);
	
	public boolean updatePassword(Trainer trainer,Integer trainerId);
	
	public Trainer addTrainer(TrainerRequest request);
}
