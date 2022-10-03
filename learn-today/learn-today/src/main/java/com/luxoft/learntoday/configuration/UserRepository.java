package com.luxoft.learntoday.configuration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.luxoft.learntoday.dto.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
	
	@Query("from UserEntity where username=:username")
	public UserEntity findByUsername(@Param("username")  String username);

}
