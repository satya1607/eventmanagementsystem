package com.example.eventmanagementsystem.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

@Document(collection = "Registration")
public class Registration {

    @Id
    private String id;
    
    @DBRef
    private User user; 
    
    @DBRef
    private Event event;
    
    @NotBlank(message = "User email is required")
    private String userEmail;
    
    @NotNull(message = "Registration timestamp is required")
    @PastOrPresent(message = "Registration date must be in the present or past")
    private LocalDateTime registeredAt;
    
    private boolean attended;
    
	@PastOrPresent(message = "Attendance timestamp must be in the past or present")
    private LocalDateTime attendanceTimestamp;
    
    public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public LocalDateTime getRegisteredAt() {
		return registeredAt;
	}

	public void setRegisteredAt(LocalDateTime registeredAt) {
		this.registeredAt = registeredAt;
	}
	
    public Registration() {}

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public boolean isAttended() {
        return attended;
    }
    public void setAttended(boolean attended) {
        this.attended = attended;
    }

    public LocalDateTime getAttendanceTimestamp() {
        return attendanceTimestamp;
    }
    public void setAttendanceTimestamp(LocalDateTime attendanceTimestamp) {
        this.attendanceTimestamp = attendanceTimestamp;
    }
}