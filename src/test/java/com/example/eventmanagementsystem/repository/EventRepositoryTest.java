package com.example.eventmanagementsystem.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.example.eventmanagementsystem.entity.Event;

@DataMongoTest
public class EventRepositoryTest {

	@Autowired
    private EventRepository eventRepository;

    private Event e1;
    private Event e2;
    private Event e3;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();

        e1 = new Event();
        e1.setId(null);
        e1.setName("Event One");
        e1.setDescription("First event");
        e1.setVenue("Hyderabad");
        e1.setCategory("Conference");
        e1.setDateTime(LocalDateTime.of(2025, 11, 10, 10, 0));
        // assume speakerIds etc. omitted for brevity
        eventRepository.save(e1);

        e2 = new Event();
        e2.setName("Event Two");
        e2.setDescription("Second event");
        e2.setVenue("Bangalore");
        e2.setCategory("Workshop");
        e2.setDateTime(LocalDateTime.of(2025, 11, 15, 14, 0));
        eventRepository.save(e2);

        e3 = new Event();
        e3.setName("Event Three");
        e3.setDescription("Third event");
        e3.setVenue("Hyderabad");
        e3.setCategory("Workshop");
        e3.setDateTime(LocalDateTime.of(2025, 11, 20, 16, 0));
        eventRepository.save(e3);
    }

    @Test
    void whenFindByVenue_thenReturnsCorrectEvents() {
        List<Event> results = eventRepository.findByVenue("Hyderabad");

        assertThat(results)
             .hasSize(2)
             .extracting(Event::getVenue)
             .containsOnly("Hyderabad");
    }

    @Test
    void whenFindByCategory_thenReturnsCorrectEvents() {
        List<Event> results = eventRepository.findByCategory("Workshop");

        assertThat(results)
             .hasSize(2)
             .extracting(Event::getCategory)
             .containsOnly("Workshop");
    }

    @Test
    void whenFindByDateTimeBetween_thenReturnsCorrectEvents() {
        LocalDateTime start = LocalDateTime.of(2025, 11, 9, 0, 0);
        LocalDateTime end   = LocalDateTime.of(2025, 11, 16, 23, 59);
        List<Event> results = eventRepository.findByDateTimeBetween(start, end);

        assertThat(results)
             .hasSize(2)
             .extracting(Event::getName)
             .containsExactlyInAnyOrder("Event One", "Event Two");
    }

    @Test
    void whenSave_thenIdIsGenerated() {
        Event e = new Event();
        e.setName("New Event");
        e.setVenue("Chennai");
        e.setCategory("Seminar");
        e.setDateTime(LocalDateTime.of(2026, 1, 1, 9, 0));

        Event saved = eventRepository.save(e);

        assertThat(saved.getId()).isNotNull();

        Optional<Event> foundOpt = eventRepository.findById(saved.getId());
        assertThat(foundOpt).isPresent();

        Event actual = foundOpt.get();
        assertThat(actual.getId()).isEqualTo(saved.getId());
        assertThat(actual.getName()).isEqualTo("New Event");
        assertThat(actual.getVenue()).isEqualTo("Chennai");
        assertThat(actual.getCategory()).isEqualTo("Seminar");
        assertThat(actual.getDateTime()).isEqualTo(LocalDateTime.of(2026, 1, 1, 9, 0));
    }

    @Test
    void whenDeleteById_thenEventIsRemoved() {
        String idToDelete = e2.getId();
        eventRepository.deleteById(idToDelete);
        Optional<Event> deleted = eventRepository.findById(idToDelete);
        assertThat(deleted).isEmpty();
    }

    @Test
    void whenExistsById_thenReturnsTrue() {
        String existingId = e1.getId();
        boolean exists = eventRepository.existsById(existingId);
        assertThat(exists).isTrue();
    }

    @Test
    void whenNonExistingIdExistsById_thenReturnsFalse() {
        boolean exists = eventRepository.existsById("nonExistingId123");
        assertThat(exists).isFalse();
    }
}
