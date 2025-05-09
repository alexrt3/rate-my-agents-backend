package com.ratemyleaser.rate_my_leaser_backend.controllers;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import com.ratemyleaser.rate_my_leaser_backend.config.SecurityConfig;
import com.ratemyleaser.rate_my_leaser_backend.services.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerUnitTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private UserService userService;

        @Test
        void shouldReturnTrueIfUserEmailExists() throws Exception {
                boolean mockUserReponse = true;

                when(userService.doesUserEmailExist(any())).thenReturn(mockUserReponse);

                mockMvc.perform(get("/user/email")
                                .param("userEmail", "john.doe@example.com"))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(content().string("true"));
        }

        @Test
        void shouldReturnFalseIfUserEmailDoesNotExists() throws Exception {
                boolean mockUserReponse = false;

                when(userService.doesUserEmailExist(any())).thenReturn(mockUserReponse);

                mockMvc.perform(get("/user/email")
                                .param("userEmail", "john.doe@example.com"))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(content().string("false"));
        }

        @Test
        void shouldReturnTrueIfUserPhoneNumberExists() throws Exception {
                boolean mockUserReponse = true;

                when(userService.doesUserPhoneNumberExist(any())).thenReturn(mockUserReponse);

                mockMvc.perform(get("/user/phonenumber")
                                .param("userPhoneNumber", "1234567890"))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(content().string("true"));
        }

        @Test
        void shouldReturnFalseIfUserPhoneNumberDoesNotExists() throws Exception {
                boolean mockUserReponse = false;

                when(userService.doesUserPhoneNumberExist(any())).thenReturn(mockUserReponse);

                mockMvc.perform(get("/user/phonenumber")
                                .param("userPhoneNumber", "1234567890"))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(content().string("false"));
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
