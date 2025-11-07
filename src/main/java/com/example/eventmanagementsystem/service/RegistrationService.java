package com.example.eventmanagementsystem.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.eventmanagementsystem.entity.Event;
import com.example.eventmanagementsystem.entity.Registration;
import com.example.eventmanagementsystem.entity.User;
import com.example.eventmanagementsystem.repository.EventRepository;
import com.example.eventmanagementsystem.repository.RegistrationRepository;
import com.example.eventmanagementsystem.repository.UserRepository;

@Service
public class RegistrationService {

	 private final RegistrationRepository registrationRepository;
	  
	    private final UserRepository userRepository;
	    private final EventRepository eventRepository;

	    public RegistrationService(RegistrationRepository registrationRepository,
	                             EventRepository eventRepository,UserRepository userRepository) {
	        this.registrationRepository = registrationRepository;
	    
	        this.userRepository=userRepository;
	        this.eventRepository=eventRepository;
	    }

	    public void markAttended(String registrationId, boolean attended) {
	        Registration reg = registrationRepository.findById(registrationId)
	            .orElseThrow(() -> new IllegalArgumentException("Registration not found: " + registrationId));

	        reg.setAttended(attended);
	        if (attended) {
	            reg.setAttendanceTimestamp(LocalDateTime.now());
	        } else {
	            reg.setAttendanceTimestamp(null);
	        }
	        registrationRepository.save(reg);
	    }

	    public List<Registration> getRegistrationsForEvent(String eventId) {
	        return registrationRepository.findByEventId(eventId);
	    }

	    public List<Registration> getRegistrationsForUser(String userId) {
	        return registrationRepository.findByUserId(userId);
	    }
	    public String getEventIdForRegistration(String registrationId) {
	        return registrationRepository.findById(registrationId)
	                .map(Registration::getEventId)
	                .orElseThrow(() -> new NoSuchElementException(
	                        "No registration found with id: " + registrationId));
	    }
	    public List<Registration> findByEventId(String eventId) {
	        return registrationRepository.findByEventId(eventId);
	    }
	    public boolean isUserRegistered(String userEmail, String eventId) {
	        User user = userRepository.findByEmail(userEmail)
	                   .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + userEmail));

	        String userId = user.getId();  // or user.getId(), whichever your field is

	        return registrationRepository.existsByUserIdAndEventId(userId, eventId);
	    }
	    public Registration registerUserForEvent(String userEmail, String eventId) {
	        // 1. Load user by email
	        User user = userRepository.findByEmail(userEmail)
	                .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + userEmail));

	        // 2. Load event by id
	        Event event = eventRepository.findById(eventId)
	                .orElseThrow(() -> new IllegalArgumentException("Event not found for id: " + eventId));

	        // 3. Check if already registered
	        boolean already = registrationRepository.existsByUserIdAndEventId(user.getId(), eventId);
	        if (already) {
	            throw new IllegalArgumentException("User " + userEmail + " is already registered for event " + eventId);
	        }

	        // 4. Create registration record
	        Registration registration = new Registration();
	        registration.setUserId(user.getId());
	        registration.setEventId(eventId);
	        registration.setAttendanceTimestamp(LocalDateTime.now());
	        registration.setAttended(false);

	        registration = registrationRepository.save(registration);

	        return registration;
	    }
}
