package com.example.eventmanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.eventmanagementsystem.entity.User;
import com.example.eventmanagementsystem.enums.UserRole;
import com.example.eventmanagementsystem.service.UserService;


import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

	private final UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            userService.registerNewUser(user);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
        return "redirect:/login?registered";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
    @PostMapping("/login")
	 public String login(@RequestParam String email,
	                     @RequestParam String password,
	                     @RequestParam String role,
	                     Model model,
	                     HttpSession session) {

	     try {
	         // Fetch user from DB
	         User dbUser = userService.login(email);

	         // Check password
	         if (!dbUser.getPassword().equals(password)) {
	        	 if(!passwordEncoder.matches(password,dbUser.getPassword())) {
	             model.addAttribute("error", "Invalid password!");
	             return "login";
	        	 }
	         }

	         // Check role
	         if (!dbUser.getRole().name().equals(role)) {
	             model.addAttribute("error", "Role mismatch!");
	             return "login";
	         }

	         // Save user info in session (optional)
	         session.setAttribute("loggedInUser", dbUser);

	         // Redirect based on role
	         if (dbUser.getRole() == UserRole.ADMIN) {
	             return "redirect:/admindashboard";
	         } else {
	             return "redirect:/userdashboard";
	         }

	     } catch (Exception e) {
	         model.addAttribute("error", "User not found!");
	         return "login";
	     }
	 }

}

