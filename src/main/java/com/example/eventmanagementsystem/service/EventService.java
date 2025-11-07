package com.example.eventmanagementsystem.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.eventmanagementsystem.entity.Event;
import com.example.eventmanagementsystem.entity.User;
import com.example.eventmanagementsystem.repository.EventRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EventService {
	
	private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public Event updateEvent(String id, Event updated) {
        Optional<Event> opt = eventRepository.findById(id);
        if (opt.isEmpty()) {
            throw new RuntimeException("Event not found");
        }
        updated.setId(id);
        return eventRepository.save(updated);
    }

    public void deleteEvent(String id) {
        eventRepository.deleteById(id);
    }

    public List<Event> listAll() {
        return eventRepository.findAll();
    }

    public List<Event> searchByVenue(String venue) {
        return eventRepository.findByVenue(venue);
    }

    public List<Event> searchByCategory(String category) {
        return eventRepository.findByCategory(category);
    }

    public List<Event> searchByDateRange(LocalDateTime start, LocalDateTime end) {
        return eventRepository.findByDateTimeBetween(start, end);
    }
    
    public Optional<Event> findById(String id) {
    	return eventRepository.findById(id);
 
    }
	
}
