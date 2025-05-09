package com.ratemyleaser.rate_my_leaser_backend.utilities;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashPassword {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String hash(String unhashedPassword) {
        return passwordEncoder.encode(unhashedPassword);
    }

    public static boolean matches(String rawPassword, String hashed) {
        return passwordEncoder.matches(rawPassword, hashed);
    }
}
