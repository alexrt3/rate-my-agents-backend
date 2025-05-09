package com.ratemyleaser.rate_my_leaser_backend.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;

import com.ratemyleaser.rate_my_leaser_backend.dtos.AuthResponse;
import com.ratemyleaser.rate_my_leaser_backend.dtos.UserRegistrationRequest;
import com.ratemyleaser.rate_my_leaser_backend.dtos.UserResponse;
import com.ratemyleaser.rate_my_leaser_backend.exceptions.EmailAlreadyExistsException;
import com.ratemyleaser.rate_my_leaser_backend.exceptions.PhoneNumberAlreadyExistsException;
import com.ratemyleaser.rate_my_leaser_backend.exceptions.UserNotFoundException;
import com.ratemyleaser.rate_my_leaser_backend.mappers.UserMapper;
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

        assertThat(actualAuthResponse.getToken()).isEqualTo(authResponse.getToken());
        assertThat(actualAuthResponse.getEmail()).isEqualTo(authResponse.getEmail());
        assertThat(actualAuthResponse.getFirstName()).isEqualTo(authResponse.getFirstName());
        assertThat(actualAuthResponse.getLastName()).isEqualTo(authResponse.getLastName());
        assertThat(actualAuthResponse.isAgent()).isEqualTo(authResponse.isAgent());
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

    @Test
    public void shouldReturnSuccesfullyRegisteredUser() {
        UserRegistrationRequest request = TestDataFactory.createUserRegistrationRequest();
        User user = UserMapper.toEntity(request);

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse expectedUser = authService.registerUser(request);

        verify(userRepository).existsByEmail(user.getEmail());
        verify(userRepository, times(1)).save(any(User.class));

        assertThat(expectedUser).isNotNull();
        assertThat(expectedUser.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(expectedUser.getLastName()).isEqualTo(user.getLastName());
        assertThat(expectedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(expectedUser.getPhoneNumber()).isEqualTo(user.getPhoneNumber());
        assertThat(expectedUser.isAgent()).isFalse();
    }

    @Test
    public void shouldThrowExceptionWhenRegisteringUserIsCalledAndEmailExists() {
        UserRegistrationRequest request = TestDataFactory.createUserRegistrationRequest();

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThatThrownBy(() -> authService.registerUser(request))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessage("The email test@test.com is already in use.");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void shouldThrowExceptionWhenRegisteringUserIsCalledAndPhoneNumberExists() {
        UserRegistrationRequest request = TestDataFactory.createUserRegistrationRequest();

        when(userRepository.existsByPhoneNumber(anyString())).thenReturn(true);

        assertThatThrownBy(() -> authService.registerUser(request))
                .isInstanceOf(PhoneNumberAlreadyExistsException.class)
                .hasMessage("The phonenumber 1234567890 is already in use.");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void shouldThrowDataAccessExceptionWhenRegisteringUserAndDbIsDown() {
        User user = TestDataFactory.createUser();
        UserRegistrationRequest request = TestDataFactory.createUserRegistrationRequest();

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenThrow(new DataAccessException("Simulated DB failure") {
        });

        assertThatThrownBy(() -> authService.registerUser(request)).isInstanceOf(DataAccessException.class);

        verify(userRepository).existsByEmail(user.getEmail());
        verify(userRepository).save(any(User.class));
    }
}
