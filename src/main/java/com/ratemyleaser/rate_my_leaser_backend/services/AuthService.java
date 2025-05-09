package com.ratemyleaser.rate_my_leaser_backend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.stereotype.Service;

import com.ratemyleaser.rate_my_leaser_backend.dtos.AuthResponse;
import com.ratemyleaser.rate_my_leaser_backend.models.User;
import com.ratemyleaser.rate_my_leaser_backend.repositories.UserRepository;
import com.ratemyleaser.rate_my_leaser_backend.utilities.HashPassword;
import com.ratemyleaser.rate_my_leaser_backend.utilities.JwtUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final JwtUtils jwtUtils;

    public AuthResponse authenticateUser(String email, String password) {
        Optional<User> userOpt = userRepository.findUserByEmail(email);

        if (userOpt.isPresent() && HashPassword.matches(userOpt.get().getPassword(), password)) {
            User user = userOpt.get();
            String token = jwtUtils.generateToken(email);
            return AuthResponse.builder()
                    .token(token)
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .isAgent(user.isAgent())
                    .build();
        } else {
            throw new RuntimeException("Invalid credentials");
        }

    }
}
