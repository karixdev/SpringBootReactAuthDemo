package com.karixdev.backend.payload.response;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class SuccessResponseTest {

    @Autowired
    JacksonTester<SuccessResponse> jTester;

    @Test
    void testSerialize() throws IOException {
        SuccessResponse successResponse = new SuccessResponse();

        var result = jTester.write(successResponse);

        assertThat(result).hasJsonPathValue("$.message");
        assertThat(result).extractingJsonPathValue("$.message")
                .isEqualTo("success");
    }

}
