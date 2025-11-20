package com.example.eventmanagementsystem.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.example.eventmanagementsystem.entity.Event;
import com.example.eventmanagementsystem.entity.Registration;
import com.example.eventmanagementsystem.entity.User;

@DataMongoTest
public class RegistrationRepositoryTest {

    @Autowired
    private RegistrationRepository registrationRepository;

    private Registration reg1;
    private Registration reg2;

    @BeforeEach
    void setUp() {
        registrationRepository.deleteAll();

        // Create Event
        Event event1 = new Event();
        event1.setId("E1");

        Event event2 = new Event();
        event2.setId("E2");

        // Create User
        User user1 = new User();
        user1.setId("U1");

        User user2 = new User();
        user2.setId("U2");

        // Registration 1
        reg1 = new Registration();
        reg1.setId("R1");
        reg1.setUser(user1);
        reg1.setEvent(event1);
        reg1.setAttended(true);
        reg1.setAttendanceTimestamp(LocalDateTime.now());

        // Registration 2
        reg2 = new Registration();
        reg2.setId("R2");
        reg2.setUser(user1);
        reg2.setEvent(event2);
        reg2.setAttended(false);

        registrationRepository.save(reg1);
        registrationRepository.save(reg2);
    }

    // ✔️ Test findByUserId()
    @Test
    void testFindByUserId() {
        List<Registration> list = registrationRepository.findByUserId("U1");

        assertThat(list).hasSize(2);
        assertThat(list).extracting(r -> r.getId())
                .containsExactlyInAnyOrder("R1", "R2");
    }

    // ✔️ Test findByEventId()
    @Test
    void testFindByEventId() {
        List<Registration> list = registrationRepository.findByEventId("E1");

        assertThat(list).hasSize(1);
        assertThat(list.get(0).getId()).isEqualTo("R1");
    }

    // ✔️ existsByUserIdAndEventId()
    @Test
    void testExistsByUserIdAndEventId() {
        boolean exists = registrationRepository.existsByUserIdAndEventId("U1", "E1");

        assertThat(exists).isTrue();

        boolean notExists = registrationRepository.existsByUserIdAndEventId("U2", "E1");

        assertThat(notExists).isFalse();
    }
}
