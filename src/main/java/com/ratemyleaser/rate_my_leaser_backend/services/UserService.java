package com.ratemyleaser.rate_my_leaser_backend.services;

import org.springframework.stereotype.Service;

import com.ratemyleaser.rate_my_leaser_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

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
