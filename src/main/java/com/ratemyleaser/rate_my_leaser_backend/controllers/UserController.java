package com.ratemyleaser.rate_my_leaser_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ratemyleaser.rate_my_leaser_backend.dtos.UserResponse;
import com.ratemyleaser.rate_my_leaser_backend.services.UserService;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/me")
    public ResponseEntity<UserResponse> getUserProfile(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);

        UserResponse userProfileResponse = userService.getUserProfile(token);

        if (userProfileResponse == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(userProfileResponse);
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
