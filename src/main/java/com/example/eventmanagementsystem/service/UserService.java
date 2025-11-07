package com.example.eventmanagementsystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.eventmanagementsystem.entity.Event;
import com.example.eventmanagementsystem.entity.User;
import com.example.eventmanagementsystem.exception.UserNotFoundException;
import com.example.eventmanagementsystem.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerNewUser(User user) {
        // check if email already exists
        userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new RuntimeException("Email already in use");
        });
        // encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // set default role
//        user.setRole("ROLE_USER");
        return userRepository.save(user);
    }
    public User login(String email) {
		User user=userRepository.findByEmail(email)
				.orElseThrow(() ->  new UserNotFoundException("User not found in database"));
		
	 return user;
    }
}