package com.luxoft.learntoday.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.luxoft.learntoday.entity.Trainer;

public interface TrainerRepository extends JpaRepository<Trainer, Integer> {

	@Modifying
	@Transactional
	@Query("update Trainer set password=:password where trainerId=:trainerId")
	int updatePassword(@Param("password") String password, @Param("trainerId") Integer trainerId);

}
