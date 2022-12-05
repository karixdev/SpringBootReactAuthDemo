package com.karixdev.backend.jwt;

import com.karixdev.backend.entity.User;
import com.karixdev.backend.entity.UserRole;
import com.karixdev.backend.security.UserDetailsServiceImpl;
import com.karixdev.backend.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtFilterTest {

    @InjectMocks
    JwtFilter underTest;

    @Mock
    UserDetailsServiceImpl userDetailsService;

    @Mock
    JwtHelper jwtHelper;

    @Mock
    HttpServletResponse servletResponse;

    @Mock
    FilterChain filterChain;

    @Mock
    SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void GivenRequestWithoutAuthorizationHeader_WhenDoFilterInternal_ThenAuthenticationIsNotProcessed() throws ServletException, IOException {
        // Given
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();

        // When
        underTest.doFilterInternal(servletRequest, servletResponse, filterChain);

        // Then
        verify(userDetailsService, never()).loadUserByUsername(any(String.class));
        verify(filterChain).doFilter(
                any(ServletRequest.class),
                any(ServletResponse.class));
    }

    @Test
    void GivenRequestWithInvalidToken_WhenDoFilterInternal_ThenAuthenticationIsNotProcessed() throws ServletException, IOException {
        // Given
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.addHeader("Authorization", "Bearer token");

        when(jwtHelper.isTokenValid(any(String.class)))
                .thenReturn(false);

        // When
        underTest.doFilterInternal(servletRequest, servletResponse, filterChain);

        // Then
        verify(userDetailsService, never()).loadUserByUsername(any(String.class));
        verify(filterChain).doFilter(
                any(ServletRequest.class),
                any(ServletResponse.class));
    }

    @Test
    void GivenRequestWithValidToken_WhenDoFilterInternal_ThenAuthenticationIsProcessed() throws ServletException, IOException {
        // Given
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.addHeader("Authorization", "Bearer token");

        when(jwtHelper.isTokenValid(any(String.class)))
                .thenReturn(true);

        when(jwtHelper.getEmailFromToken(any(String.class)))
                .thenReturn("email@email.com");

        UserDetails userDetails = new UserPrincipal(new User(
                1L,
                "email@email.com",
                "secret-password",
                UserRole.ROLE_USER
        ));
        when(userDetailsService.loadUserByUsername(any(String.class)))
                .thenReturn(userDetails);

        // When
        underTest.doFilterInternal(servletRequest, servletResponse, filterChain);

        // Then
        verify(userDetailsService).loadUserByUsername(any(String.class));
        verify(SecurityContextHolder.getContext()).setAuthentication(any(Authentication.class));
        verify(filterChain).doFilter(
                any(ServletRequest.class),
                any(ServletResponse.class));
    }


}
