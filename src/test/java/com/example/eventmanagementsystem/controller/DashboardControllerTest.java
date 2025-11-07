package com.example.eventmanagementsystem.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(DashboardController.class)
public class DashboardControllerTest {

	 @Autowired
	    private MockMvc mockMvc;

	    // 1. Test the /dashboard endpoint
	    @Test
	    @WithMockUser(username="user@example.com")  // Simulates an authenticated user
	    void testDashboard_ReturnsDashboardViewAndModelHasUserEmail() throws Exception {
	        mockMvc.perform(get("/dashboard"))
	               .andExpect(status().isOk())
	               .andExpect(view().name("dashboard"))
	               .andExpect(model().attribute("userEmail", "user@example.com"));
	    }

	    // 2. Test the /userdashboard endpoint
	    @Test
	    @WithMockUser(username="user@example.com", roles={"USER"})
	    void testUserDashboard_ReturnsUserDashboardView() throws Exception {
	        mockMvc.perform(get("/userdashboard"))
	               .andExpect(status().isOk())
	               .andExpect(view().name("userdashboard"));
	    }

	    // 3. Test the /admindashboard endpoint
	    @Test
	    @WithMockUser(username="admin@example.com", roles={"ADMIN"})
	    void testAdminDashboard_ReturnsAdminDashboardView() throws Exception {
	        mockMvc.perform(get("/admindashboard"))
	               .andExpect(status().isOk())
	               .andExpect(view().name("admindashboard"));
	    }
}
