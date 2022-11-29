package com.karixdev.backend.repository;

import com.karixdev.backend.entity.User;
import com.karixdev.backend.entity.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    UserRepository underTest;

    @Autowired
    TestEntityManager entityManager;

    User user;

    @BeforeEach
    void setUp() {
        user = new User();

        user.setEmail("email@email.com");
        user.setUserRole(UserRole.ROLE_USER);
        user.setPassword("secret");

        entityManager.persistAndFlush(user);
    }

    @AfterEach
    void tearDown() {
        entityManager.remove(user);
        entityManager.flush();
    }

    @Test
    void GivenNonExistingUserEmail_WhenFindByEmail_ThenReturnsEmptyOptional() {
        // Given
        String email = "mail@mail.com";

        // When
        Optional<User> result = underTest.findByEmail(email);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void GivenExistingUserEmail_WhenFindByEmail_ThenReturnsEmptyOptional() {
        // Given
        String email = user.getEmail();

        // When
        Optional<User> result = underTest.findByEmail(email);

        // Then
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

}
