package com.karixdev.backend.controller;

import com.karixdev.backend.payload.request.LoginRequest;
import com.karixdev.backend.payload.request.RegisterUserRequest;
import com.karixdev.backend.payload.response.SuccessResponse;
import com.karixdev.backend.security.UserPrincipal;
import com.karixdev.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/me")
    public ResponseEntity<?> me(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(authService.me(userPrincipal));
    }

}
