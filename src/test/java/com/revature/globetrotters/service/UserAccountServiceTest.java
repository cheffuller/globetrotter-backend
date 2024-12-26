package com.revature.globetrotters.service;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.revature.globetrotters.entity.Follow;
import com.revature.globetrotters.entity.UserAccount;
import com.revature.globetrotters.repository.FollowRepository;
import com.revature.globetrotters.repository.UserAccountRepository;



public class UserAccountServiceTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @InjectMocks
    private AccountService accountService;

    @Mock
    private FollowRepository followRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private UserAccount createUserAccount(String username, String password) {
        UserAccount account = new UserAccount();
        account.setUsername(username);
        account.setPassword(password);
        account.setPasswordSalt("password_salt");
        account.setAddress("123 Street");
        account.setCity("City");
        account.setCountry("Country");
        account.setEmail("email@example.com");
        account.setFirstName("First");
        account.setLastName("Last");
        return account;
    }

    @Test
    public void testRegisterSuccess() {
        UserAccount account = createUserAccount("newuser", "password");
        when(userAccountRepository.save(account)).thenReturn(account);
        UserAccount result = accountService.register(account);
        assertEquals(account, result);
    }

    @Test
    public void testRegisterNullAccount() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> accountService.register(null));
        assertEquals("Account is required.", exception.getMessage());
    }

    @Test
    public void testRegisterMissingUsername() {
        UserAccount account = createUserAccount(null, "password");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> accountService.register(account));
        assertEquals("Username is required.", exception.getMessage());
    }

    @Test
    public void testRegisterMissingPassword() {
        UserAccount account = createUserAccount("newuser", null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> accountService.register(account));
        assertEquals("Password is required.", exception.getMessage());
    }

    @Test
    public void testRegisterUsernameTaken() {
        UserAccount account = createUserAccount("newuser", "password");
        when(userAccountRepository.findByUsername("newuser")).thenReturn(new UserAccount());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> accountService.register(account));
        assertEquals("Username is already taken.", exception.getMessage());
    }

    @Test
    public void testAuthenticateSuccess() {
        UserAccount account = createUserAccount("testuser", "password");
        when(userAccountRepository.findByUsername("testuser")).thenReturn(account);
        UserAccount result = accountService.authenticate("testuser", "password");
        assertEquals(account, result);
    }

    @Test
    public void testAuthenticateNullUsername() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> accountService.authenticate(null, "password"));
        assertEquals("Username and password are required.", exception.getMessage());
    }

    @Test
    public void testAuthenticateEmptyUsername() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> accountService.authenticate("", "password"));
        assertEquals("Username and password are required.", exception.getMessage());
    }

    @Test
    public void testAuthenticateNullPassword() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> accountService.authenticate("testuser", null));
        assertEquals("Username and password are required.", exception.getMessage());
    }

    @Test
    public void testAuthenticateEmptyPassword() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> accountService.authenticate("testuser", ""));
        assertEquals("Username and password are required.", exception.getMessage());
    }

    @Test
    public void testAuthenticateInvalidCredentials() {
        when(userAccountRepository.findByUsername("testuser")).thenReturn(null);
        UserAccount result = accountService.authenticate("testuser", "password");
        assertEquals(null, result);
    }

       @Test
    public void testGetFollowersSuccess() {
        int userId = 1;
        List<Follow> followers = Arrays.asList(new Follow(2, userId), new Follow(3, userId));
        when(followRepository.findByFollowing(userId)).thenReturn(followers);

        List<Follow> result = accountService.getFollowers(userId);
        assertEquals(followers, result);
    }

    @Test
    public void testGetFollowersInvalidUserId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> accountService.getFollowers(0));
        assertEquals("User ID must be greater than zero.", exception.getMessage());
    }

    @Test
    public void testGetFollowingSuccess() {
        int userId = 1;
        List<Follow> following = Arrays.asList(new Follow(userId, 2), new Follow(userId, 3));
        when(followRepository.findByFollower(userId)).thenReturn(following);

        List<Follow> result = accountService.getFollowing(userId);
        assertEquals(following, result);
    }

    @Test
    public void testGetFollowingInvalidUserId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> accountService.getFollowing(0));
        assertEquals("User ID must be greater than zero.", exception.getMessage());
    }
}
