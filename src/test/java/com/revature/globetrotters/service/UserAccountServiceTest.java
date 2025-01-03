package com.revature.globetrotters.service;

import com.revature.globetrotters.entity.Follow;
import com.revature.globetrotters.entity.UserAccount;
import com.revature.globetrotters.exception.BadRequestException;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.exception.UnauthorizedException;
import com.revature.globetrotters.repository.FollowRepository;
import com.revature.globetrotters.repository.FollowRequestRepository;
import com.revature.globetrotters.repository.UserAccountRepository;
import com.revature.globetrotters.repository.UserProfileRepository;
import com.revature.globetrotters.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class UserAccountServiceTest {
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Mock
    BCryptPasswordEncoder mockPasswordEncoder;
    @Mock
    private UserAccountRepository userAccountRepository;
    @InjectMocks
    private AccountService accountService;
    @Mock
    private UserProfileRepository userProfileRepository;
    @Mock
    private FollowRepository followRepository;
    @Mock
    private FollowRequestRepository followRequestRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private UserAccount createUserAccount(String username, String password) {
        UserAccount account = new UserAccount();
        account.setUsername(username);
        account.setPassword(password);
        account.setAddress("123 Street");
        account.setCity("City");
        account.setCountry("Country");
        account.setEmail("email@example.com");
        account.setFirstName("First");
        account.setLastName("Last");
        return account;
    }

    @Test
    public void testRegisterNullAccount() {
        assertThrows(BadRequestException.class, () -> accountService.register(null));
    }

    @Test
    public void testRegisterMissingUsername() {
        UserAccount account = createUserAccount(null, "password");
        assertThrows(BadRequestException.class, () -> accountService.register(account));
    }

    @Test
    public void testRegisterMissingPassword() {
        UserAccount account = createUserAccount("newuser", null);
        assertThrows(BadRequestException.class, () -> accountService.register(account));
    }

    @Test
    public void testRegisterUsernameTaken() {
        UserAccount account = createUserAccount("newuser", "password");
        when(userAccountRepository.findByUsername("newuser")).thenReturn(Optional.of(new UserAccount()));
        assertThrows(BadRequestException.class, () -> accountService.register(account));
    }

    @Test
    public void testLoginSuccess() throws UnauthorizedException {
        String username = "username";
        String password = "password";
        UserAccount account = createUserAccount(username, password);
        UserAccount foundAccount = createUserAccount(username, password);
        foundAccount.setPassword(passwordEncoder.encode(password));

        when(userAccountRepository.findByUsername(username)).thenReturn(Optional.of(foundAccount));
        when(mockPasswordEncoder.matches(password, foundAccount.getPassword())).thenReturn(true);

        String token = accountService.login(account);
        String tokenSubject = JwtUtil.extractSubjectFromToken(token);
        assertEquals(username, tokenSubject);
    }

    @Test
    public void testLoginNullUsername() {
        assertThrows(BadRequestException.class,
                () -> accountService.login(createUserAccount(null, "password")));
    }

    @Test
    public void testLoginEmptyUsername() {
        assertThrows(BadRequestException.class,
                () -> accountService.login(createUserAccount("", "password")));
    }

    @Test
    public void testLoginNullPassword() {
        assertThrows(BadRequestException.class,
                () -> accountService.login(createUserAccount("testuser", null)));
    }

    @Test
    public void testLoginEmptyPassword() {
        assertThrows(BadRequestException.class,
                () -> accountService.login(createUserAccount("testuser", "")));
    }

    @Test
    public void testFindListOfUsersFollowingSuccess() throws NotFoundException {
        int userId = 1;
        List<Follow> followers = Arrays.asList(new Follow(2, userId), new Follow(3, userId));

        // Mock the existence of the user and the follower list retrieval
        when(userAccountRepository.existsById(userId)).thenReturn(true);
        when(followRepository.findByFollowing(userId)).thenReturn(followers);

        List<Follow> result = accountService.findListOfUsersFollowing(userId);
        assertEquals(followers, result);
        verify(userAccountRepository, times(1)).existsById(userId);
    }

    @Test
    public void testFindListOfUsersFollowingNonExistentUserId() {
        int userId = 1;

        // Mock non-existence of the user
        when(userAccountRepository.existsById(userId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> accountService.findListOfUsersFollowing(userId));
        verify(userAccountRepository, times(1)).existsById(userId);
    }

    @Test
    public void testGetFollowingSuccess() throws NotFoundException {
        int userId = 1;
        List<Follow> following = Arrays.asList(new Follow(userId, 2), new Follow(userId, 3));

        // Mock the existence of the user and the following list retrieval
        when(userAccountRepository.existsById(userId)).thenReturn(true);
        when(followRepository.findByFollower(userId)).thenReturn(following);

        List<Follow> result = accountService.findListOfUsersFollowed(userId);
        assertEquals(following, result);
        verify(userAccountRepository, times(1)).existsById(userId);
    }

    @Test
    public void testGetFollowingNonExistentUserId() {
        int userId = 1;

        when(userAccountRepository.existsById(userId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> accountService.findListOfUsersFollowed(userId));
        verify(userAccountRepository, times(1)).existsById(userId);
    }
}
