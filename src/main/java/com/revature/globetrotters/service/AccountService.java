package com.revature.globetrotters.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.globetrotters.entity.Follow;
import com.revature.globetrotters.entity.Post;
import com.revature.globetrotters.entity.TravelPlan;
import com.revature.globetrotters.entity.UserAccount;
import com.revature.globetrotters.repository.FollowRepository;
import com.revature.globetrotters.repository.PostRepository;
import com.revature.globetrotters.repository.TravelPlanRepository;
import com.revature.globetrotters.repository.UserAccountRepository;

@Service
public class AccountService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private TravelPlanRepository planRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private FollowRepository followRepository;

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


    public Follow follow(int followerId, int followingId) {
        if (followerId <= 0 || followingId <= 0) {
            throw new IllegalArgumentException("User IDs must be greater than zero.");
        }

        Follow.FollowId followId = new Follow.FollowId(followerId, followingId);
        if (followRepository.findById(followId).isPresent()) {
            throw new IllegalArgumentException("Already following user.");
        }

        Follow follow = new Follow(followId);
        return followRepository.save(follow);
    }

    public List<TravelPlan> getPlans(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be greater than zero.");
        }

        return planRepository.getTravelPlansByAccountId(userId);
    }
    
    public Post createPost(int userId, Post post) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be greater than zero.");
        }
        if (post == null) {
            throw new IllegalArgumentException("Post is required.");
        }
        post.setCreated_at(new Date(userId));
    
        return postRepository.save(post);
    }
    

}

