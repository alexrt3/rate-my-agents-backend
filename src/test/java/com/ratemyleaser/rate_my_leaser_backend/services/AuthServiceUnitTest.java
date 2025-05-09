package com.ratemyleaser.rate_my_leaser_backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ratemyleaser.rate_my_leaser_backend.dtos.AuthResponse;
import com.ratemyleaser.rate_my_leaser_backend.exceptions.UserNotFoundException;
import com.ratemyleaser.rate_my_leaser_backend.models.User;
import com.ratemyleaser.rate_my_leaser_backend.repositories.UserRepository;
import com.ratemyleaser.rate_my_leaser_backend.utilities.HashPassword;
import com.ratemyleaser.rate_my_leaser_backend.utilities.JwtUtils;
import com.ratemyleaser.rate_my_leaser_backend.utils.TestDataFactory;

import jakarta.transaction.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class AuthServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthService authService;

    @Test
    public void shouldReturnCorrectAuthReponseWhenUserLogsIn() {
        User user = TestDataFactory.createUser();
        String hashedPassword = HashPassword.hash(user.getPassword());

        String token = jwtUtils.generateToken(user.getEmail());

        AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .isAgent(user.isAgent())
                .build();

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(jwtUtils.generateToken(anyString())).thenReturn(token);

        AuthResponse actualAuthResponse = authService.authenticateUser(user.getEmail(), hashedPassword);

        assertEquals(authResponse.getToken(), actualAuthResponse.getToken());
        assertEquals(authResponse.getEmail(), actualAuthResponse.getEmail());
        assertEquals(authResponse.getFirstName(), actualAuthResponse.getFirstName());
        assertEquals(authResponse.getLastName(), actualAuthResponse.getLastName());
        assertEquals(authResponse.isAgent(), actualAuthResponse.isAgent());
    }

    @Test
    public void shouldThrowExceptionWhenPasswordIsInvalid() {
        User user = TestDataFactory.createUser();

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> {
            authService.authenticateUser(user.getEmail(), "wrong password");
        });
    }

    @Test
    public void shouldThrowExceptionWhenUserDoesntExist() {
        User user = TestDataFactory.createUser();

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            authService.authenticateUser(user.getEmail(), user.getPassword());
        });
    }

    @Test
    public void shouldThrowExceptionWhenEmailIsNullOrEmpty() {
        assertThrows(RuntimeException.class, () -> {
            authService.authenticateUser(null, "password");
        });

        assertThrows(RuntimeException.class, () -> {
            authService.authenticateUser("", "password");
        });
    }

    @Test
    public void shouldThrowExceptionWhenPasswordIsNullOrEmpty() {
        User user = TestDataFactory.createUser();
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> {
            authService.authenticateUser(user.getEmail(), null);
        });

        assertThrows(RuntimeException.class, () -> {
            authService.authenticateUser(user.getEmail(), "");
        });
    }
}
