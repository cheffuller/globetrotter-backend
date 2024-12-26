package com.revature.globetrotters.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.revature.globetrotters.entity.UserAccount;
import com.revature.globetrotters.service.AccountService;

public class UserAccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private UserAccountController userAccountController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoginSuccess() {
        UserAccount mockAccount = new UserAccount();
        mockAccount.setUsername("testuser");
        mockAccount.setPassword("password");

        when(accountService.authenticate(anyString(), anyString())).thenReturn(mockAccount);

        ResponseEntity<?> response = userAccountController.login(mockAccount);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockAccount, response.getBody());
    }

    @Test
    public void testLoginInvalidCredentials() {
        UserAccount mockAccount = new UserAccount();
        mockAccount.setUsername("testuser");
        mockAccount.setPassword("wrongpassword");

        when(accountService.authenticate(anyString(), anyString())).thenReturn(null);

        ResponseEntity<?> response = userAccountController.login(mockAccount);
        assertEquals(401, response.getStatusCode().value());
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Invalid username or password", response.getBody());
    }

    @Test
    public void testLoginBadRequest() {
        UserAccount mockAccount = new UserAccount();
        mockAccount.setUsername("testuser");
        mockAccount.setPassword("password");

        when(accountService.authenticate(anyString(), anyString())).thenThrow(new IllegalArgumentException("Bad request"));

        ResponseEntity<?> response = userAccountController.login(mockAccount);
        assertEquals(400, response.getStatusCode().value());
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Bad request", response.getBody());
    }

    @Test
    public void testLoginServerError() {
        UserAccount mockAccount = new UserAccount();
        mockAccount.setUsername("testuser");
        mockAccount.setPassword("password");

        when(accountService.authenticate(anyString(), anyString())).thenThrow(new RuntimeException("Server error"));

        ResponseEntity<?> response = userAccountController.login(mockAccount);
        assertEquals(500, response.getStatusCode().value());
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("An error occurred during login: Server error", response.getBody());
    }

    @Test
    public void testRegisterSuccess() {
        UserAccount mockAccount = new UserAccount();
        mockAccount.setUsername("newuser");
        mockAccount.setPassword("password");

        when(accountService.register(any(UserAccount.class))).thenReturn(mockAccount);

        ResponseEntity<?> response = userAccountController.register(mockAccount);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockAccount, response.getBody());
    }

    @Test
    public void testRegisterBadRequest() {
        UserAccount mockAccount = new UserAccount();
        mockAccount.setUsername("newuser");
        mockAccount.setPassword("password");

        when(accountService.register(any(UserAccount.class))).thenThrow(new IllegalArgumentException("Bad request"));

        ResponseEntity<?> response = userAccountController.register(mockAccount);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Bad request", response.getBody());
    }

    @Test
    public void testRegisterServerError() {
        UserAccount mockAccount = new UserAccount();
        mockAccount.setUsername("newuser");
        mockAccount.setPassword("password");

        when(accountService.register(any(UserAccount.class))).thenThrow(new RuntimeException("Server error"));

        ResponseEntity<?> response = userAccountController.register(mockAccount);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("An error occurred during registration: Server error", response.getBody());
    }
}