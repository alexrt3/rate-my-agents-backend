package com.ratemyleaser.rate_my_leaser_backend.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ratemyleaser.rate_my_leaser_backend.dtos.UserRegistrationRequest;
import com.ratemyleaser.rate_my_leaser_backend.dtos.UserResponse;
import com.ratemyleaser.rate_my_leaser_backend.exceptions.EmailAlreadyExistsException;
import com.ratemyleaser.rate_my_leaser_backend.exceptions.PhoneNumberAlreadyExistsException;
import com.ratemyleaser.rate_my_leaser_backend.mappers.UserMapper;
import com.ratemyleaser.rate_my_leaser_backend.models.User;
import com.ratemyleaser.rate_my_leaser_backend.repositories.UserRepository;
import com.ratemyleaser.utilities.HashPassword;

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
