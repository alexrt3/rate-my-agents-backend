package com.ratemyleaser.rate_my_leaser_backend.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import com.ratemyleaser.rate_my_leaser_backend.repositories.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void shouldReturnFalseIfUserEmailDoesNotExists() {
        String testEmail = "test@test.com";
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        boolean result = userService.doesUserEmailExist(testEmail);

        verify(userRepository).existsByEmail(testEmail);
        assertThat(result).isFalse();
    }

    @Test
    public void shouldReturnTrueIfUserEmailExists() {
        String testEmail = "test@test.com";
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        boolean result = userService.doesUserEmailExist(testEmail);

        verify(userRepository).existsByEmail(testEmail);
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnTrueIfUserPhoneNumberExists() {
        String testPhoneNumber = "1234567890";
        when(userRepository.existsByPhoneNumber(anyString())).thenReturn(true);
        boolean result = userService.doesUserPhoneNumberExist(testPhoneNumber);

        verify(userRepository).existsByPhoneNumber(testPhoneNumber);
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseIfUserPhoneNumberDoesNotExists() {
        String testPhoneNumber = "1234567890";
        when(userRepository.existsByPhoneNumber(anyString())).thenReturn(false);
        boolean result = userService.doesUserPhoneNumberExist(testPhoneNumber);

        verify(userRepository).existsByPhoneNumber(testPhoneNumber);
        assertThat(result).isFalse();
    }
}