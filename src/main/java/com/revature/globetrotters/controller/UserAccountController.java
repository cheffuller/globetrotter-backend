package com.revature.globetrotters.controller;

import java.util.Optional;

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

import com.revature.globetrotters.entity.Post;
import com.revature.globetrotters.entity.UserAccount;
import com.revature.globetrotters.exception.BadRequestException;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.service.AccountService;

@Controller
@RequestMapping("/users")
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
                throw new BadRequestException("Invalid username or password");
            }
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during login");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserAccount account) {
        try {
            UserAccount newAccount = accountService.register(account);
            return ResponseEntity.ok(newAccount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during registration");
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable int userId) {
        try {
            Optional<UserAccount> account = accountService.getUser(userId);
            if (account.isEmpty()) {
                throw new NotFoundException("User not found");
            }
            return ResponseEntity.ok(account);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving user");
        }
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<?> getFollowers(@PathVariable int userId) {
        try {
            return ResponseEntity.ok(accountService.getFollowers(userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving followers");
        }
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<?> getFollowing(@PathVariable int userId) {
        try {
            return ResponseEntity.ok(accountService.getFollowing(userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving following");
        }
    }

    @PostMapping("/{userId}/following")
    public ResponseEntity<?> follow(@PathVariable int userId, @RequestBody int followingId) {
        try {
            accountService.followUser(userId, followingId);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while following user");
        }
    }

    @GetMapping("/{user-id}/plans")
    public ResponseEntity<?> getPlans(@PathVariable("user-id") int userId) {
        try {
            return ResponseEntity.ok(accountService.getPlans(userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving plans");
        }
    }

    @PostMapping("/{user-id}/posts")
    public ResponseEntity<?> createPost(@PathVariable("user-id") int userId, @RequestBody Post post) {
        try {
            return ResponseEntity.ok(accountService.createPost(userId, post));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating post");
        }
    }
}
