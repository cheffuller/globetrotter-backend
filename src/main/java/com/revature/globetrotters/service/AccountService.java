package com.revature.globetrotters.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.globetrotters.entity.UserAccount;
import com.revature.globetrotters.repository.UserAccountRepository;

@Service
public class AccountService {

    @Autowired
    private UserAccountRepository userAccountRepository;

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
}

