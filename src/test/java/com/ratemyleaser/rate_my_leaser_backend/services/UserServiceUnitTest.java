package com.ratemyleaser.rate_my_leaser_backend.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.ratemyleaser.rate_my_leaser_backend.dtos.UserRegistrationRequest;
import com.ratemyleaser.rate_my_leaser_backend.dtos.UserResponse;
import com.ratemyleaser.rate_my_leaser_backend.exceptions.EmailAlreadyExistsException;
import com.ratemyleaser.rate_my_leaser_backend.exceptions.PhoneNumberAlreadyExistsException;
import com.ratemyleaser.rate_my_leaser_backend.mappers.UserMapper;
import com.ratemyleaser.rate_my_leaser_backend.models.User;
import com.ratemyleaser.rate_my_leaser_backend.repositories.UserRepository;
import com.ratemyleaser.rate_my_leaser_backend.utils.TestDataFactory;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void shouldReturnSuccesfullyRegisteredUser() {
        UserRegistrationRequest request = TestDataFactory.createUserRegistrationRequest();
        User user = UserMapper.toEntity(request);

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse expectedUser = userService.registerUser(request);

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

        assertThatThrownBy(() -> userService.registerUser(request))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessage("The email test@test.com is already in use.");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void shouldThrowExceptionWhenRegisteringUserIsCalledAndPhoneNumberExists() {
        UserRegistrationRequest request = TestDataFactory.createUserRegistrationRequest();

        when(userRepository.existsByPhoneNumber(anyString())).thenReturn(true);

        assertThatThrownBy(() -> userService.registerUser(request))
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

        assertThatThrownBy(() -> userService.registerUser(request)).isInstanceOf(DataAccessException.class);

        verify(userRepository).existsByEmail(user.getEmail());
        verify(userRepository).save(any(User.class));
    }

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