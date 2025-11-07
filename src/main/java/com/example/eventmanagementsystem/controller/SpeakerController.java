package com.example.eventmanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.eventmanagementsystem.entity.Speaker;
import com.example.eventmanagementsystem.repository.SpeakerRepository;

@Controller
@RequestMapping("/admin/speakers")
public class SpeakerController {
	 @Autowired
	    private SpeakerRepository speakerRepository;

	    // ✅ Display all speakers
	    @GetMapping
	    public String viewSpeakers(Model model) {
	        model.addAttribute("speakers", speakerRepository.findAll());
	        return "admin/speakers/list"; // corresponds to your template file (admin/speakers.html)
	    }

	    // ✅ Show form to add new speaker
	    @GetMapping("/create")
	    public String showCreateForm(Model model) {
	        model.addAttribute("speaker", new Speaker());
	        return "admin/speakers/form"; // a new template you’ll create for add/edit
	    }

	    // ✅ Save new speaker
	    @PostMapping("/save")
	    public String saveSpeaker(@ModelAttribute Speaker speaker, RedirectAttributes redirectAttributes) {
	        speakerRepository.save(speaker);
	        redirectAttributes.addFlashAttribute("successMessage", "Speaker saved successfully!");
	        return "redirect:/admin/speakers";
	    }

	    // ✅ Edit existing speaker
	    @GetMapping("/edit/{id}")
	    public String editSpeaker(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
	        Speaker speaker = speakerRepository.findById(id).orElse(null);
	        if (speaker == null) {
	            redirectAttributes.addFlashAttribute("errorMessage", "Speaker not found!");
	            return "redirect:/admin/speakers";
	        }
	        model.addAttribute("speaker", speaker);
	        return "admin/speakers/form";
	    }

	    // ✅ Delete speaker
	    @GetMapping("/delete/{id}")
	    public String deleteSpeaker(@PathVariable String id, RedirectAttributes redirectAttributes) {
	        if (speakerRepository.existsById(id)) {
	            speakerRepository.deleteById(id);
	            redirectAttributes.addFlashAttribute("successMessage", "Speaker deleted successfully!");
	        } else {
	            redirectAttributes.addFlashAttribute("errorMessage", "Speaker not found!");
	        }
	        return "redirect:/admin/speakers";
	    }
}
