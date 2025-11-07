package com.example.eventmanagementsystem.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "registrations")
public class Registration {

    @Id
    private String id;

    private String userId;      // reference to User document ID
    private String eventId;     // reference to Event document ID

    private String userEmail;   // for quick display without extra query
    private LocalDateTime registeredAt;
    
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
	private boolean attended;
    private LocalDateTime attendanceTimestamp;

    // Constructors, getters + setters

    public Registration() {}

    public Registration(String userId, String eventId) {
        this.userId = userId;
        this.eventId = eventId;
        this.attended = false;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventId() {
        return eventId;
    }
    public void setEventId(String eventId) {
        this.eventId = eventId;
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