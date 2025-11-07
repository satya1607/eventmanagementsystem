package com.example.eventmanagementsystem.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;



@Document(collection = "events")
public class Event {
    @Id
    private String id;

    private String name;
    private String description;
    private LocalDateTime dateTime;
    private String venue;
    private String category;
    private List<String> speakerIds;   // references to speakers

    // Constructors, getters & setters omitted for brevity
    
    public String getName() { return name; }
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public List<String> getSpeakerIds() { return speakerIds; }
    public void setSpeakerIds(List<String> speakerIds) { this.speakerIds = speakerIds; }
}