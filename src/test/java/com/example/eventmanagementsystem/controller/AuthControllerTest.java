package com.example.eventmanagementsystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.eventmanagementsystem.entity.User;
import com.example.eventmanagementsystem.enums.UserRole;
import com.example.eventmanagementsystem.service.UserService;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        user.setUsername("Test User");
        user.setPassword("encodedPwd");
        user.setRole(UserRole.USER);
    }

    @Test
    void showRegistrationForm_returnsRegisterView() throws Exception {
        mockMvc.perform(get("/register"))
               .andExpect(status().isOk())
               .andExpect(view().name("register"))
               .andExpect(model().attributeExists("user"));
    }

    @Test
    void registerUser_success_redirectsToLoginRegistered() throws Exception {
    	 User mockUser = new User();
    	    mockUser.setId("1");
    	    mockUser.setEmail("john@example.com");
    	    mockUser.setPassword("12345");
    	    mockUser.setRole(UserRole.USER);

    	    // Correct mocking for non-void method
    	    when(userService.registerNewUser(any(User.class))).thenReturn(mockUser);

    	    mockMvc.perform(post("/register")
    	            .param("username", "John")
    	            .param("email", "john@example.com")
    	            .param("password", "12345")
    	            .param("role", "USER")
    	    )
    	    .andExpect(status().is3xxRedirection())
    	    .andExpect(redirectedUrl("/login?registered"));
    }

    @Test
    void registerUser_emailAlreadyExists_returnsRegisterViewWithError() throws Exception {
    	doThrow(new RuntimeException("Email already exists"))
        .when(userService).registerNewUser(any(User.class));

         mockMvc.perform(post("/register")
        .param("username", "John")
        .param("email", "john@example.com")
        .param("password", "12345")
        .param("role", "USER")  
          )
        .andExpect(status().isOk())
        .andExpect(view().name("register"))
        .andExpect(model().attributeExists("error"))
        .andExpect(model().attribute("error", "Email already exists"));
}

    @Test
    void loginPage_returnsLoginView() throws Exception {
        mockMvc.perform(get("/login"))
               .andExpect(status().isOk())
               .andExpect(view().name("login"));
    }

    @Test
    void login_success_userRole_redirectsToUserDashboard() throws Exception {
        user.setRole(UserRole.USER);
        when(userService.login("test@example.com")).thenReturn(user);
        when(passwordEncoder.matches("plainPwd", "encodedPwd")).thenReturn(true);

        mockMvc.perform(post("/login")
        		.with(csrf())
               .contentType(MediaType.APPLICATION_FORM_URLENCODED)
               .param("email", "test@example.com")
               .param("password", "plainPwd")
               .param("role", "USER"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/userdashboard"));
    }

    @Test
    void login_success_adminRole_redirectsToAdminDashboard() throws Exception {
        user.setRole(UserRole.ADMIN);
        when(userService.login("test@example.com")).thenReturn(user);
        when(passwordEncoder.matches("plainPwd", "encodedPwd")).thenReturn(true);

        mockMvc.perform(post("/login")
               .contentType(MediaType.APPLICATION_FORM_URLENCODED)
               .param("email", "test@example.com")
               .param("password", "plainPwd")
               .param("role", "ADMIN"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/admindashboard"));
    }

    @Test
    void login_invalidPassword_returnsLoginWithError() throws Exception {
        when(userService.login("test@example.com")).thenReturn(user);
        when(passwordEncoder.matches("wrongPwd", "encodedPwd")).thenReturn(false);

        mockMvc.perform(post("/login")
               .contentType(MediaType.APPLICATION_FORM_URLENCODED)
               .param("email", "test@example.com")
               .param("password", "wrongPwd")
               .param("role", "USER"))
               .andExpect(status().isOk())
               .andExpect(view().name("login"))
               .andExpect(model().attributeExists("error"));
    }

    @Test
    void login_roleMismatch_returnsLoginWithError() throws Exception {
        user.setRole(UserRole.USER);
        when(userService.login("test@example.com")).thenReturn(user);
        when(passwordEncoder.matches("plainPwd", "encodedPwd")).thenReturn(true);

        mockMvc.perform(post("/login")
               .contentType(MediaType.APPLICATION_FORM_URLENCODED)
               .param("email", "test@example.com")
               .param("password", "plainPwd")
               .param("role", "ADMIN"))
               .andExpect(status().isOk())
               .andExpect(view().name("login"))
               .andExpect(model().attributeExists("error"));
    }

    @Test
    void login_userNotFound_returnsLoginWithError() throws Exception {
        when(userService.login("missing@example.com")).thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(post("/login")
        		.with(csrf())
               .contentType(MediaType.APPLICATION_FORM_URLENCODED)
               .param("email", "missing@example.com")
               .param("password", "plainPwd")
               .param("role", "USER"))
               .andExpect(status().isOk())
               .andExpect(view().name("login"))
               .andExpect(model().attributeExists("error"));
    }
}

