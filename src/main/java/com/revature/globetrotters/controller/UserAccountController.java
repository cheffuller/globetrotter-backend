package com.revature.globetrotters.controller;

import com.revature.globetrotters.entity.TravelPlan;
import com.revature.globetrotters.entity.UserAccount;
import com.revature.globetrotters.exception.BadRequestException;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.exception.UnauthorizedException;
import com.revature.globetrotters.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173/")
public class UserAccountController {
    @Autowired
    private AccountService accountService;
    private static final Logger logger = LoggerFactory.getLogger(UserAccountController.class);

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity handleBadRequestException(BadRequestException exception) {
        logger.info(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(NotFoundException exception) {
        logger.info(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity handleUnauthorizedException(UnauthorizedException exception) {
        logger.info(exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserAccount account) throws UnauthorizedException {
        String token = accountService.login(account);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserAccount account) throws BadRequestException {
        accountService.register(account);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable int userId) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getUser(userId));
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<?> getFollowers(@PathVariable int userId) throws NotFoundException {
        return ResponseEntity.ok(accountService.findListOfUsersFollowing(userId));
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<?> getFollowing(@PathVariable int userId) throws NotFoundException {
        return ResponseEntity.ok(accountService.findListOfUsersFollowed(userId));
    }

    @PostMapping("/{userId}/following")
    public ResponseEntity follow(@PathVariable int userId) throws NotFoundException, BadRequestException {
        accountService.followUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping("/{userId}/following")
    public ResponseEntity unfollow(@PathVariable int userId) throws BadRequestException {
        accountService.unfollowUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/{user-id}/plans")
    public ResponseEntity<List<TravelPlan>> getPlans(@PathVariable("user-id") int userId) throws NotFoundException {
        return ResponseEntity.ok(accountService.getPlans(userId));
    }
}
