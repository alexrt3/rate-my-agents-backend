package com.ratemyleaser.rate_my_leaser_backend.repositories;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.ratemyleaser.rate_my_leaser_backend.models.User;
import com.ratemyleaser.rate_my_leaser_backend.utils.TestDataFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class UserRepositoryUnitTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldReturnExistingUserInRepositoryByEmail() {
        User user = TestDataFactory.createUser();

        userRepository.save(user);
        Optional<User> retrievedUser = userRepository.findUserByEmail("test@test.com");

        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get().getFirstName()).isEqualTo(user.getFirstName());
        assertThat(retrievedUser.get().getLastName()).isEqualTo(user.getLastName());
        assertThat(retrievedUser.get().getEmail()).isEqualTo(user.getEmail());
        assertThat(retrievedUser.get().getPassword()).isEqualTo(user.getPassword());
        assertThat(retrievedUser.get().getPhoneNumber()).isEqualTo(user.getPhoneNumber());
        assertThat(retrievedUser.get().isAgent()).isEqualTo(user.isAgent());
    }

    @Test
    public void shouldReturnErrorIfFindingByEmailAndUserDoesNotExist() {

        Optional<User> retrievedUser = userRepository.findUserByEmail("test@test.com");

        assertThat(retrievedUser).isEmpty();
    }

    @Test
    public void shouldReturnExistingUserInRepositoryById() {

        User user = TestDataFactory.createUser();

        User savedUser = userRepository.save(user);
        UUID savedUserId = savedUser.getId();

        Optional<User> retrievedUser = userRepository.findById(savedUserId);

        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get().getId()).isEqualTo(savedUserId);
        assertThat(retrievedUser.get().getFirstName()).isEqualTo(user.getFirstName());
        assertThat(retrievedUser.get().getLastName()).isEqualTo(user.getLastName());
        assertThat(retrievedUser.get().getEmail()).isEqualTo(user.getEmail());
        assertThat(retrievedUser.get().getPassword()).isEqualTo(user.getPassword());
        assertThat(retrievedUser.get().getPhoneNumber()).isEqualTo(user.getPhoneNumber());
        assertThat(retrievedUser.get().isAgent()).isEqualTo(user.isAgent());
    }

    @Test
    public void shouldReturnErrorIfFindingByIdAndUserDoesNotExist() {

        Optional<User> retrievedUser = userRepository.findUserById(UUID.randomUUID());

        assertThat(retrievedUser).isEmpty();
    }

    @Test
    public void shouldReturnTrueIfUserExistsByEmail() {
        User user = TestDataFactory.createUser();

        userRepository.save(user);
        Boolean userExists = userRepository.existsByEmail("test@test.com");

        assertTrue(userExists);
    }

    @Test
    public void shouldReturnFalseIfUserDoesNotExistsByEmail() {
        User user = TestDataFactory.createUser();

        userRepository.save(user);
        Boolean userExists = userRepository.existsByEmail("test123@test.com");

        assertFalse(userExists);
    }

}
