package com.revature.globetrotters.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.globetrotters.entity.UserAccount;
import com.revature.globetrotters.exception.BadRequestException;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserAccountControllerTest {
    @Mock
    private AccountService accountService;
    @InjectMocks
    private UserAccountController userAccountController;
    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userAccountController).build();
        objectMapper = new ObjectMapper();
    }

    @ParameterizedTest
    @CsvSource({
            "'john_doe', 'password'"
    })
    public void testLogin_ValidCredentials(String username, String password) throws Exception {
        UserAccount account = new UserAccount();
        account.setUsername(username);
        account.setPassword(password);

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isOk());

        verify(accountService, times(1)).authenticate(account);
    }

    @Test
    public void testLogin_InvalidCredentials() throws Exception {
        UserAccount account = new UserAccount();
        account.setUsername("invalid_user");
        account.setPassword("wrong_password");
        when(accountService.authenticate(account)).thenThrow(new BadRequestException(""));

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isBadRequest());

        verify(accountService, times(1)).authenticate(account);
    }

    @Test
    public void testRegister_ValidAccount() throws Exception {
        UserAccount account = new UserAccount();
        account.setUsername("new_user");
        account.setPassword("new_password");

        doNothing().when(accountService).register(any(UserAccount.class));
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isOk());

        verify(accountService, times(1)).register(any(UserAccount.class));
    }

    @Test
    public void testGetUser_UserExists() throws Exception {
        UserAccount account = new UserAccount();
        account.setId(1);
        account.setUsername("john_doe");

        when(accountService.getUser(1)).thenReturn(account);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john_doe"));

        verify(accountService, times(1)).getUser(1);
    }

    @Test
    public void testGetUser_UserDoesNotExist() throws Exception {
        when(accountService.getUser(999)).thenThrow(new NotFoundException("Not found"));

        mockMvc.perform(get("/users/999"))
                .andExpect(status().isNotFound());

        verify(accountService, times(1)).getUser(999);
    }

    @Test
    public void testGetFollowers() throws Exception {
        mockMvc.perform(get("/users/1/followers"))
                .andExpect(status().isOk());

        verify(accountService, times(1)).findListOfUsersFollowing(1);
    }

    @Test
    public void testGetFollowing() throws Exception {
        mockMvc.perform(get("/users/1/following"))
                .andExpect(status().isOk());

        verify(accountService, times(1)).findListOfUsersFollowed(1);
    }
}
