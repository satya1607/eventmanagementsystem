package com.example.eventmanagementsystem.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.eventmanagementsystem.entity.Event;
import com.example.eventmanagementsystem.service.EventService;
import com.example.eventmanagementsystem.service.RegistrationService;

@Controller
@RequestMapping("/events")
public class EventController {

	private final EventService eventService;
	private final RegistrationService registrationService;

    public EventController(EventService eventService,RegistrationService registrationService) {
        this.eventService = eventService;
        this.registrationService=registrationService;
    }

    @GetMapping
    public String listEvents(Model model) {
        List<Event> list = eventService.listAll();
        model.addAttribute("events", list);
        return "events/list";
    }

    @GetMapping("/search/filter")
    public String searchEvents(
            @RequestParam(required = false) String venue,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end,
            Model model) {

        LocalDateTime startDt;
        LocalDateTime endDt;

        if (start == null || start.isBlank()) {
            startDt = LocalDateTime.of(1900, 1, 1, 0, 0);  // a safe early date
        } else {
            startDt = LocalDateTime.parse(start);
        }

        if (end == null || end.isBlank()) {
            endDt = LocalDateTime.of(3000, 12, 31, 23, 59); // a safe future date
        } else {
            endDt = LocalDateTime.parse(end);
        }

        List<Event> result = eventService.searchByDateRange(startDt, endDt);

        if (venue != null && !venue.isBlank()) {
            result = eventService.searchByVenue(venue);
        }
        if (category != null && !category.isBlank()) {
            result = eventService.searchByCategory(category);
        }

        model.addAttribute("events", result);
        return "events/search";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("event", new Event());
        return "events/create";
    }

    @PostMapping("/create")
    public String createEvent(@ModelAttribute Event event) {
        eventService.createEvent(event);
        return "redirect:/events";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
    	 Optional<Event> event = eventService.findById(id);
        model.addAttribute("event", event);
        return "events/edit";
    }

    @PutMapping("/edit/{id}")
    public String updateEvent(@PathVariable String id, @ModelAttribute Event event) {
        eventService.updateEvent(id, event);
        return "redirect:/events";
    }

    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable String id) {
        eventService.deleteEvent(id);
        return "redirect:/events";
    }
    @GetMapping("/view/{eventId}")
    public String viewEvent(@PathVariable("eventId") String eventId,
                            Model model,
                            Authentication authentication) {
        Optional<Event> opt = eventService.findById(eventId);
        Event event = opt.orElseThrow(() -> new RuntimeException("Not found"));
        model.addAttribute("event", event);
        model.addAttribute("isRegistered",
                authentication != null && registrationService.isUserRegistered(authentication.getName(), eventId));
        return "events/view";     // thymeleaf template: src/main/resources/templates/events/view.html
    }

    @GetMapping("/register/{eventId}")
    public String registerForEvent(@PathVariable("eventId") String eventId,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {
        if (authentication == null) {
            return "redirect:/login";
        }
        String userEmail = authentication.getName();
        try {
            registrationService.registerUserForEvent(userEmail, eventId);
            redirectAttributes.addFlashAttribute("message", "Registration successful!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Registration failed: " + e.getMessage());
        }
        return "redirect:/events/view/" + eventId;
    }
}
