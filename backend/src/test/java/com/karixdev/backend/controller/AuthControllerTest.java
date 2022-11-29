package com.karixdev.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karixdev.backend.entity.User;
import com.karixdev.backend.entity.UserRole;
import com.karixdev.backend.exception.UserAlreadyExistsException;
import com.karixdev.backend.payload.request.RegisterUserRequest;
import com.karixdev.backend.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = AuthController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
public class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthService authService;

    ObjectMapper objectMapper = new ObjectMapper();

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
    void GivenNotValidCredentials_WhenRegister_ThenRespondsWithBadRequestStatus() throws Exception {
        // Given
        RegisterUserRequest payload = new RegisterUserRequest("abc", "abc");
        String mapped = objectMapper.writeValueAsString(payload);

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(mapped))
                .andExpect(status().isBadRequest());
    }

    @Test
    void GivenCredentialsWithAlreadyExistingUserEmail_WhenRegister_ThenRespondsWithConflictStatus() throws Exception {
        // Given
        RegisterUserRequest payload = new RegisterUserRequest("abc@abc.pl", "super-secret-password");
        String mapped = objectMapper.writeValueAsString(payload);

        when(authService.registerUser(any(RegisterUserRequest.class)))
                .thenThrow(new UserAlreadyExistsException(payload.getEmail()));

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(mapped))
                .andExpect(status().isConflict());
    }

    @Test
    void GivenValidCredentials_WhenRegister_ThenRespondsWithCreatedStatusAndSuccessMessage() throws Exception {
        // Given
        RegisterUserRequest payload = new RegisterUserRequest("abc@abc.pl", "super-secret-password");
        String mapped = objectMapper.writeValueAsString(payload);

        when(authService.registerUser(any(RegisterUserRequest.class)))
                .thenReturn(user);

        // When & Then
        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(mapped))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("success"))
                .andReturn();
    }

}
