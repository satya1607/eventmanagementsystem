package com.example.eventmanagementsystem.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.example.eventmanagementsystem.entity.Registration;

@DataMongoTest
public class RegistrationRepositoryTest {

	@Autowired
    private RegistrationRepository registrationRepository;

    private Registration reg1;
    private Registration reg2;
    private Registration reg3;

    @BeforeEach
    void setUp() {
        registrationRepository.deleteAll();

        reg1 = new Registration();
        reg1.setUserId("user1");
        reg1.setEventId("eventA");
        reg1.setAttended(false);
        reg1.setRegisteredAt(LocalDateTime.of(2025, 11, 1, 10, 0));
        registrationRepository.save(reg1);

        reg2 = new Registration();
        reg2.setUserId("user1");
        reg2.setEventId("eventB");
        reg2.setAttended(true);
        reg2.setRegisteredAt(LocalDateTime.of(2025, 11, 2, 11, 0));
        registrationRepository.save(reg2);

        reg3 = new Registration();
        reg3.setUserId("user2");
        reg3.setEventId("eventA");
        reg3.setAttended(false);
        reg3.setRegisteredAt(LocalDateTime.of(2025, 11, 3, 12, 0));
        registrationRepository.save(reg3);
    }

    @Test
    void whenFindByUserId_thenReturnAllRegistrationsForThatUser() {
        List<Registration> results = registrationRepository.findByUserId("user1");

        assertThat(results).hasSize(2)
                           .extracting(Registration::getEventId)
                           .containsExactlyInAnyOrder("eventA", "eventB");
    }

    @Test
    void whenFindByEventId_thenReturnAllRegistrationsForThatEvent() {
        List<Registration> results = registrationRepository.findByEventId("eventA");

        assertThat(results).hasSize(2)
                           .extracting(Registration::getUserId)
                           .containsExactlyInAnyOrder("user1", "user2");
    }

    @Test
    void whenExistsByUserIdAndEventId_thenReturnTrueForMatchingPair() {
        boolean exists = registrationRepository.existsByUserIdAndEventId("user1", "eventA");
        assertThat(exists).isTrue();
    }

    @Test
    void whenExistsByUserIdAndEventId_thenReturnFalseForNonMatchingPair() {
        boolean exists = registrationRepository.existsByUserIdAndEventId("user2", "eventB");
        assertThat(exists).isFalse();
    }
}
