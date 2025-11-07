package com.example.eventmanagementsystem.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.eventmanagementsystem.entity.Registration;

@Repository
public interface RegistrationRepository extends MongoRepository<Registration, String>{

	List<Registration> findByUserId(String userId);
    List<Registration> findByEventId(String eventId);
    boolean existsByUserIdAndEventId(String userId, String eventId);
}
