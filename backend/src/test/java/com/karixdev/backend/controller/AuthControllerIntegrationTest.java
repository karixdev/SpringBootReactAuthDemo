package com.karixdev.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.karixdev.backend.entity.User;
import com.karixdev.backend.entity.UserRole;
import com.karixdev.backend.jwt.JwtHelper;
import com.karixdev.backend.payload.request.LoginRequest;
import com.karixdev.backend.payload.request.RegisterUserRequest;
import com.karixdev.backend.payload.response.LoginResponse;
import com.karixdev.backend.payload.response.SuccessResponse;
import com.karixdev.backend.repository.UserRepository;
import com.karixdev.backend.security.UserPrincipal;
import com.karixdev.backend.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthControllerIntegrationTest {

    @Autowired
    WebTestClient webClient;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    JwtHelper jwtHelper;

    ObjectMapper mapper = new ObjectMapper();

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void registerInvalidCredentials() throws JsonProcessingException {
        String payload = mapper.writeValueAsString(
                new RegisterUserRequest("abc", "abc")
        );

        webClient.post().uri("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void registerAlreadyExistingEmail() throws JsonProcessingException {
        String payload = mapper.writeValueAsString(
                new RegisterUserRequest("abc@abc.pl", "secret-password")
        );

        User user = new User();

        user.setEmail("abc@abc.pl");
        user.setUserRole(UserRole.ROLE_USER);
        user.setPassword("secret-password");

        userRepository.save(user);

        webClient.post().uri("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void registerValidCredentials() throws JsonProcessingException {
        String payload = mapper.writeValueAsString(
                new RegisterUserRequest("abc@abc.pl", "secret-password")
        );

        webClient.post().uri("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.message").isEqualTo("success");

        Optional<User> optionalUser = userRepository.findByEmail("abc@abc.pl");
        assertTrue(optionalUser.isPresent());

        User user = optionalUser.get();
        assertEquals("abc@abc.pl", user.getEmail());
        assertTrue(user.getId() > 0);
    }

    @Test
    void loginInvalidCredentials() throws JsonProcessingException {
        String payload = mapper.writeValueAsString(
                new LoginRequest("abc", "abc")
        );

        webClient.post().uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void loginWithNotExistingUserCredentials() throws JsonProcessingException {
        String payload = mapper.writeValueAsString(
                new LoginRequest("abc@abc.pl", "super-secret-password")
        );

        webClient.post().uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void loginWithValidCredentials() throws JsonProcessingException {
        String payload = mapper.writeValueAsString(
                new LoginRequest("abc@abc.pl", "super-secret-password")
        );

        webClient.post().uri("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .exchange();

        webClient.post().uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.access_token")
                    .isNotEmpty()
                .jsonPath("$.user.email")
                    .isEqualTo("abc@abc.pl")
                .jsonPath("$.user.user_role")
                    .isEqualTo("ROLE_USER");
    }

    @Test
    void shouldReturn401WhenCalledMeAsUnauthorizedUser() throws JsonProcessingException {
        webClient.get().uri("/api/auth/me")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void shouldReturnUserResponseWhenCalledMe() throws JsonProcessingException {
        User user = userService.create("abc@abc.pl", "secret-password");
        UserPrincipal userPrincipal = new UserPrincipal(user);
        String token = jwtHelper.generateToken(userPrincipal);

        webClient.get().uri("/api/auth/me")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.email")
                    .isEqualTo("abc@abc.pl")
                .jsonPath("$.user_role")
                    .isEqualTo("ROLE_USER");

    }
}
