package com.revature.globetrotters.controller;

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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.revature.globetrotters.entity.Comment;
import com.revature.globetrotters.exception.BadRequestException;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.exception.UnauthorizedException;
import com.revature.globetrotters.service.KafkaProducerService;
import com.revature.globetrotters.service.PostService;
import com.revature.globetrotters.service.TokenService;

@Controller
@CrossOrigin(origins = "http://globetrotter-revature.s3-website-us-east-1.amazonaws.com")
@RequestMapping("comments")
public class CommentController {
    @Autowired
    private PostService postService;
    @Autowired
    private KafkaProducerService kafkaProducerService;
    @Autowired
    private TokenService tokenService;
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException exception) {
        logger.info(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException exception) {
        logger.info(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorizedException(UnauthorizedException exception) {
        logger.info(exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @PostMapping
    public ResponseEntity<Comment> postComment(@RequestBody Comment comment) throws BadRequestException {
        return ResponseEntity.status(HttpStatus.OK).body(postService.postComment(comment));
    }

    @GetMapping("{commentId}")
    public ResponseEntity<Comment> getCommentById(@PathVariable int commentId) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(postService.findCommentById(commentId));
    }

    @PatchMapping("{commentId}")
    public ResponseEntity<Comment> patchCommentContentById(@PathVariable(name = "commentId") int commentId,
                                                           @RequestBody Comment comment) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(postService.updateCommentContentById(commentId, comment));
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Integer commentId)
            throws NotFoundException, UnauthorizedException {
        postService.deleteComment(commentId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("{commentId}/likes")
    public ResponseEntity<Integer> getNumberOfLikesOnCommentById(@PathVariable int commentId) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getNumberOfLikesOnCommentById(commentId));
    }

    @PostMapping("{commentId}/likes")
    public ResponseEntity<?> likeComment(@PathVariable int commentId) throws NotFoundException, JsonProcessingException {
        kafkaProducerService.publishMessage("like comment", commentId, tokenService.getUserAccountId());
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping("{commentId}/likes")
    public ResponseEntity<?> unlikeComment(@PathVariable int commentId) throws NotFoundException, JsonProcessingException {
        kafkaProducerService.publishMessage("unlike comment",commentId, tokenService.getUserAccountId());
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("{commentId}/liked")
    public ResponseEntity<Boolean> userLikedComment(@PathVariable int commentId) throws BadRequestException {
        return ResponseEntity.status(HttpStatus.OK).body(postService.userLikedComment(commentId));
    }
}
