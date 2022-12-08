package com.karixdev.backend.security;

import com.karixdev.backend.entity.User;
import com.karixdev.backend.entity.UserRole;
import com.karixdev.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @InjectMocks
    UserDetailsServiceImpl underTest;

    @Mock
    UserService userService;

    @Test
    void GivenNonExistingUserEmail_WhenLoadByUsername_ThenThrowsMessageWithAppropriateMessage() {
        // Given
        String email = "abc@abc.pl";

        when(userService.findUserByEmail(any(String.class)))
                .thenReturn(Optional.empty());

        // When & Then 1
        var result = assertThrows(UsernameNotFoundException.class,
                () -> underTest.loadUserByUsername(email));

        // Then 2
        String expected = "User with provided email not found";

        assertEquals(expected, result.getMessage());
    }

    @Test
    void GivenExistingUserEmail_WhenLoadByUsername_ThenReturnsUserDetails() {
        // Given
        String email = "abc@abc.pl";

        User user = new User(
                1L,
                email,
                "secret",
                UserRole.ROLE_USER
        );

        when(userService.findUserByEmail(any(String.class)))
                .thenReturn(Optional.of(user));

        // When
        UserDetails result = underTest.loadUserByUsername(email);

        // Then
        UserDetails expected = new UserPrincipal(user);

        assertEquals(expected, result);
    }

}
