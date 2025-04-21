package com.ratemyleaser.rate_my_leaser_backend.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("The email " + email + " is already in use.");
    }
}
