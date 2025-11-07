package com.example.eventmanagementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.eventmanagementsystem.service.RegistrationService;

@Controller
@RequestMapping("/registrations")
public class RegistrationController {

	private final RegistrationService registrationService;
	

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;

    }

    @PostMapping("/register")
    public String registerForEvent(@RequestParam String eventId,
                                   @RequestParam String userEmail,
                                   Model model) {
        registrationService.registerUserForEvent(eventId, userEmail);
        model.addAttribute("message", "Registration done. Confirmation sent to email.");
        return "registrations/confirmation";
    }

    @GetMapping("/my/{userId}")
    public String myRegistrations(@PathVariable("userId") String userId,Model model) {
        model.addAttribute("regs", registrationService.getRegistrationsForUser(userId));
        return "registrations/list";
    }

}
