package com.example.eventmanagementsystem.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.eventmanagementsystem.entity.Event;
import com.example.eventmanagementsystem.repository.EventRepository;

public class EventServiceTest {

	 @Mock
	    private EventRepository eventRepository;

	    @InjectMocks
	    private EventService eventService;

	    private Event event;

	    @BeforeEach
	    void setUp() {
	        MockitoAnnotations.openMocks(this);

	        event = new Event();
	        event.setId("1");
	        event.setName("Tech Conference");
	        event.setVenue("Hyderabad");
	        event.setCategory("Technology");
	        event.setDateTime(LocalDateTime.of(2025, 11, 10, 10, 0));
	    }

	    @Test
	    void testCreateEvent() {
	        when(eventRepository.save(any(Event.class))).thenReturn(event);

	        Event saved = eventService.createEvent(event);

	        assertThat(saved).isNotNull();
	        assertThat(saved.getName()).isEqualTo("Tech Conference");
	        verify(eventRepository, times(1)).save(event);
	    }

	    @Test
	    void testUpdateEvent_Success() {
	        // Arrange
	        when(eventRepository.findById("1")).thenReturn(Optional.of(event));
	        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

	        // Create an object with updated name
	        Event updated = new Event();
	        updated.setName("Updated Conference");
	        updated.setVenue(event.getVenue());      // optionally copy other details
	        updated.setCategory(event.getCategory());
	        updated.setDateTime(event.getDateTime());

	        // Act
	        Event result = eventService.updateEvent("1", updated);

	        // Assert
	        assertThat(result.getName()).isEqualTo("Updated Conference");
	        verify(eventRepository).findById("1");
	        verify(eventRepository).save(updated);
	    }

	    @Test
	    void testUpdateEvent_NotFound() {
	        when(eventRepository.findById("99")).thenReturn(Optional.empty());

	        Event updated = new Event();
	        updated.setName("Nonexistent Event");

	        assertThatThrownBy(() -> eventService.updateEvent("99", updated))
	                .isInstanceOf(RuntimeException.class)
	                .hasMessage("Event not found");

	        verify(eventRepository, times(1)).findById("99");
	        verify(eventRepository, never()).save(any(Event.class));
	    }

	    @Test
	    void testDeleteEvent() {
	        doNothing().when(eventRepository).deleteById("1");

	        eventService.deleteEvent("1");

	        verify(eventRepository, times(1)).deleteById("1");
	    }

	    @Test
	    void testListAll() {
	        when(eventRepository.findAll()).thenReturn(Arrays.asList(event));

	        List<Event> result = eventService.listAll();

	        assertThat(result).hasSize(1);
	        assertThat(result.get(0).getName()).isEqualTo("Tech Conference");
	        verify(eventRepository, times(1)).findAll();
	    }

	    @Test
	    void testSearchByVenue() {
	        when(eventRepository.findByVenue("Hyderabad")).thenReturn(List.of(event));

	        List<Event> result = eventService.searchByVenue("Hyderabad");

	        assertThat(result).hasSize(1);
	        assertThat(result.get(0).getVenue()).isEqualTo("Hyderabad");
	        verify(eventRepository, times(1)).findByVenue("Hyderabad");
	    }

	    @Test
	    void testSearchByCategory() {
	        when(eventRepository.findByCategory("Technology")).thenReturn(List.of(event));

	        List<Event> result = eventService.searchByCategory("Technology");

	        assertThat(result).hasSize(1);
	        assertThat(result.get(0).getCategory()).isEqualTo("Technology");
	        verify(eventRepository, times(1)).findByCategory("Technology");
	    }

	    @Test
	    void testSearchByDateRange() {
	        LocalDateTime start = LocalDateTime.of(2025, 11, 1, 0, 0);
	        LocalDateTime end = LocalDateTime.of(2025, 11, 30, 0, 0);
	        when(eventRepository.findByDateTimeBetween(start, end)).thenReturn(List.of(event));

	        List<Event> result = eventService.searchByDateRange(start, end);

	        assertThat(result).hasSize(1);
	        assertThat(result.get(0).getName()).isEqualTo("Tech Conference");
	        verify(eventRepository, times(1)).findByDateTimeBetween(start, end);
	    }

	    @Test
	    void testFindById() {
	        when(eventRepository.findById("1")).thenReturn(Optional.of(event));

	        Event result = eventService.findById("1");

	        assertThat(result.getName()).isEqualTo("Tech Conference");
	        verify(eventRepository, times(1)).findById("1");
	    }
	}

