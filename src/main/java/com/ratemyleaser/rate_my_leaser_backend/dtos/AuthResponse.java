package com.ratemyleaser.rate_my_leaser_backend.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String email;
    private String firstName;
    private String lastName;
    private boolean isAgent;
}