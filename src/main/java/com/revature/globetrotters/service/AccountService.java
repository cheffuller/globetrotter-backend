package com.revature.globetrotters.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.globetrotters.entity.UserAccount;
import com.revature.globetrotters.repository.FollowRepository;
import com.revature.globetrotters.repository.UserAccountRepository;

@Service
public class AccountService {

    @Autowired
    private UserAccountRepository userAccountRepository;

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
    

    
}

