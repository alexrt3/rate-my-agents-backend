package com.ratemyleaser.rate_my_leaser_backend.exceptions;

public class PhoneNumberAlreadyExistsException extends RuntimeException {
    public PhoneNumberAlreadyExistsException(String phoneNumber) {
        super("The phonenumber " + phoneNumber + " is already in use.");
    }
}
