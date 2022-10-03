package com.luxoft.learntoday.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.luxoft.learntoday.entity.Course;

public interface AdminRepository extends JpaRepository<Course, Integer> {

	@Query("from Course where title=:title")
	Course findCourseByTitle(@Param("title") String title);

}
