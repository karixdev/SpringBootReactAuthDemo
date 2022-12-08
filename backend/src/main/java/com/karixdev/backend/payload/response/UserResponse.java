package com.karixdev.backend.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.karixdev.backend.entity.User;
import com.karixdev.backend.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String email;

    @JsonProperty("user_role")
    private UserRole userRole;

    public UserResponse(User user) {
        this.email = user.getEmail();
        this.userRole = user.getUserRole();
    }
}
