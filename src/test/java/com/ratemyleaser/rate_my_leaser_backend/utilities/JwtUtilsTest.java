package com.ratemyleaser.rate_my_leaser_backend.utilities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.jsonwebtoken.ExpiredJwtException;

@SpringBootTest
public class JwtUtilsTest {

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    public void shouldGenerateToken() {
        String email = "test@email.com";
        String token = jwtUtils.generateToken(email);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    public void shouldReturnTrueIfTokenIsValid() {
        String email = "test@email.com";

        String token = jwtUtils.generateToken(email);
        boolean isValid = jwtUtils.validateToken(token);

        assertNotNull(token);
        assertTrue(isValid);
    }

    @Test
    public void shouldReturnFalseIfTokenSignatureIsInvalid() {
        String email = "test@email.com";
        String token = jwtUtils.generateToken(email);

        String[] parts = token.split("\\.");
        parts[2] = "invalidsignature";
        String tamperedToken = String.join(".", parts);

        boolean isValid = jwtUtils.validateToken(tamperedToken);

        assertNotNull(tamperedToken);
        assertFalse(isValid);
    }

    @Test
    public void shouldReturnFalseIfTokenIsNullOrEmpty() {
        assertFalse(jwtUtils.validateToken(null));
        assertFalse(jwtUtils.validateToken(""));
    }

    @Test
    public void shouldExtractEmailFromToken() {
        String email = "test@email.com";
        String token = jwtUtils.generateToken(email);

        String extractedEmail = jwtUtils.extractEmail(token);

        assertNotNull(token);
        assertEquals(email, extractedEmail);
    }

    @Test
    public void shouldReturnFalseIfTokenHeaderIsInvalid() {
        String email = "test@email.com";
        String token = jwtUtils.generateToken(email);

        String[] parts = token.split("\\.");
        parts[0] = "tamperedHeader";
        String tamperedToken = String.join(".", parts);

        boolean isValid = jwtUtils.validateToken(tamperedToken);
        assertFalse(isValid);
    }

    @Test
    public void shouldReturnFalseIfTokenIsMalformed() {
        String malformedToken = "randomtext.notajwt.token";
        boolean isValid = jwtUtils.validateToken(malformedToken);
        assertFalse(isValid);
    }

    @Test
    public void shouldReturnNullIfExtractEmailFromInvalidToken() {
        String tamperedToken = "randomtext.notajwt.token";
        String extractedEmail = jwtUtils.extractEmail(tamperedToken);
        assertNull(extractedEmail);
    }

    @Test
    public void shouldReturnFalseIfTokenHasInvalidCharacters() {
        String invalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0QGVtYWlsLmNvbSIsImlhdCI6MTYwMjM0NDM2NywiZXhwIjoxNjAyMzQ0MzcwfQ==.invalid@signature";
        boolean isValid = jwtUtils.validateToken(invalidToken);
        assertFalse(isValid);
    }

    @Test
    public void tokenShouldExpireWhenExpirationTimeIsReached() {
        String email = "test@email.com";
        long customExpiration = 100;
        String token = jwtUtils.generateTokenWithCustomExpiration(email, customExpiration);

        try {
            Thread.sleep(200); // Wait to ensure the token expires
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertThrows(ExpiredJwtException.class, () -> {
            jwtUtils.validateToken(token);
        });
    }
}
