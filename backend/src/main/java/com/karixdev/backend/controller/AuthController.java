package com.karixdev.backend.controller;

import com.karixdev.backend.payload.request.RegisterUserRequest;
import com.karixdev.backend.payload.response.SuccessResponse;
import com.karixdev.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping("/api/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserRequest payload) {
        authService.registerUser(payload);

        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.CREATED);
    }

}
