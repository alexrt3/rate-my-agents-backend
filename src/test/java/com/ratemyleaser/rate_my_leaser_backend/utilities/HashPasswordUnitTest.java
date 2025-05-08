package com.ratemyleaser.rate_my_leaser_backend.utilities;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class HashPasswordUnitTest {
    @Test
    void shouldHashAndMatchPassword() {
        String rawPassword = "securePass123";
        String hashed = HashPassword.hash(rawPassword);

        assertNotNull(hashed);
        assertTrue(HashPassword.matches(rawPassword, hashed));
    }

}
