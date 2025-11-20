package com.example.eventmanagementsystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.eventmanagementsystem.entity.Event;
import com.example.eventmanagementsystem.service.EventService;
import com.example.eventmanagementsystem.service.RegistrationService;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
@WebMvcTest(EventController.class)
public class EventControllerTest {

	 @Autowired
	    private MockMvc mockMvc;

	    @MockBean
	    private EventService eventService;

	    @MockBean
	    private RegistrationService registrationService;

	    private Event event1, event2;

	    @BeforeEach
	    void setUp() {
	        event1 = new Event();
	        event1.setId("1");
	        event1.setName("Tech Conference");
	        event1.setVenue("Hyderabad");
	        event1.setCategory("Technology");
	        event1.setDateTime(LocalDateTime.now());

	        event2 = new Event();
	        event2.setId("2");
	        event2.setName("Music Fest");
	        event2.setVenue("Mumbai");
	        event2.setCategory("Music");
	        event2.setDateTime(LocalDateTime.now());
	    }

	    @Test
	    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
	    void testListEvents() throws Exception {
	        List<Event> events = Arrays.asList(event1, event2);
	        when(eventService.listAll()).thenReturn(events);

	        mockMvc.perform(get("/events"))
	                .andExpect(status().isOk())
	                .andExpect(view().name("events/list"))
	                .andExpect(model().attributeExists("events"));
	    }

	    @Test
	    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
	    void testSearchEvents_ByVenue() throws Exception {
	        when(eventService.searchByVenue("Hyderabad")).thenReturn(List.of(event1));

	        mockMvc.perform(get("/events/search/filter")
	                        .param("venue", "Hyderabad"))
	                .andExpect(status().isOk())
	                .andExpect(view().name("events/search"))
	                .andExpect(model().attributeExists("events"));
	    }

	    @Test
	    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
	    void testShowCreateForm() throws Exception {
	        mockMvc.perform(get("/events/create"))
	                .andExpect(status().isOk())
	                .andExpect(view().name("events/create"))
	                .andExpect(model().attributeExists("event"));
	    }

	    @Test
	    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
	    void testDeleteEvent() throws Exception {
	        mockMvc.perform(get("/events/delete/1"))
	                .andExpect(status().is3xxRedirection())
	                .andExpect(redirectedUrl("/events"));

	        Mockito.verify(eventService).deleteEvent("1");
	    }

	    @Test
	    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
	    void testViewEvent_WithAuthentication() throws Exception {
	        Authentication authentication = Mockito.mock(Authentication.class);
	        when(authentication.getName()).thenReturn("user@example.com");
	        when(eventService.findById("1")).thenReturn(event1);
	        when(registrationService.isUserRegistered("user@example.com", "1")).thenReturn(true);

	        mockMvc.perform(get("/events/view/1").principal(authentication))
	                .andExpect(status().isOk())
	                .andExpect(view().name("events/view"))
	                .andExpect(model().attributeExists("event"))
	                .andExpect(model().attributeExists("isRegistered"));
	    }

	    @Test
	    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
	    void testRegisterForEvent_Success() throws Exception {
	        Authentication authentication = Mockito.mock(Authentication.class);
	        when(authentication.getName()).thenReturn("user@example.com");

	        mockMvc.perform(get("/events/register/1").principal(authentication))
	                .andExpect(status().is3xxRedirection())
	                .andExpect(redirectedUrl("/events/view/1"));
	    }

	    
}
