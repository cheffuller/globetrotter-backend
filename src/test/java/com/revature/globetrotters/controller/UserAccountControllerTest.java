package com.revature.globetrotters.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.globetrotters.entity.Post;
import com.revature.globetrotters.entity.UserAccount;
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

import java.sql.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

    @Test
    public void testLogin_ValidCredentials() throws Exception {
        UserAccount account = new UserAccount();
        account.setUsername("john_doe");
        account.setPassword("password");

        when(accountService.authenticate("john_doe", "password")).thenReturn(account);

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john_doe"));

        verify(accountService, times(1)).authenticate("john_doe", "password");
    }

    @Test
    public void testLogin_InvalidCredentials() throws Exception {
        when(accountService.authenticate("invalid_user", "wrong_password")).thenReturn(null);

        UserAccount account = new UserAccount();
        account.setUsername("invalid_user");
        account.setPassword("wrong_password");

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));

        verify(accountService, times(1)).authenticate("invalid_user", "wrong_password");
    }

    @Test
    public void testRegister_ValidAccount() throws Exception {
        UserAccount account = new UserAccount();
        account.setUsername("new_user");
        account.setPassword("new_password");

        when(accountService.register(any(UserAccount.class))).thenReturn(account);

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("new_user"));

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

    @Test
    public void testFollowUser() throws Exception {
        int userId = 3;
        int followingId = 1;

        mockMvc.perform(post("/users/" + userId + "/following")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(followingId)))
                .andExpect(status().isOk());

        verify(accountService, times(1)).followUser(userId, followingId);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 2",
            "1, 3"
    })
    public void followUserAlreadyFollowedTest(int followerId, int followingId) throws Exception {
        mockMvc.perform(post("/users/" + followingId + "/following")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(followerId)))
                .andExpect(status().isBadRequest());

        verify(accountService, times(1)).followUser(followerId, followingId);
    }

    @Test
    public void testCreatePost() throws Exception {
        int userId = 1;
        Post post = new Post();

        post.setTravelPlanId(123);
        post.setPostedDate(new Date(System.currentTimeMillis()));

        when(accountService.createPost(userId, post)).thenReturn(post);

        mockMvc.perform(post("/users/" + userId + "/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.travelPlanId").value(123))
                .andExpect(jsonPath("$.postedDate").isNotEmpty());

        verify(accountService, times(1)).createPost(userId, post);
    }

}
