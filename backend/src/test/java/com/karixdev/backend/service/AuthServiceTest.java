package com.karixdev.backend.service;

import com.karixdev.backend.entity.User;
import com.karixdev.backend.entity.UserRole;
import com.karixdev.backend.exception.UserAlreadyExistsException;
import com.karixdev.backend.payload.request.RegisterUserRequest;
import com.karixdev.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @InjectMocks
    AuthService underTest;

    @Mock
    UserService userService;

    User user;

    RegisterUserRequest registerUserRequest;

    @BeforeEach
    void setUp() {
        user = new User();

        user.setId(1L);
        user.setEmail("email@email.com");
        user.setUserRole(UserRole.ROLE_USER);
        user.setPassword("secret");

        registerUserRequest = new RegisterUserRequest(
                "email@email.com",
                "super-secret-password"
        );
    }

    @Test
    void GivenRequestWithAlreadyExistingUserEmail_WhenRegisterUser_ThenThrowsUserAlreadyExistsException() {
        when(userService.create(anyString(), anyString()))
                .thenThrow(new UserAlreadyExistsException(registerUserRequest.getEmail()));

        // When & Then 1
        UserAlreadyExistsException result = assertThrows(UserAlreadyExistsException.class,
                () -> underTest.registerUser(registerUserRequest));

        // Then 2
        String expectedMessage =
                String.format("User with email %s already exists", registerUserRequest.getEmail());

        assertEquals(expectedMessage, result.getMessage());
    }

    @Test
    void GivenCorrectCredentials_WhenCreate_ThenReturnsCorrectUser() {
        // Given
        when(userService.create(anyString(), anyString()))
                .thenReturn(user);

        // When
        User result = underTest.registerUser(registerUserRequest);

        // Then
        assertEquals(user, result);
    }
}
