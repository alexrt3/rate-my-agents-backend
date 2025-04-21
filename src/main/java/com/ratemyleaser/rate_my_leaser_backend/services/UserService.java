package com.ratemyleaser.rate_my_leaser_backend.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ratemyleaser.rate_my_leaser_backend.exceptions.EmailAlreadyExistsException;
import com.ratemyleaser.rate_my_leaser_backend.models.User;
import com.ratemyleaser.rate_my_leaser_backend.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> loginByEmail(String email, String password) {
        return userRepository.findUserByEmail(email).filter(user -> user.getPassword().equals(password));
    }

    public User registerUser(String firstName, String lastName, String email, String password, String phoneNumber,
            String userName) {

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        try {
            User user = User.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .password(password)
                    .phoneNumber(phoneNumber)
                    .userName(userName)
                    .isAgent(false)
                    .build();

            User savedUser = userRepository.save(user);

            log.info("Successfully registered user with email {}", email);
            return savedUser;

        } catch (Exception e) {
            log.error("Error registering user", e);
            throw e;
        }
    }
}
