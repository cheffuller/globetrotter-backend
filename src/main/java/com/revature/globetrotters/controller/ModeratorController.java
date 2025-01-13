package com.revature.globetrotters.controller;

import com.revature.globetrotters.entity.ModeratorAccount;
import com.revature.globetrotters.exception.BadRequestException;
import com.revature.globetrotters.exception.UnauthorizedException;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.service.ModeratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/moderators")
@CrossOrigin(origins = "http://globetrotter-revature.s3-website-us-east-1.amazonaws.com")
public class ModeratorController {
    @Autowired
    private ModeratorService moderatorService;
    private static final Logger logger = LoggerFactory.getLogger(ModeratorController.class);

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
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody ModeratorAccount account) throws UnauthorizedException {
        String token = moderatorService.login(account);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @PostMapping("/ban/{userId}")
    public ResponseEntity banUser(@PathVariable(name = "userId") int userId)
            throws UnauthorizedException, NotFoundException {
        moderatorService.banUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping("/ban/{userId}")
    public ResponseEntity unbanUser(@PathVariable(name = "userId") int userId)
            throws UnauthorizedException, NotFoundException {
        moderatorService.unbanUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
