package com.example.eventmanagementsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.example.eventmanagementsystem.entity.Event;
import com.example.eventmanagementsystem.entity.Registration;
import com.example.eventmanagementsystem.entity.User;
import com.example.eventmanagementsystem.repository.EventRepository;
import com.example.eventmanagementsystem.repository.RegistrationRepository;
import com.example.eventmanagementsystem.repository.UserRepository;

public class RegistrationServiceTest {

	 @Mock
	    private RegistrationRepository registrationRepository;

	    @Mock
	    private EventRepository eventRepository;

	    @Mock
	    private JavaMailSender mailSender;

	    @Mock
	    private UserRepository userRepository;

	    @InjectMocks
	    private RegistrationService registrationService;

	    @BeforeEach
	    void setUp() {
	        MockitoAnnotations.openMocks(this);
	    }

	    @Test
	    void testMarkAttended_ShouldUpdateAttendance() {
	        Registration reg = new Registration();
	        reg.setId("1");
	        reg.setAttended(false);

	        when(registrationRepository.findById("1")).thenReturn(Optional.of(reg));
	        when(registrationRepository.save(any(Registration.class))).thenReturn(reg);

	        registrationService.markAttended("1", true);

	        assertTrue(reg.isAttended());
	        assertNotNull(reg.getAttendanceTimestamp());
	        verify(registrationRepository, times(1)).save(reg);
	    }

	    @Test
	    void testMarkAttended_RegistrationNotFound_ShouldThrowException() {
	        when(registrationRepository.findById("123")).thenReturn(Optional.empty());
	        assertThrows(IllegalArgumentException.class, () -> registrationService.markAttended("123", true));
	    }

	    @Test
	    void testGetRegistrationsForEvent() {
	        List<Registration> regs = List.of(new Registration(), new Registration());
	        when(registrationRepository.findByEventId("event1")).thenReturn(regs);

	        List<Registration> result = registrationService.getRegistrationsForEvent("event1");
	        assertEquals(2, result.size());
	    }

	    @Test
	    void testGetRegistrationsForUser() {
	        List<Registration> regs = List.of(new Registration());
	        when(registrationRepository.findByUserId("user1")).thenReturn(regs);

	        List<Registration> result = registrationService.getRegistrationsForUser("user1");
	        assertEquals(1, result.size());
	    }

	    @Test
	    void testGetEventIdForRegistration() {
	        Registration reg = new Registration();
	        reg.setEventId("E123");
	        when(registrationRepository.findById("R1")).thenReturn(Optional.of(reg));

	        String eventId = registrationService.getEventIdForRegistration("R1");
	        assertEquals("E123", eventId);
	    }

	    @Test
	    void testGetEventIdForRegistration_NotFound() {
	        when(registrationRepository.findById("R2")).thenReturn(Optional.empty());
	        assertThrows(NoSuchElementException.class, () -> registrationService.getEventIdForRegistration("R2"));
	    }

	    @Test
	    void testIsUserRegistered_ShouldReturnTrue() {
	        User user = new User();
	        user.setId("U1");
	        user.setEmail("test@example.com");

	        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
	        when(registrationRepository.existsByUserIdAndEventId("U1", "E1")).thenReturn(true);

	        boolean result = registrationService.isUserRegistered("test@example.com", "E1");
	        assertTrue(result);
	    }

	    @Test
	    void testIsUserRegistered_UserNotFound_ShouldThrowException() {
	        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());
	        assertThrows(IllegalArgumentException.class,
	                () -> registrationService.isUserRegistered("missing@example.com", "E1"));
	    }

	    @Test
	    void testRegisterUserForEvent_Success() {
	        User user = new User();
	        user.setId("U1");
	        user.setUsername("Priyanka");
	        user.setEmail("priya@example.com");

	        Event event = new Event();
	        event.setId("E1");
	        event.setName("Tech Conference");
	        event.setVenue("Hyderabad");
	        event.setDateTime(LocalDateTime.now());

	        Registration saved = new Registration();
	        saved.setId("R1");
	        saved.setUserId("U1");
	        saved.setEventId("E1");

	        when(userRepository.findByEmail("priya@example.com")).thenReturn(Optional.of(user));
	        when(eventRepository.findById("E1")).thenReturn(Optional.of(event));
	        when(registrationRepository.existsByUserIdAndEventId("U1", "E1")).thenReturn(false);
	        when(registrationRepository.save(any(Registration.class))).thenReturn(saved);

	        Registration result = registrationService.registerUserForEvent("priya@example.com", "E1");

	        assertEquals("E1", result.getEventId());
	        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
	    }

	    @Test
	    void testRegisterUserForEvent_UserNotFound() {
	        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());
	        assertThrows(IllegalArgumentException.class,
	                () -> registrationService.registerUserForEvent("missing@example.com", "E1"));
	    }

	    @Test
	    void testRegisterUserForEvent_EventNotFound() {
	        User user = new User();
	        user.setId("U1");
	        when(userRepository.findByEmail("priya@example.com")).thenReturn(Optional.of(user));
	        when(eventRepository.findById("E1")).thenReturn(Optional.empty());

	        assertThrows(IllegalArgumentException.class,
	                () -> registrationService.registerUserForEvent("priya@example.com", "E1"));
	    }

	    @Test
	    void testRegisterUserForEvent_AlreadyRegistered() {
	        User user = new User();
	        user.setId("U1");
	        Event event = new Event();
	        event.setId("E1");

	        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
	        when(eventRepository.findById("E1")).thenReturn(Optional.of(event));
	        when(registrationRepository.existsByUserIdAndEventId("U1", "E1")).thenReturn(true);

	        assertThrows(IllegalArgumentException.class,
	                () -> registrationService.registerUserForEvent("test@example.com", "E1"));
	    }
	}

