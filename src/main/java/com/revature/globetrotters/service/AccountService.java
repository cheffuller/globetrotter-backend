package com.revature.globetrotters.service;

import ch.qos.logback.core.util.StringUtil;
import com.revature.globetrotters.consts.JwtConsts;
import com.revature.globetrotters.entity.Follow;
import com.revature.globetrotters.entity.FollowRequest;
import com.revature.globetrotters.entity.TravelPlan;
import com.revature.globetrotters.entity.UserAccount;
import com.revature.globetrotters.entity.UserProfile;
import com.revature.globetrotters.enums.AccountRole;
import com.revature.globetrotters.enums.FollowingStatus;
import com.revature.globetrotters.exception.BadRequestException;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.exception.UnauthorizedException;
import com.revature.globetrotters.repository.FollowRepository;
import com.revature.globetrotters.repository.FollowRequestRepository;
import com.revature.globetrotters.repository.PostRepository;
import com.revature.globetrotters.repository.TravelPlanRepository;
import com.revature.globetrotters.repository.UserAccountRepository;
import com.revature.globetrotters.repository.UserProfileRepository;
import com.revature.globetrotters.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class AccountService {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private FollowRequestRepository followRequestRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private TravelPlanRepository planRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String login(UserAccount account) throws UnauthorizedException {
        if (!account.isPasswordValid() || !account.isUsernameValid()) {
            throw new UnauthorizedException("Username and password are required.");
        }

        UserAccount foundAccount = userAccountRepository.findByUsername(account.getUsername())
                .orElseThrow(() -> new UnauthorizedException(String.format(
                        "User with username %s not found.",
                        account.getUsername())
                ));

        if (!passwordEncoder.matches(account.getPassword(), foundAccount.getPassword())) {
            throw new UnauthorizedException("Invalid login credentials." +
                    passwordEncoder.matches(account.getPassword(), foundAccount.getPassword()) +
                    ".\nPassword: " + account.getPassword() +
                    ".\nFound password hash: " + foundAccount.getPassword());
        }

        return JwtUtil.generateTokenFromUserName(account.getUsername(), Map.of(
                JwtConsts.ACCOUNT_ROLE, AccountRole.Customer.getRole(),
                JwtConsts.ACCOUNT_ID, foundAccount.getId().toString()
        ));
    }

    public void register(UserAccount account) throws BadRequestException {
        if (account == null ||
                !account.isUsernameValid() ||
                userAccountRepository.findByUsername(account.getUsername()).isPresent() ||
                StringUtil.isNullOrEmpty(account.getEmail()) ||
                userAccountRepository.findByUsername(account.getEmail()).isPresent() ||
                !account.isPasswordValid() ||
                StringUtil.isNullOrEmpty(account.getAddress()) ||
                StringUtil.isNullOrEmpty(account.getCity()) ||
                StringUtil.isNullOrEmpty(account.getCountry()) ||
                StringUtil.isNullOrEmpty(account.getFirstName()) ||
                StringUtil.isNullOrEmpty(account.getLastName())
        ) {
            throw new BadRequestException("Invalid details for account creation.");
        }

        account.setPassword(passwordEncoder.encode(account.getPassword()));
        int userId = userAccountRepository.save(account).getId();
        userProfileRepository.save(new UserProfile(userId, "", account.getUsername(), true));
    }

    public UserAccount getUser(int userId) throws NotFoundException {
        return userAccountRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User ID does not exist."));
    }

    public List<Follow> findListOfUsersFollowing(int userId) throws NotFoundException {
        if (!userAccountRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with ID %d does not exist.", userId));
        }
        return followRepository.findByFollowing(userId);
    }

    public List<Follow> findListOfUsersFollowed(int userId) throws NotFoundException {
        if (!userAccountRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with ID %d does not exist.", userId));
        }
        return followRepository.findByFollower(userId);
    }


    public void followUser(String username) throws NotFoundException, BadRequestException {
        UserAccount account = userAccountRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException("User does not exist.")
        );

        int followingId = account.getId();
        int followerId = tokenService.getUserAccountId();

        if (!userAccountRepository.existsById(followerId)) {
            throw new NotFoundException(String.format("User with ID %d does not exist.", followerId));
        }

        if (!userAccountRepository.existsById(followingId)) {
            throw new NotFoundException(String.format("User with ID %d does not exist.", followerId));
        }

        if (followRepository.existsById(new Follow.FollowId(followerId, followingId)) ||
                followRequestRepository.existsById(new FollowRequest.FollowRequestId(followerId, followingId))) {
            throw new BadRequestException(String.format("User with ID %d is already following or requested to " +
                    "follow user with id %d.", followingId, followingId));
        }

        UserProfile accountToFollow = userProfileRepository.findById(followingId).get();
        if (accountToFollow.isPrivate()) {
            followRequestRepository.save(new FollowRequest(followerId, followingId));
        } else {
            followRepository.save(new Follow(followerId, followingId));
        }
    }

    public void unfollowUser(String username) throws BadRequestException, NotFoundException {
        UserAccount account = userAccountRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException("User does not exist.")
        );

        int followingId = account.getId();
        int followerId = tokenService.getUserAccountId();

        Follow followToDelete = new Follow(followerId, followingId);
        if (followRepository.existsById(followToDelete.getId())) {
            followRepository.delete(followToDelete);
            return;
        }

        FollowRequest followRequestToDelete = new FollowRequest(followerId, followingId);
        if (followRequestRepository.existsById(followRequestToDelete.getId())) {
            followRequestRepository.delete(followRequestToDelete);
            return;
        }

        throw new BadRequestException(String.format(
                "User with ID %d is not following or has not requested to follow user with ID %d.",
                followerId, followingId));
    }

    public FollowingStatus findFollowingStatus(String username) throws NotFoundException {
        UserAccount account = userAccountRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException("User does not exist.")
        );

        int followingId = account.getId();
        int followerId = tokenService.getUserAccountId();

        if (followRepository.existsById(new Follow.FollowId(followerId, followingId))) {
            return FollowingStatus.Following;
        }

        if (followRequestRepository.existsById(new FollowRequest.FollowRequestId(followerId, followingId))) {
            return FollowingStatus.FollowRequested;
        }

        return FollowingStatus.NotFollowing;
    }

    public List<TravelPlan> getPlans(int userId) throws NotFoundException {
        if (!userAccountRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with ID %d does not exist.", userId));
        }
        return planRepository.getTravelPlansByAccountId(userId);
    }
}
