package com.luxoft.learntoday.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luxoft.learntoday.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

}
