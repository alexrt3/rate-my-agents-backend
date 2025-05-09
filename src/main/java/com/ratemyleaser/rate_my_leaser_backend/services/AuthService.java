package com.ratemyleaser.rate_my_leaser_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ratemyleaser.rate_my_leaser_backend.dtos.AuthResponse;
import com.ratemyleaser.rate_my_leaser_backend.dtos.UserRegistrationRequest;
import com.ratemyleaser.rate_my_leaser_backend.dtos.UserResponse;
import com.ratemyleaser.rate_my_leaser_backend.exceptions.EmailAlreadyExistsException;
import com.ratemyleaser.rate_my_leaser_backend.exceptions.PhoneNumberAlreadyExistsException;
import com.ratemyleaser.rate_my_leaser_backend.exceptions.UserNotFoundException;
import com.ratemyleaser.rate_my_leaser_backend.mappers.UserMapper;
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

    public UserResponse registerUser(UserRegistrationRequest userInfo) {
        if (userRepository.existsByEmail(userInfo.getEmail().toLowerCase())) {
            throw new EmailAlreadyExistsException(userInfo.getEmail());
        }

        if (userRepository.existsByPhoneNumber(userInfo.getPhoneNumber())) {
            throw new PhoneNumberAlreadyExistsException(userInfo.getPhoneNumber());
        }

        String hashedPassword = HashPassword.hash(userInfo.getPassword());

        try {
            User user = User.builder()
                    .firstName(userInfo.getFirstName().toLowerCase())
                    .lastName(userInfo.getLastName().toLowerCase())
                    .email(userInfo.getEmail().toLowerCase())
                    .password(hashedPassword)
                    .phoneNumber(userInfo.getPhoneNumber())
                    .isAgent(userInfo.isAgent())
                    .build();

            User savedUser = userRepository.save(user);
            UserResponse userDto = UserMapper.toDto(savedUser);

            log.info("Successfully registered user with email {}", user.getEmail());
            return userDto;

        } catch (Exception e) {
            log.error("Error registering user", e);
            throw e;
        }
    }

    public AuthResponse authenticateUser(String email, String password) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found."));

        if (!HashPassword.matches(user.getPassword(), password)) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtils.generateToken(email);
        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .isAgent(user.isAgent())
                .build();
    }
}
