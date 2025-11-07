package com.example.eventmanagementsystem.controller;


import com.example.eventmanagementsystem.entity.Registration;
import com.example.eventmanagementsystem.service.RegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.security.test.context.support.WithMockUser;

@WebMvcTest(RegistrationController.class)
@AutoConfigureMockMvc(addFilters = false)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrationService registrationService;

    @BeforeEach
    void setUp() {
        Mockito.reset(registrationService);
    }

    @Test
    void testRegisterForEvent() throws Exception {
        // Arrange
    	Registration dummy = new Registration();
    	when(registrationService.registerUserForEvent("E001", "user@example.com"))
    	    .thenReturn(dummy);

        // Act & Assert
        mockMvc.perform(post("/registrations/register")
        		        .with(csrf())
                        .param("eventId", "E001")
                        .param("userEmail", "user@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("registrations/confirmation"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Registration done. Confirmation sent to email."));

        verify(registrationService, times(1))
                .registerUserForEvent("E001", "user@example.com");
    }

    
}

