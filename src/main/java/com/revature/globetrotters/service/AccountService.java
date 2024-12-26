package com.revature.globetrotters.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

@Service
public class AccountService {
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


    public UserAccount authenticate(String username, String password) {

        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Username and password are required.");
        }

        UserAccount account = userAccountRepository.findByUsername(username);
        if (account != null && account.getPassword().equals(password)) {
            return account;
        }
        return null;
    }

    public UserAccount register(UserAccount account) {

        if (account == null) {
            throw new IllegalArgumentException("Account is required.");
        }
    
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required.");
        }
    
        if (account.getPassword() == null || account.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }

        if (account.getPasswordSalt() == null || account.getPasswordSalt().trim().isEmpty()) {
            throw new IllegalArgumentException("Password Salt is required.");
        }
    
        if (account.getAddress() == null || account.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Address is required.");
        }
    
        if (account.getCity() == null || account.getCity().trim().isEmpty()) {
            throw new IllegalArgumentException("City is required.");
        }
    
        if (account.getCountry() == null || account.getCountry().trim().isEmpty()) {
            throw new IllegalArgumentException("Country is required.");
        }
    
        if (account.getEmail() == null || account.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required.");
        }
    
        if (account.getFirstName() == null || account.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required.");
        }
    
        if (account.getLastName() == null || account.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required.");
        }
    
        if (userAccountRepository.findByUsername(account.getUsername()) != null) {
            throw new IllegalArgumentException("Username is already taken.");
        }
    
        return userAccountRepository.save(account);
    }

    public Optional<UserAccount> getUser(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be greater than zero.");
        }
        return userAccountRepository.findById(userId);
    }


    public List<Follow> getFollowers(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be greater than zero.");
        }

        return followRepository.findByFollowing(userId);
    }

    public List<Follow> getFollowing(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be greater than zero.");
        }

        return followRepository.findByFollower(userId);
    }


    public void followUser(int followerId, int followingId) throws NotFoundException, BadRequestException {
        if (!userProfileRepository.existsById(followerId)) {
            throw new NotFoundException(String.format("User with ID %d not found", followerId));
        }

        if (!userProfileRepository.existsById(followingId)) {
            throw new NotFoundException(String.format("User with ID %d not found", followerId));
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
        }

        throw new BadRequestException(String.format("User with ID %d is not following and has not requested to " +
                "follow user with id %d.", followingId, followingId));
    }

    public List<TravelPlan> getPlans(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be greater than zero.");
        }

        return planRepository.getTravelPlansByAccountId(userId);
    }

    public Post createPost(int userId, Post post) {
        return null;
    }
}

