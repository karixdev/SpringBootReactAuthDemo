package com.karixdev.backend.jwt;

import com.karixdev.backend.entity.User;
import com.karixdev.backend.repository.UserRepository;
import com.karixdev.backend.security.UserPrincipal;
import com.karixdev.backend.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class JwtIntegrationTest {

    @Autowired
    WebTestClient webClient;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    JwtHelper jwtHelper;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void shouldAuthorizeUser() {
        User user = userService.create("abc@abc.pl", "secret-password");
        UserPrincipal userPrincipal = new UserPrincipal(user);

        String token = jwtHelper.generateToken(userPrincipal);

        webClient.get().uri("/api/auth/me")
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

}
