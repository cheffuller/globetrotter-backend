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

    private void validateNotEmpty(String field, String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " is required.");
        }
    }

    public UserAccount authenticate(String username, String password) {
        validateNotEmpty("Username", username);
        validateNotEmpty("Password", password);

        return Optional.ofNullable(userAccountRepository.findByUsername(username))
                .filter(account -> account.getPassword().equals(password))
                .orElse(null);
    }

    public UserAccount register(UserAccount account) {
        if (account == null) throw new IllegalArgumentException("Account is required.");

        validateNotEmpty("Username", account.getUsername());
        validateNotEmpty("Password", account.getPassword());
        validateNotEmpty("Password Salt", account.getPasswordSalt());
        validateNotEmpty("Address", account.getAddress());
        validateNotEmpty("City", account.getCity());
        validateNotEmpty("Country", account.getCountry());
        validateNotEmpty("Email", account.getEmail());
        validateNotEmpty("First name", account.getFirstName());
        validateNotEmpty("Last name", account.getLastName());

        if (userAccountRepository.findByUsername(account.getUsername()) != null) {
            throw new IllegalArgumentException("Username is already taken.");
        }

        return userAccountRepository.save(account);
    }

    public Optional<UserAccount> getUser(int userId) {
        if (userId <= 0) throw new IllegalArgumentException("User ID must be greater than zero.");
        return userAccountRepository.findById(userId);
    }

    public List<Follow> getFollowers(int userId) {
        if (userId <= 0) throw new IllegalArgumentException("User ID must be greater than zero.");
        return followRepository.findByFollowing(userId);
    }

    public List<Follow> getFollowing(int userId) {
        if (userId <= 0) throw new IllegalArgumentException("User ID must be greater than zero.");
        return followRepository.findByFollower(userId);
    }

    public void followUser(int followerId, int followingId) throws NotFoundException, BadRequestException {
        if (!userProfileRepository.existsById(followerId) || !userProfileRepository.existsById(followingId)) {
            throw new NotFoundException("User(s) not found.");
        }

        if (followRepository.existsById(new Follow.FollowId(followerId, followingId)) ||
                followRequestRepository.existsById(new FollowRequest.FollowRequestId(followerId, followingId))) {
            throw new BadRequestException("Follow request already exists.");
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
        } else {
            throw new BadRequestException("Not following or requested to follow.");
        }
    }

    public List<TravelPlan> getPlans(int userId) {
        if (userId <= 0) throw new IllegalArgumentException("User ID must be greater than zero.");
        return planRepository.getTravelPlansByAccountId(userId);
    }

    public Post createPost(int userId, Post post) {
        return postRepository.save(post); // Assuming the createPost method should save the post
    }
}
