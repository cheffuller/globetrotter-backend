package com.revature.globetrotters.controller;

import com.revature.globetrotters.entity.UserProfile;
import com.revature.globetrotters.exception.BadRequestException;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.exception.UnauthorizedException;
import com.revature.globetrotters.service.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
@CrossOrigin(origins = "http://http://globetrotter-revature.s3-website-us-east-1.amazonaws.com:5173")
public class UserProfileController {
    @Autowired
    private UserProfileService userProfileService;
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

    @PostMapping("/profile")
    public ResponseEntity updateUserProfile(@RequestBody UserProfile userProfile)
            throws NotFoundException, BadRequestException {
        userProfileService.updateUserProfile(userProfile);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/{username}/profile")
    public ResponseEntity<UserProfile> getProfile(@PathVariable("username") String username) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(userProfileService.findUserProfile(username));
    }

    @GetMapping("/{username}/profile/display-name")
    public ResponseEntity<String> getDisplayNameFromUsername(@PathVariable(name = "username") String username)
            throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(userProfileService.findDisplayNameFromUsername(username));
    }

    @GetMapping("/{userId}/display-name")
    public ResponseEntity<String> getDisplayNameFromUserId(@PathVariable(name = "userId") Integer userId)
            throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(userProfileService.findDisplayNameFromUserId(userId));
    }
}