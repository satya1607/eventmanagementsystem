package com.example.eventmanagementsystem.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.eventmanagementsystem.entity.Event;
import com.example.eventmanagementsystem.entity.Registration;
import com.example.eventmanagementsystem.service.EventService;
import com.example.eventmanagementsystem.service.RegistrationService;

@Controller
@RequestMapping("/admin/events")
public class AttendanceController {

	private final RegistrationService registrationService;
    private final EventService eventService;

    public AttendanceController(RegistrationService registrationService,
                                EventService eventService) {
        this.registrationService = registrationService;
        this.eventService = eventService;
    }

    @GetMapping("/attendance/{id}")
    public String showAttendanceForm(@PathVariable String id, Model model) {
    	
    	System.out.println("Show attendance for eventId: " + id);
    	Optional<Event> event = eventService.findById(id);
    	  System.out.println("event:"+event);
        model.addAttribute("event", event);
        List<Registration> regs = registrationService.findByEventId(id);
        System.out.println("Found registrations count: " + (regs == null ? 0 : regs.size()));
        model.addAttribute("registrations", regs);
        return "admin/events/attendance"; // Thymeleaf template
    }

    @PostMapping("/attendance/mark")
    public String markAttendance(@RequestParam String registrationId,
                                 @RequestParam boolean attended,
                                 RedirectAttributes redirectAttributes) {
        try {
            registrationService.markAttended(registrationId, attended);
            redirectAttributes.addFlashAttribute("successMessage", "Attendance updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating attendance: " + e.getMessage());
        }
        return "redirect:/admin/events/attendance/" + registrationService.getEventIdForRegistration(registrationId);
    }
    
}
