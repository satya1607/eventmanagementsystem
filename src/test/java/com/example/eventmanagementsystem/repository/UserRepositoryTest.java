package com.example.eventmanagementsystem.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.example.eventmanagementsystem.entity.User;

@DataMongoTest
public class UserRepositoryTest {

	 @Autowired
	    private UserRepository userRepository;

	    @Test
	    public void whenSaveAndFindByEmail_thenUserShouldBeFound() {
	        // given
	    	userRepository.deleteAll();
	        User user = new User();
	        user.setEmail("test@example.com");
	        user.setPassword("pwd");
	        // other fields…

	        userRepository.save(user);

	        // when
	        Optional<User> found = userRepository.findByEmail("test@example.com");

	        // then
	        assertThat(found).isPresent();
	        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
	    }
}
