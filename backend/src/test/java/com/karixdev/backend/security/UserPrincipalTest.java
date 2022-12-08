package com.karixdev.backend.security;

import com.karixdev.backend.entity.User;
import com.karixdev.backend.entity.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserPrincipalTest {

    UserPrincipal underTest;

    User user;

    @BeforeEach
    void setUp() {
        user = new User(
                1L,
                "email@email.com",
                "secret-password",
                UserRole.ROLE_USER
        );

        underTest = new UserPrincipal(user);
    }

    @Test
    void shouldReturnCorrectUserEntity() {
        assertEquals(user, underTest.getUser());
    }

    @Test
    void shouldReturnCorrectUsername() {
        assertEquals(user.getEmail(), underTest.getUsername());
    }

    @Test
    void shouldReturnCorrectPassword() {
        assertEquals(user.getPassword(), underTest.getPassword());
    }

    @Test
    void shouldReturnCorrectAuthorities() {
        List<GrantedAuthority> expected = List.of(
                new SimpleGrantedAuthority(user.getUserRole().name()));

        assertEquals(expected, underTest.getAuthorities());
    }

    @Test
    void shouldReturnTrue() {
        assertTrue(underTest.isAccountNonExpired());
        assertTrue(underTest.isAccountNonLocked());
        assertTrue(underTest.isCredentialsNonExpired());
        assertTrue(underTest.isEnabled());
    }

}
