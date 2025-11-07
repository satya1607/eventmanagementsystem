package com.example.eventmanagementsystem.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.eventmanagementsystem.entity.Event;
import com.example.eventmanagementsystem.entity.Registration;
import com.example.eventmanagementsystem.service.EventService;
import com.example.eventmanagementsystem.service.RegistrationService;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class AttendanceControllerTest {

	@Mock
    private RegistrationService registrationService;

    @Mock
    private EventService eventService;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private AttendanceController attendanceController;

    private MockMvc mockMvc;

    private Event event;
    private Registration registration;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(attendanceController).build();

        event = new Event();
        event.setId("1");
        event.setName("Tech Conference");
        event.setVenue("Bangalore");
        event.setCategory("Technology");
        event.setDateTime(LocalDateTime.now());

        registration = new Registration();
        registration.setId("r1");
        registration.setEventId("1");
        registration.setUserId("u1");
        registration.setAttended(false);
    }

    // ✅ Test GET mapping: /attendance/{id}
    @Test
    void testShowAttendanceForm_ShouldReturnAttendanceView() throws Exception {
        when(eventService.findById("1")).thenReturn(Optional.of(event));
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
        when(registrationService.getEventIdForRegistration("r1")).thenReturn("1");

        String view = attendanceController.markAttendance("r1", true, redirectAttributes);

        assertThat(view).isEqualTo("redirect:/admin/events/attendance/1");
        verify(registrationService).markAttended("r1", true);
        verify(redirectAttributes).addFlashAttribute(eq("successMessage"), anyString());
    }

    // ✅ Test POST mapping: /attendance/mark (failure)
    @Test
    void testMarkAttendance_Failure() {
        doThrow(new RuntimeException("Database error"))
                .when(registrationService).markAttended("r1", true);
        when(registrationService.getEventIdForRegistration("r1")).thenReturn("1");

        String view = attendanceController.markAttendance("r1", true, redirectAttributes);

        assertThat(view).isEqualTo("redirect:/admin/events/attendance/1");
        verify(redirectAttributes).addFlashAttribute(eq("errorMessage"), contains("Error updating attendance"));
    }
}
