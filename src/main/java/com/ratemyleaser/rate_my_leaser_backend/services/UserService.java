package com.ratemyleaser.rate_my_leaser_backend.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ratemyleaser.rate_my_leaser_backend.dtos.UserResponse;
import com.ratemyleaser.rate_my_leaser_backend.exceptions.UserNotFoundException;
import com.ratemyleaser.rate_my_leaser_backend.models.User;
import com.ratemyleaser.rate_my_leaser_backend.repositories.UserRepository;
import com.ratemyleaser.rate_my_leaser_backend.utilities.JwtUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final JwtUtils jwtUtils;

    public UserResponse getUserProfile(String token) {
        String userEmail = jwtUtils.extractEmail(token);
        if (userEmail == null) {
            log.error("User email not found in token");
            throw new UserNotFoundException("User email not found in token");
        }
        Optional<User> userOpt = userRepository.findUserByEmail(userEmail);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return UserResponse.builder().firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .isAgent(user.isAgent())
                    .build();
        } else {
            log.error("User with email {} not found", userEmail);
            throw new UserNotFoundException(userEmail);
        }
    }

    public Boolean doesUserEmailExist(String userEmail) {
        Boolean userEmailExists = userRepository.existsByEmail(userEmail);
        if (userEmailExists) {
            log.info("Email {} already exists", userEmail);
        } else {
            log.info("Email {} does not exist", userEmail);
        }
        return userEmailExists;
    }

    public Boolean doesUserPhoneNumberExist(String userPhoneNumber) {
        Boolean userPhoneNumberExists = userRepository.existsByPhoneNumber(userPhoneNumber);

        if (userPhoneNumberExists) {
            log.info("Phone number {} already exists", userPhoneNumber);
        } else {
            log.info("Phone number {} does not exist", userPhoneNumber);
        }
        return userPhoneNumberExists;
    }
}
