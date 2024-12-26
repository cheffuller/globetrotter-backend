package com.revature.globetrotters.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.revature.globetrotters.entity.Follow;
import com.revature.globetrotters.entity.TravelPlan;
import com.revature.globetrotters.entity.UserAccount;
import com.revature.globetrotters.exception.BadRequestException;
import com.revature.globetrotters.exception.NotFoundException;
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

        when(accountService.authenticate("testuser", "password")).thenReturn(mockAccount);

        ResponseEntity<?> response = userAccountController.login(mockAccount);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockAccount, response.getBody());
    }

    @Test
    public void testLoginInvalidCredentials() {
        UserAccount mockAccount = new UserAccount();
        mockAccount.setUsername("testuser");
        mockAccount.setPassword("wrongpassword");

        when(accountService.authenticate("testuser", "wrongpassword")).thenReturn(null);

        ResponseEntity<?> response = userAccountController.login(mockAccount);
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Invalid username or password", response.getBody());
    }

    @Test
    public void testLoginBadRequest() {
        UserAccount mockAccount = new UserAccount();
        mockAccount.setUsername("testuser");
        mockAccount.setPassword("password");

        when(accountService.authenticate("testuser", "password")).thenThrow(new IllegalArgumentException("Bad request"));

        ResponseEntity<?> response = userAccountController.login(mockAccount);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Bad request", response.getBody());
    }

    @Test
    public void testLoginServerError() {
        UserAccount mockAccount = new UserAccount();
        mockAccount.setUsername("testuser");
        mockAccount.setPassword("password");

        when(accountService.authenticate("testuser", "password")).thenThrow(new RuntimeException("Server error"));

        ResponseEntity<?> response = userAccountController.login(mockAccount);
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("An error occurred during login: Server error", response.getBody());
    }

    @Test
    public void testRegisterSuccess() {
        UserAccount mockAccount = new UserAccount();
        mockAccount.setUsername("newuser");
        mockAccount.setPassword("password");

        when(accountService.register(mockAccount)).thenReturn(mockAccount);

        ResponseEntity<?> response = userAccountController.register(mockAccount);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockAccount, response.getBody());
    }

    @Test
    public void testRegisterBadRequest() {
        UserAccount mockAccount = new UserAccount();
        mockAccount.setUsername("newuser");
        mockAccount.setPassword("password");

        when(accountService.register(mockAccount)).thenThrow(new IllegalArgumentException("Bad request"));

        ResponseEntity<?> response = userAccountController.register(mockAccount);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Bad request", response.getBody());
    }

    @Test
    public void testRegisterServerError() {
        UserAccount mockAccount = new UserAccount();
        mockAccount.setUsername("newuser");
        mockAccount.setPassword("password");

        when(accountService.register(mockAccount)).thenThrow(new RuntimeException("Server error"));

        ResponseEntity<?> response = userAccountController.register(mockAccount);
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("An error occurred during registration: Server error", response.getBody());
    }

    @Test
    public void testGetUserSuccess() {
        UserAccount mockAccount = new UserAccount();
        mockAccount.setId(1);
        mockAccount.setUsername("testuser");

        when(accountService.getUser(1)).thenReturn(Optional.of(mockAccount));

        ResponseEntity<?> response = userAccountController.getUser(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Optional.of(mockAccount), response.getBody());
    }
    @Test
    public void testGetUserNotFound() {
        when(accountService.getUser(1)).thenReturn(Optional.empty());

        ResponseEntity<?> response = userAccountController.getUser(1);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User not found", response.getBody());
    }

    @Test
    public void testGetUserBadRequest() {
        when(accountService.getUser(1)).thenThrow(new IllegalArgumentException("Bad request"));

        ResponseEntity<?> response = userAccountController.getUser(1);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Bad request", response.getBody());
    }

    @Test
    public void testGetUserServerError() {
        when(accountService.getUser(1)).thenThrow(new RuntimeException("Server error"));

        ResponseEntity<?> response = userAccountController.getUser(1);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("An error occurred while retrieving user: Server error", response.getBody());
    }

    @Test
    public void testGetFollowersSuccess() {
        List<Follow> mockFollowers = new ArrayList<>();
        Follow follower1 = new Follow();
        Follow.FollowId followId = new Follow.FollowId();
        //followId.setId(2);
        follower1.setId(followId);
        //follower1.setUsername("follower1");
        mockFollowers.add(follower1);

        when(accountService.getFollowers(1)).thenReturn(mockFollowers);

        ResponseEntity<?> response = userAccountController.getFollowers(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockFollowers, response.getBody());
    }

    @Test
    public void testGetFollowersBadRequest() {
        when(accountService.getFollowers(1)).thenThrow(new IllegalArgumentException("Bad request"));

        ResponseEntity<?> response = userAccountController.getFollowers(1);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Bad request", response.getBody());
    }

    @Test
    public void testGetFollowersServerError() {
        when(accountService.getFollowers(1)).thenThrow(new RuntimeException("Server error"));

        ResponseEntity<?> response = userAccountController.getFollowers(1);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("An error occurred while retrieving followers: Server error", response.getBody());
    }
    @Test
    public void testGetFollowingSuccess() {
        List<Follow> mockFollowing = new ArrayList<>();
        Follow following1 = new Follow();
        Follow.FollowId followId = new Follow.FollowId();
        //followId.setId(2);
        following1.setId(followId);
        mockFollowing.add(following1);

        when(accountService.getFollowing(1)).thenReturn(mockFollowing);

        ResponseEntity<?> response = userAccountController.getFollowing(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockFollowing, response.getBody());
    }

    @Test
    public void testGetFollowingBadRequest() {
        when(accountService.getFollowing(1)).thenThrow(new IllegalArgumentException("Bad request"));

        ResponseEntity<?> response = userAccountController.getFollowing(1);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Bad request", response.getBody());
    }

    @Test
    public void testGetFollowingServerError() {
        when(accountService.getFollowing(1)).thenThrow(new RuntimeException("Server error"));

        ResponseEntity<?> response = userAccountController.getFollowing(1);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("An error occurred while retrieving following: Server error", response.getBody());
    }

    @Test
    public void testFollowSuccess() throws NotFoundException, BadRequestException {
        doNothing().when(accountService).followUser(1, 2);

        ResponseEntity<?> response = userAccountController.follow(1, 2);

        assertEquals(200, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    public void testFollowBadRequest() throws NotFoundException, BadRequestException {
        doThrow(new IllegalArgumentException("Bad request")).when(accountService).followUser(1, 2);

        ResponseEntity<?> response = userAccountController.follow(1, 2);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Bad request", response.getBody());
    }

    @Test
    public void testFollowServerError() throws NotFoundException, BadRequestException {
        doThrow(new RuntimeException("Server error")).when(accountService).followUser(1, 2);

        ResponseEntity<?> response = userAccountController.follow(1, 2);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("An error occurred while following user: Server error", response.getBody());
    }
@Test
public void testGetPlansSuccess() {
    List<TravelPlan> mockPlans = new ArrayList<>();
    TravelPlan plan1 = new TravelPlan();
    //plan1.setName("Plan 1");
    TravelPlan plan2 = new TravelPlan();
    //plan2.setName("Plan 2");
    mockPlans.add(plan1);
    mockPlans.add(plan2);

    when(accountService.getPlans(1)).thenReturn(mockPlans);

    ResponseEntity<?> response = userAccountController.getPlans(1);

    assertEquals(200, response.getStatusCodeValue());
    assertEquals(mockPlans, response.getBody());
}

@Test
public void testGetPlansBadRequest() {
    when(accountService.getPlans(1)).thenThrow(new IllegalArgumentException("Bad request"));

    ResponseEntity<?> response = userAccountController.getPlans(1);

    assertEquals(400, response.getStatusCodeValue());
    assertEquals("Bad request", response.getBody());
}

@Test
public void testGetPlansServerError() {
    when(accountService.getPlans(1)).thenThrow(new RuntimeException("Server error"));

    ResponseEntity<?> response = userAccountController.getPlans(1);

    assertEquals(500, response.getStatusCodeValue());
        assertEquals("An error occurred while retrieving plans: Server error", response.getBody());
    }
}
