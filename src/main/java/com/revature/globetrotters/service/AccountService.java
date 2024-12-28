package com.revature.globetrotters.service;

import ch.qos.logback.core.util.StringUtil;
import com.revature.globetrotters.entity.Follow;
import com.revature.globetrotters.entity.FollowRequest;
import com.revature.globetrotters.entity.Post;
import com.revature.globetrotters.entity.TravelPlan;
import com.revature.globetrotters.entity.UserAccount;
import com.revature.globetrotters.entity.UserProfile;
import com.revature.globetrotters.exception.BadRequestException;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.repository.FollowRepository;
import com.revature.globetrotters.repository.FollowRequestRepository;
import com.revature.globetrotters.repository.PostRepository;
import com.revature.globetrotters.repository.TravelPlanRepository;
import com.revature.globetrotters.repository.UserAccountRepository;
import com.revature.globetrotters.repository.UserProfileRepository;
import com.revature.globetrotters.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;


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
    private TravelPlanRepository planRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    public String authenticate(String username, String password) throws NotFoundException, BadRequestException {
        if (StringUtil.isNullOrEmpty(username.trim()) ||
                StringUtil.isNullOrEmpty(password.trim())) {
            throw new IllegalArgumentException("Username and password are required.");
        }

        Optional<UserAccount> account = userAccountRepository.findByUsername(username);
        if (account.isEmpty()) {
            throw new NotFoundException(String.format("User with ID %s not found.", username));
        }

        if (!passwordEncoder.matches(password, account.get().getPassword())) {
            throw new BadRequestException("Invalid login credentials.");
        }

        return JwtUtil.generateTokenFromUserName(username, new HashMap<>());
    }

    public void register(UserAccount account) throws BadRequestException {
        if (account == null ||
                StringUtil.isNullOrEmpty(account.getUsername()) ||
                userAccountRepository.findByUsername(account.getUsername()).isPresent() ||
                StringUtil.isNullOrEmpty(account.getEmail()) ||
                userAccountRepository.findByUsername(account.getEmail()).isPresent() ||
                StringUtil.isNullOrEmpty(account.getPassword()) ||
                StringUtil.isNullOrEmpty(account.getAddress()) ||
                StringUtil.isNullOrEmpty(account.getCity()) ||
                StringUtil.isNullOrEmpty(account.getCountry()) ||
                StringUtil.isNullOrEmpty(account.getFirstName()) ||
                StringUtil.isNullOrEmpty(account.getLastName())
        ) {
            throw new BadRequestException("Invalid details for account creation.");
        }

        account.setPassword(passwordEncoder.encode(account.getPassword()));
        userAccountRepository.save(account);
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


    public void followUser(int followerId, int followingId) throws NotFoundException, BadRequestException {
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

    public void unfollowUser(int followerId, int followingId) throws BadRequestException {
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

    public List<TravelPlan> getPlans(int userId) throws NotFoundException {
        if (!userAccountRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with ID %d does not exist.", userId));
        }
        return planRepository.getTravelPlansByAccountId(userId);
    }

    public Post createPost(int userId, Post post) throws NotFoundException {
        if (!userAccountRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with ID %d does not exist.", userId));
        }
        return postRepository.save(post); // Assuming the createPost method should save the post
    }
}
