package com.example.eventmanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {
	
	@GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        model.addAttribute("userEmail", authentication.getName());
        return "dashboard";
    }
	@GetMapping("/userdashboard")
    public String showUserdashboard() {
        return "userdashboard";
    }
	@GetMapping("/admindashboard")
    public String showAdmindashboard() {
        return "admindashboard";
    }
}
