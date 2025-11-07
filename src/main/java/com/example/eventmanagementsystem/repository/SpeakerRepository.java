package com.example.eventmanagementsystem.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.eventmanagementsystem.entity.Speaker;

public interface SpeakerRepository extends MongoRepository<Speaker,String>{

}
