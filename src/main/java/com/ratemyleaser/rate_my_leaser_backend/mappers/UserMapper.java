package com.ratemyleaser.rate_my_leaser_backend.mappers;

import com.ratemyleaser.rate_my_leaser_backend.dtos.UserRegistrationRequest;
import com.ratemyleaser.rate_my_leaser_backend.dtos.UserResponse;
import com.ratemyleaser.rate_my_leaser_backend.models.User;

public class UserMapper {
    public static User toEntity(UserRegistrationRequest dto) {
        return User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .phoneNumber(dto.getPhoneNumber())
                .isAgent(dto.isAgent())
                .build();
    }

    public static UserResponse toDto(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .isAgent(user.isAgent())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
