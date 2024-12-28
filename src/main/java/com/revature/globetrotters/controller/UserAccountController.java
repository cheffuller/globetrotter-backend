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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173/")
public class UserAccountController {
    @Autowired
    private AccountService accountService;
    private static final Logger logger = LoggerFactory.getLogger(UserAccountController.class);

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserAccount account) {
        try {
            String token = accountService.authenticate(account.getUsername(), account.getPassword());
            return ResponseEntity.status(HttpStatus.OK).body(token);
        } catch (IllegalArgumentException | BadRequestException e) {
            logger.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (NotFoundException e) {
            logger.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserAccount account) {
        try {
            accountService.register(account);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (BadRequestException e) {
            logger.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable int userId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(accountService.getUser(userId));
        } catch (NotFoundException e) {
            logger.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<?> getFollowers(@PathVariable int userId) {
        try {
            return ResponseEntity.ok(accountService.findListOfUsersFollowing(userId));
        } catch (NotFoundException e) {
            logger.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<?> getFollowing(@PathVariable int userId) {
        try {
            return ResponseEntity.ok(accountService.findListOfUsersFollowed(userId));
        } catch (NotFoundException e) {
            logger.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/{userId}/following")
    public ResponseEntity<?> follow(@PathVariable int userId, @RequestBody UserAccount account) {
        try {
            accountService.followUser(account.getId(), userId);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (NotFoundException e) {
            logger.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (BadRequestException e) {
            logger.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{userId}/following")
    public ResponseEntity<?> unfollow(@PathVariable int userId, @RequestBody UserAccount account) {
        try {
            accountService.unfollowUser(account.getId(), userId);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (BadRequestException e) {
            logger.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{user-id}/plans")
    public ResponseEntity<?> getPlans(@PathVariable("user-id") int userId) {
        try {
            return ResponseEntity.ok(accountService.getPlans(userId));
        } catch (NotFoundException e) {
            logger.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/{user-id}/posts")
    public ResponseEntity<?> createPost(@PathVariable("user-id") int userId, @RequestBody Post post) {
        try {
            return ResponseEntity.ok(accountService.createPost(userId, post));
        } catch (NotFoundException e) {
            logger.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
