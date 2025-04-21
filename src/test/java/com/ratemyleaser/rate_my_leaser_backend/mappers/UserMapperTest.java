package com.ratemyleaser.rate_my_leaser_backend.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import com.ratemyleaser.rate_my_leaser_backend.dtos.UserRegistrationRequest;
import com.ratemyleaser.rate_my_leaser_backend.models.User;
import com.ratemyleaser.rate_my_leaser_backend.utils.TestDataFactory;
import com.ratemyleaser.rate_my_leaser_backend.dtos.UserResponse;

public class UserMapperTest {

    @Test
    public void shouldMapFromUserRegistrationRequestToUserEntity() {
        UserRegistrationRequest dto = TestDataFactory.createUserRegistrationRequest();
        User user = UserMapper.toEntity(dto);

        assertThat(user).isNotNull();
        assertThat(user.getFirstName()).isEqualTo(dto.getFirstName());
        assertThat(user.getLastName()).isEqualTo(dto.getLastName());
        assertThat(user.getEmail()).isEqualTo(dto.getEmail());
        assertThat(user.getPassword()).isEqualTo(dto.getPassword());
        assertThat(user.getPhoneNumber()).isEqualTo(dto.getPhoneNumber());
        assertThat(user.getUserName()).isEqualTo(dto.getUserName());
        assertThat(user.isAgent()).isEqualTo(dto.isAgent());
    }

    @Test
    public void shouldMapFromUserEntityToUserRegistrationRequest() {
        User user = TestDataFactory.createUser();
        UUID testId = UUID.randomUUID();
        user.setId(testId);
        UserResponse dto = UserMapper.toDto(user);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(dto.getId());
        assertThat(user.getFirstName()).isEqualTo(dto.getFirstName());
        assertThat(user.getLastName()).isEqualTo(dto.getLastName());
        assertThat(user.getEmail()).isEqualTo(dto.getEmail());
        assertThat(user.getPhoneNumber()).isEqualTo(dto.getPhoneNumber());
        assertThat(user.getUserName()).isEqualTo(dto.getUserName());
        assertThat(user.isAgent()).isEqualTo(dto.isAgent());
        assertThat(user.getCreatedAt()).isEqualTo(dto.getCreatedAt());
    }

}
