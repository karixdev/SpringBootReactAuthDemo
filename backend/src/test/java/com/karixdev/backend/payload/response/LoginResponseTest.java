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
public class LoginResponseTest {

    @Autowired
    JacksonTester<LoginResponse> jTester;

    @Test
    void testSerialize() throws IOException {
        User user = new User(
                1L,
                "email@email.com",
                "secret-pass",
                UserRole.ROLE_USER
        );
        String accessToken = "token";

        LoginResponse response = new LoginResponse(accessToken, user);

        var result = jTester.write(response);

        assertThat(result).hasJsonPathValue("$.access_token");
        assertThat(result).extractingJsonPathValue("$.access_token")
                .isEqualTo("token");

        assertThat(result).hasJsonPathValue("$.user");

        assertThat(result).hasJsonPathValue("$.user.email");
        assertThat(result).extractingJsonPathValue("$.user.email")
                .isEqualTo("email@email.com");

        assertThat(result).hasJsonPathValue("$.user.user_role");
        assertThat(result).extractingJsonPathValue("$.user.user_role")
                .isEqualTo("ROLE_USER");
    }

}
