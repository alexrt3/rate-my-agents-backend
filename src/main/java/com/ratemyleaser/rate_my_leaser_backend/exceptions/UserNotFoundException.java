package com.ratemyleaser.rate_my_leaser_backend.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super(email + " not found");
    }
}
