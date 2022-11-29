package com.karixdev.backend.service;

import com.karixdev.backend.entity.User;
import com.karixdev.backend.payload.request.RegisterUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserService userService;

    public User registerUser(RegisterUserRequest payload) {
        String email = payload.getEmail();
        String password = payload.getPassword();

        return userService.create(email, password);
    }

}
