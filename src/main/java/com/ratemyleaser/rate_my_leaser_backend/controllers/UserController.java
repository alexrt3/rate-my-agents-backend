package com.ratemyleaser.rate_my_leaser_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ratemyleaser.rate_my_leaser_backend.dtos.UserRegistrationRequest;
import com.ratemyleaser.rate_my_leaser_backend.dtos.UserResponse;
import com.ratemyleaser.rate_my_leaser_backend.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegistrationRequest userRegistrationInfo) {
        UserResponse registeredUserResponse = userService.registerUser(userRegistrationInfo);

        if (registeredUserResponse == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(registeredUserResponse);
    }

    @GetMapping(path = "/email")
    public ResponseEntity<Boolean> doesUserEmailExists(@RequestParam String userEmail) {
        Boolean userEmailExistsResponse = userService.doesUserEmailExist(userEmail);

        if (userEmailExistsResponse == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(userEmailExistsResponse);
    }

    @GetMapping(path = "/phonenumber")
    public ResponseEntity<Boolean> doesUserPhoneNumberExists(@RequestParam String userPhoneNumber) {
        Boolean userPhoneNumberExistsResponse = userService.doesUserPhoneNumberExist(userPhoneNumber);

        if (userPhoneNumberExistsResponse == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(userPhoneNumberExistsResponse);
    }
}
