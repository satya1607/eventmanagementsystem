package com.example.eventmanagementsystem.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.eventmanagementsystem.entity.Event;
import com.example.eventmanagementsystem.entity.User;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {

	List<Event> findByVenue(String venue);
    List<Event> findByCategory(String category);
    List<Event> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);
}