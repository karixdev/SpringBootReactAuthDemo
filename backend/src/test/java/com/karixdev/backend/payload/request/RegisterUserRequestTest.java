package com.karixdev.backend.payload.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class RegisterUserRequestTest {

    @Autowired
    private JacksonTester<RegisterUserRequest> jTester;

    @Test
    void testSerialize() throws IOException {
        RegisterUserRequest payload = new RegisterUserRequest(
                "email@email.com",
                "secret-password"
        );

        var result = jTester.write(payload);

        assertThat(result).hasJsonPathStringValue("$.email");
        assertThat(result).extractingJsonPathValue("$.email")
                .isEqualTo("email@email.com");

        assertThat(result).hasJsonPathStringValue("$.password");
        assertThat(result).extractingJsonPathValue("$.password")
                .isEqualTo("secret-password");
    }

    @Test
    void testDeserialize() throws IOException {
        String content = """
                {
                    "email": "email@email.com",
                    "password": "secret-password"
                }
                """;

        RegisterUserRequest result = jTester.parseObject(content);

        assertThat(result.getEmail()).isEqualTo("email@email.com");
        assertThat(result.getPassword()).isEqualTo("secret-password");
    }

}
