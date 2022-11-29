package com.karixdev.backend.service;

import com.karixdev.backend.entity.User;
import com.karixdev.backend.entity.UserRole;
import com.karixdev.backend.exception.UserAlreadyExistsException;
import com.karixdev.backend.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService underTest;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    User user;

    @BeforeEach
    void setUp() {
        user = new User();

        user.setId(1L);
        user.setEmail("email@email.com");
        user.setUserRole(UserRole.ROLE_USER);
        user.setPassword("secret");
    }

    @Test
    void GivenAlreadyExistingUserEmail_WhenCreate_ThenThrowsUserAlreadyExistsException() {
        // Given
        String email = "email@email.com";
        String password = "super-secret-password";

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(user));

        // When & Then 1
        UserAlreadyExistsException result = assertThrows(UserAlreadyExistsException.class,
                () -> underTest.create(email, password));

        // Then 2
        String expectedMessage = String.format("User with email %s already exists", email);

        assertEquals(expectedMessage, result.getMessage());
    }

    @Test
    void GivenCorrectCredentials_WhenCreate_ThenReturnsCorrectUser() {
        // Given
        String email = "email@email.com";
        String password = "super-secret-password";

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode(any(CharSequence.class)))
                .thenReturn(password);

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        // When
        User result = underTest.create(email, password);

        // Then
        assertEquals(user, result);

    }

}
