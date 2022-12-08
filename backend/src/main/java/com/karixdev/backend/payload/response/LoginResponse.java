package com.karixdev.backend.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.karixdev.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {

    private UserResponse user;

    @JsonProperty("access_token")
    private String accessToken;

    public LoginResponse(String accessToken, User user) {
        this.accessToken = accessToken;
        this.user = new UserResponse(user);
    }
}
