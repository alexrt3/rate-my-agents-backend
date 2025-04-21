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

import com.ratemyleaser.rate_my_leaser_backend.exceptions.EmailAlreadyExistsException;
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
        User user = TestDataFactory.createUser();

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User expectedUser = userService.registerUser(user.getFirstName(), user.getLastName(), user.getEmail(),
                user.getPassword(), user.getPhoneNumber(), user.getUserName());

        verify(userRepository).existsByEmail(user.getEmail());
        verify(userRepository, times(1)).save(any(User.class));

        assertThat(expectedUser).isNotNull();
        assertThat(expectedUser.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(expectedUser.getLastName()).isEqualTo(user.getLastName());
        assertThat(expectedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(expectedUser.getPassword()).isEqualTo(user.getPassword());
        assertThat(expectedUser.getPhoneNumber()).isEqualTo(user.getPhoneNumber());
        assertThat(expectedUser.getUserName()).isEqualTo(user.getUserName());
        assertThat(expectedUser.isAgent()).isFalse();
    }

    @Test
    public void shouldThrowExceptionWhenRegisteringUserIsCalledAndEmailExists() {
        User user = TestDataFactory.createUser();

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThatThrownBy(() -> userService.registerUser(user.getFirstName(), user.getLastName(), user.getEmail(),
                user.getPassword(), user.getPhoneNumber(), user.getUserName()))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessage("The email test@test.com is already in use.");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void shouldThrowDataAccessExceptionWhenRegisteringUserAndDbIsDown() {
        User user = TestDataFactory.createUser();

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenThrow(new DataAccessException("Simulated DB failure") {
        });

        assertThatThrownBy(() -> userService.registerUser(user.getFirstName(), user.getLastName(), user.getEmail(),
                user.getPassword(), user.getPhoneNumber(), user.getUserName())).isInstanceOf(DataAccessException.class);

        verify(userRepository).existsByEmail(user.getEmail());
        verify(userRepository).save(any(User.class));
    }
}