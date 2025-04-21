package com.ratemyleaser.rate_my_leaser_backend.utils;

import com.ratemyleaser.rate_my_leaser_backend.dtos.UserRegistrationRequest;
import com.ratemyleaser.rate_my_leaser_backend.dtos.UserResponse;
import com.ratemyleaser.rate_my_leaser_backend.models.User;

public class TestDataFactory {
    public static User createUser() {
        return User.builder()
                .firstName("John")
                .lastName("Smith")
                .email("test@test.com")
                .password("password123")
                .userName("testUser")
                .phoneNumber("1234567890")
                .isAgent(false)
                .build();
    }

    public static UserRegistrationRequest createUserRegistrationRequest() {
        return UserRegistrationRequest.builder()
                .firstName("John")
                .lastName("Smith")
                .email("test@test.com")
                .password("password123")
                .userName("testUser")
                .phoneNumber("1234567890")
                .isAgent(false)
                .build();
    }

    public static UserResponse createUserResponse() {
        return UserResponse.builder()
                .firstName("John")
                .lastName("Smith")
                .email("test@test.com")
                .userName("testUser")
                .phoneNumber("1234567890")
                .isAgent(false)
                .createdAt(null)
                .build();
    }

}
