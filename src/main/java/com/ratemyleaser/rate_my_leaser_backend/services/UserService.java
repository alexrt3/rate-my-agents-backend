package com.ratemyleaser.rate_my_leaser_backend.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ratemyleaser.rate_my_leaser_backend.models.User;
import com.ratemyleaser.rate_my_leaser_backend.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> loginByEmail(String email, String password) {
        return userRepository.findUserByEmail(email).filter(user -> user.getPassword().equals(password));
    }

    public boolean registerUser(String firstName, String lastName, String email, String password, String phoneNumber,
            String userName) {
        if (userRepository.existsByEmail(email)) {
            return false;
        }

        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .userName(userName)
                .isAgent(false)
                .build();

        userRepository.save(user);

        return true;
    }
}
