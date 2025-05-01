package com.ratemyleaser.utilities;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashPassword {

    public static String hash(String unhashedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String hashedPassword = passwordEncoder.encode(unhashedPassword);

        return hashedPassword;
    }

    public static boolean matches(String rawPassword, String hashed) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        return passwordEncoder.matches(rawPassword, hashed);
    }
}
