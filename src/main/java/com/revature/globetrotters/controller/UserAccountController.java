package com.revature.globetrotters.controller;

import com.revature.globetrotters.entity.Post;
import com.revature.globetrotters.entity.UserAccount;
import com.revature.globetrotters.exception.BadRequestException;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173/")
public class UserAccountController {
    @Autowired
    private AccountService accountService;
    private static final Logger logger = LoggerFactory.getLogger(UserAccountController.class);

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserAccount account) {
        try {
            UserAccount authenticatedAccount = accountService.authenticate(account.getUsername(), account.getPassword());
            return ResponseEntity.status(HttpStatus.OK).body(authenticatedAccount);
        } catch (IllegalArgumentException | BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserAccount account) {
        try {
            UserAccount newAccount = accountService.register(account);
            return ResponseEntity.ok(newAccount);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{userId}")
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

    @GetMapping("/{userId}/followers")
    public ResponseEntity<?> getFollowers(@PathVariable int userId) {
        try {
            return ResponseEntity.ok(accountService.getFollowers(userId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while retrieving followers: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<?> getFollowing(@PathVariable int userId) {
        try {
            return ResponseEntity.ok(accountService.getFollowing(userId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while retrieving following: " + e.getMessage());
        }
    }

    @PostMapping("/{userId}/following")
    public ResponseEntity<?> follow(@PathVariable int userId, @RequestBody int followingId) {
        try {
            accountService.followUser(userId, followingId);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while following user: " + e.getMessage());
        }
    }

    @GetMapping("/{user-id}/plans")
    public ResponseEntity<?> getPlans(@PathVariable("user-id") int userId) {
        try {
            return ResponseEntity.ok(accountService.getPlans(userId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while retrieving plans: " + e.getMessage());
        }
    }

    @PostMapping("/{user-id}/posts")
    public ResponseEntity<?> createPost(@PathVariable("user-id") int userId, @RequestBody Post post) {
        try {
            return ResponseEntity.ok(accountService.createPost(userId, post));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while creating post: " + e.getMessage());
        }
    }
}
