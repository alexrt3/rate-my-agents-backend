package com.ratemyleaser.rate_my_leaser_backend.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.ratemyleaser.rate_my_leaser_backend.config.SecurityConfig;
import com.ratemyleaser.rate_my_leaser_backend.dtos.UserResponse;
import com.ratemyleaser.rate_my_leaser_backend.exceptions.EmailAlreadyExistsException;
import com.ratemyleaser.rate_my_leaser_backend.services.AuthService;

@SpringBootTest
@Import(SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    void shouldReturnUserResponseAfterSuccessfulUserRegistration() throws Exception {
        UserResponse mockUserReponse = UserResponse.builder()
                .id(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .isAgent(false)
                .createdAt(LocalDateTime.now())
                .build();

        when(authService.registerUser(any())).thenReturn(mockUserReponse);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("1234567890"))
                .andExpect(jsonPath("$.isAgent").value(false));
    }

    @Test
    void shouldReturnBadRequestWhenUserRegistrationFails() throws Exception {
        when(authService.registerUser(any())).thenReturn(null);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenUserRegistrationRequestPayloadIsIncorrect() throws Exception {
        UserResponse mockUserReponse = UserResponse.builder()
                .id(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .isAgent(false)
                .createdAt(LocalDateTime.now())
                .build();

        when(authService.registerUser(any())).thenReturn(mockUserReponse);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(badEmailJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnConflictIfEmailAlreadyExists() throws Exception {

        when(authService.registerUser(any()))
                .thenThrow(new EmailAlreadyExistsException("john.doe@example.com"));

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    public String json = """
            {
              "firstName": "John",
              "lastName": "Doe",
              "email": "john.doe@example.com",
              "password": "securePass123",
              "userName": "johndoe",
              "phoneNumber": "1234567890",
              "isAgent": false
            }
            """;

    public String badEmailJson = """
            {
              "firstName": "John",
              "lastName": "Doe",
              "email": "bademail.com",
              "password": "securePass123",
              "userName": "johndoe",
              "phoneNumber": "1234567890",
              "isAgent": false
            }
            """;

}
