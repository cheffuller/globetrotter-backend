package com.revature.globetrotters.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.revature.globetrotters.entity.UserAccount;
import com.revature.globetrotters.service.AccountService;

@Controller
@RequestMapping("/accounts")
@CrossOrigin(origins = "http://localhost:5173/")
public class UserAccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserAccount account) {
        try {
            UserAccount authenticatedAccount = accountService.authenticate(account.getUsername(), account.getPassword());
    
            if (authenticatedAccount != null) {
                return ResponseEntity.ok(authenticatedAccount);
            } else {
                return ResponseEntity.status(401).body("Invalid username or password");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred during login: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserAccount account) {
        try {
            UserAccount newAccount = accountService.register(account);
            return ResponseEntity.ok(newAccount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred during registration: " + e.getMessage());
        }
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUser(@PathVariable int userId) {
        try {
            Optional<UserAccount> account = accountService.getUser(userId);
            if (account == null) {
                return ResponseEntity.status(404).body("User not found");
            }
            return ResponseEntity.ok(account);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while retrieving user: " + e.getMessage());
        }
    }
    
    
}
