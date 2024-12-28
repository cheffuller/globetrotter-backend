package com.revature.globetrotters.service;

import com.revature.globetrotters.consts.JwtConsts;
import com.revature.globetrotters.entity.Follow;
import com.revature.globetrotters.entity.FollowRequest;
import com.revature.globetrotters.entity.UserAccount;
import com.revature.globetrotters.entity.UserProfile;
import com.revature.globetrotters.exception.BadRequestException;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.repository.FollowRepository;
import com.revature.globetrotters.repository.FollowRequestRepository;
import com.revature.globetrotters.repository.UserAccountRepository;
import com.revature.globetrotters.repository.UserProfileRepository;
import com.revature.globetrotters.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class UserAccountServiceTest {

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
        when(userAccountRepository.findByUsername("newuser")).thenReturn(Optional.of(new UserAccount()));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> accountService.register(account));
        assertEquals("Username is already taken.", exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "'testuser', 'password'"
    })
    public void testAuthenticateSuccess(String username, String password) throws NotFoundException, BadRequestException {
        UserAccount account = createUserAccount(username, password);
        when(userAccountRepository.findByUsername(username)).thenReturn(Optional.of(account));
        String token = accountService.authenticate(username, password);
        String userNameClaim = (String) JwtUtil.extractValueFromTokenByKey(token, JwtConsts.USERNAME);
        assertEquals(username, userNameClaim);
    }

    @Test
    public void testAuthenticateNullUsername() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> accountService.authenticate(null, "password"));
        assertEquals("Username is required.", exception.getMessage());
    }

    @Test
    public void testAuthenticateEmptyUsername() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> accountService.authenticate("", "password"));
        assertEquals("Username is required.", exception.getMessage());
    }

    @Test
    public void testAuthenticateNullPassword() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> accountService.authenticate("testuser", null));
        assertEquals("Password is required.", exception.getMessage());
    }

    @Test
    public void testAuthenticateEmptyPassword() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> accountService.authenticate("testuser", ""));
        assertEquals("Password is required.", exception.getMessage());
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

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> accountService.findListOfUsersFollowing(userId)
        );
        assertEquals("User ID does not exist.", exception.getMessage());
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

        // Mock non-existence of the user
        when(userAccountRepository.existsById(userId)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> accountService.findListOfUsersFollowed(userId)
        );
        assertEquals("User ID does not exist.", exception.getMessage());
        verify(userAccountRepository, times(1)).existsById(userId);
    }


    @Test
    public void testFollowUser_ValidRequest() throws NotFoundException, BadRequestException {
        int followerId = 1;
        int followingId = 2;

        UserProfile userProfile = new UserProfile();
        userProfile.setPrivate(false);

        when(userProfileRepository.existsById(followerId)).thenReturn(true);
        when(userProfileRepository.existsById(followingId)).thenReturn(true);
        when(userProfileRepository.findById(followingId)).thenReturn(Optional.of(userProfile));
        when(followRepository.existsById(any(Follow.FollowId.class))).thenReturn(false);
        when(followRequestRepository.existsById(any(FollowRequest.FollowRequestId.class))).thenReturn(false);

        accountService.followUser(followerId, followingId);

        verify(followRepository, times(1)).save(any(Follow.class));
    }

    @Test
    public void testFollowUser_UserNotFound() {
        int followerId = 1;
        int followingId = 2;

        when(userProfileRepository.existsById(followerId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> {
            accountService.followUser(followerId, followingId);
        });
    }

    @Test
    public void testFollowUser_FollowRequestAlreadyExists() {
        int followerId = 1;
        int followingId = 2;

        when(userProfileRepository.existsById(followerId)).thenReturn(true);
        when(userProfileRepository.existsById(followingId)).thenReturn(true);
        when(followRepository.existsById(any(Follow.FollowId.class))).thenReturn(true);

        assertThrows(BadRequestException.class, () -> {
            accountService.followUser(followerId, followingId);
        });
    }

    @Test
    public void testUnfollowUser_Success() throws BadRequestException {
        int followerId = 1;
        int followingId = 2;
        Follow follow = new Follow(followerId, followingId);

        when(followRepository.existsById(follow.getId())).thenReturn(true);

        accountService.unfollowUser(followerId, followingId);

        verify(followRepository, times(1)).delete(follow);
    }

    @Test
    public void testUnfollowUser_FollowDoesNotExist() {
        int followerId = 1;
        int followingId = 2;
        Follow follow = new Follow(followerId, followingId);

        when(followRepository.existsById(follow.getId())).thenReturn(false);
        when(followRequestRepository.existsById(new FollowRequest.FollowRequestId(followerId, followingId))).thenReturn(false);

        assertThrows(BadRequestException.class, () -> {
            accountService.unfollowUser(followerId, followingId);
        });
    }

    @Test
    public void testUnfollowUser_FollowRequestExists() throws BadRequestException {
        int followerId = 1;
        int followingId = 2;
        Follow follow = new Follow(followerId, followingId);
        FollowRequest followRequest = new FollowRequest(followerId, followingId);

        when(followRepository.existsById(follow.getId())).thenThrow(new NotFoundException("User does not exist"));
        when(followRequestRepository.existsById(followRequest.getId())).thenReturn(true);

        accountService.unfollowUser(followerId, followingId);

        verify(followRequestRepository, times(1)).delete(followRequest);
    }
}
