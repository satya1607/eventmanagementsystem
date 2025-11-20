package com.example.eventmanagementsystem.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.eventmanagementsystem.entity.Event;
import com.example.eventmanagementsystem.entity.Registration;
import com.example.eventmanagementsystem.entity.User;
import com.example.eventmanagementsystem.service.EventService;
import com.example.eventmanagementsystem.service.RegistrationService;
import com.example.eventmanagementsystem.service.UserService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(AttendanceController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AttendanceControllerTest {
	
	@MockBean
    private RegistrationService registrationService;

    @MockBean
    private EventService eventService;

    @MockBean
    private Model model;

    @MockBean
    private RedirectAttributes redirectAttributes;
    
    @Autowired
    private MockMvc mockMvc;

    private Event event;
    private Registration registration;

    @BeforeEach
    void setUp() {
        
        event = new Event();
        event.setId("1");
        event.setName("Tech Conference");
        event.setVenue("Bangalore");
        event.setCategory("Technology");
        event.setDateTime(LocalDateTime.now());

        User user=new User();
        
        registration = new Registration();
        registration.setId("r1");
        registration.setEvent(event);
        registration.setUser(user);
        registration.setAttended(false);
    }

    // ✅ Test GET mapping: /attendance/{id}
    @Test
    void testShowAttendanceForm_ShouldReturnAttendanceView() throws Exception {
        when(eventService.findById("1")).thenReturn(event);
        when(registrationService.findByEventId("1")).thenReturn(List.of(registration));

        mockMvc.perform(get("/admin/events/attendance/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/events/attendance"))
                .andExpect(model().attributeExists("event"))
                .andExpect(model().attributeExists("registrations"));

        verify(eventService).findById("1");
        verify(registrationService).findByEventId("1");
    }

    // ✅ Test POST mapping: /attendance/mark (success)
    @Test
    void testMarkAttendance_Success() {
    	RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        Event mockEvent = new Event();
        mockEvent.setId("1");

        when(registrationService.getEventIdForRegistration("r1")).thenReturn(mockEvent);

        AttendanceController controller = new AttendanceController(registrationService, eventService);

        String view = controller.markAttendance("r1", true, redirectAttributes);

        assertThat(view).isEqualTo("redirect:/admin/events/attendance/1");

        verify(registrationService).markAttended("r1", true);
        verify(redirectAttributes).addFlashAttribute(eq("successMessage"), anyString());
    }

    // ✅ Test POST mapping: /attendance/mark (failure)
    @Test
    void testMarkAttendance_Failure() {
        doThrow(new RuntimeException("Database error"))
                .when(registrationService).markAttended("r1", true);
        when(registrationService.getEventIdForRegistration("r1")).thenReturn(event);

        String view = new AttendanceController(registrationService, eventService)
                .markAttendance("r1", true, redirectAttributes);
        
        assertThat(view).isEqualTo("redirect:/admin/events/attendance/1");
        verify(redirectAttributes).addFlashAttribute(eq("errorMessage"), contains("Error updating attendance"));
    }
}
