package com.example.eventmanagementsystem.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.eventmanagementsystem.entity.User;
import com.example.eventmanagementsystem.exception.UserNotFoundException;
import com.example.eventmanagementsystem.repository.UserRepository;

public class UserServiceTest {

	@Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterNewUser_Success() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("plain123");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plain123")).thenReturn("encoded123");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User savedUser = userService.registerNewUser(user);

        assertEquals("encoded123", savedUser.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testRegisterNewUser_EmailAlreadyExists() {
        User user = new User();
        user.setEmail("exists@example.com");

        when(userRepository.findByEmail("exists@example.com"))
                .thenReturn(Optional.of(new User()));

        assertThrows(RuntimeException.class, () -> userService.registerNewUser(user));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testLogin_Success() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        User found = userService.login("test@example.com");

        assertNotNull(found);
        assertEquals("test@example.com", found.getEmail());
    }

    @Test
    void testLogin_UserNotFound() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.login("missing@example.com"));
    }
}
