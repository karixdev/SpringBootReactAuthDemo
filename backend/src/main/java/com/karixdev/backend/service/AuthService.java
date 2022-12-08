package com.karixdev.backend.service;

import com.karixdev.backend.entity.User;
import com.karixdev.backend.jwt.JwtHelper;
import com.karixdev.backend.payload.request.LoginRequest;
import com.karixdev.backend.payload.request.RegisterUserRequest;
import com.karixdev.backend.payload.response.LoginResponse;
import com.karixdev.backend.payload.response.UserResponse;
import com.karixdev.backend.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtHelper jwtHelper;

    public User registerUser(RegisterUserRequest payload) {
        String email = payload.getEmail();
        String password = payload.getPassword();

        return userService.create(email, password);
    }

    public LoginResponse login(LoginRequest payload) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        payload.getEmail(), payload.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String token = jwtHelper.generateToken(userPrincipal);

        return new LoginResponse(token, userPrincipal.getUser());
    }

    public UserResponse me(UserPrincipal userPrincipal) {
        return new UserResponse(userPrincipal.getUser());
    }
}
