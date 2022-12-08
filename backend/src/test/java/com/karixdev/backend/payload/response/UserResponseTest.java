package com.karixdev.backend.payload.response;

import com.karixdev.backend.entity.User;
import com.karixdev.backend.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserResponseTest {

    @Autowired
    JacksonTester<UserResponse> jTester;

    @Test
    void testSerializeUsingAllArgsConstructor() throws IOException {
        UserResponse userResponse = new UserResponse(
                "email@email.com", UserRole.ROLE_USER);

        var result = jTester.write(userResponse);

        assertThat(result).hasJsonPathValue("$.email");
        assertThat(result).extractingJsonPathValue("$.email")
                .isEqualTo("email@email.com");

        assertThat(result).hasJsonPathValue("$.user_role");
        assertThat(result).extractingJsonPathValue("$.user_role")
                .isEqualTo("ROLE_USER");
    }

    @Test
    void testSerializeUsingUserArgConstructor() throws IOException {
        User user = new User(
                1L,
                "email@email.com",
                "secret-pass",
                UserRole.ROLE_USER
        );
        UserResponse userResponse = new UserResponse(user);

        var result = jTester.write(userResponse);

        assertThat(result).hasJsonPathValue("$.email");
        assertThat(result).extractingJsonPathValue("$.email")
                .isEqualTo("email@email.com");

        assertThat(result).hasJsonPathValue("$.user_role");
        assertThat(result).extractingJsonPathValue("$.user_role")
                .isEqualTo("ROLE_USER");
    }

}
