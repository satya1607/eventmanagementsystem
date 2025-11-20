package com.example.eventmanagementsystem.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.eventmanagementsystem.entity.Speaker;

@Repository
public interface SpeakerRepository extends MongoRepository<Speaker,String>{

}
