package com.karixdev.backend.service;

import com.karixdev.backend.entity.User;
import com.karixdev.backend.entity.UserRole;
import com.karixdev.backend.exception.UserAlreadyExistsException;
import com.karixdev.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User create(String email, String plainPassword) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException(email);
        }

        User user = new User();

        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(plainPassword));
        user.setUserRole(UserRole.ROLE_USER);

        return userRepository.save(user);
    }

}
