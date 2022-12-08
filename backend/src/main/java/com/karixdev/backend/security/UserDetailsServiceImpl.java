package com.karixdev.backend.security;

import com.karixdev.backend.entity.User;
import com.karixdev.backend.repository.UserRepository;
import com.karixdev.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByEmail(username)
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("User with provided email not found");
                });

        return new UserPrincipal(user);
    }
}
