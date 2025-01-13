package com.revature.globetrotters.controller;

import com.revature.globetrotters.entity.Post;
import com.revature.globetrotters.exception.BadRequestException;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.exception.UnauthorizedException;
import com.revature.globetrotters.service.PostService;
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

import java.util.List;

@Controller
@CrossOrigin(origins = "http://globetrotter-revature.s3-website-us-east-1.amazonaws.com")
public class PostController {
    @Autowired
    private PostService postService;
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

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

    @GetMapping("users/{userId}/posts")
    public ResponseEntity<List<Post>> getAllPostsByUserId(@PathVariable(name = "userId") int userId)
            throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(postService.findPostsByUserId(userId));
    }

    @PostMapping("posts")
    public ResponseEntity<Post> postPost(@RequestBody Post newPost) throws BadRequestException, UnauthorizedException {
        Post post = postService.createPost(newPost);
        return ResponseEntity.status(HttpStatus.OK).body(post);
    }

    @GetMapping("posts/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable(name = "postId") int postId) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(postService.findPostByIdIncludingAllFields(postId));
    }

    @GetMapping("posts/plans/{travelPlanId}")
    public ResponseEntity<Integer> getPostIdByTravelPlanId(@PathVariable(name = "travelPlanId") int travelPlanId)
            throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(postService.findPostIdByTravelPlanId(travelPlanId));
    }

    @DeleteMapping("posts/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "postId") int postId)
            throws NotFoundException, UnauthorizedException {
        postService.deletePost(postId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("posts/{postId}/likes")
    public ResponseEntity<Long> getNumberOfLikesOnPostById(@PathVariable(name = "postId") int postId)
            throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getNumberOfLikesOnPostById(postId));
    }

    @PostMapping("posts/{postId}/likes")
    public ResponseEntity<?> likePost(@PathVariable(name = "postId") int postId) throws NotFoundException {
        postService.likePost(postId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping("posts/{postId}/likes")
    public ResponseEntity<?> unlikePost(@PathVariable(name = "postId") int postId) {
        postService.unlikePost(postId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("posts/{postId}/liked")
    public ResponseEntity<Boolean> userLikedPost(@PathVariable(name = "postId") int postId) throws BadRequestException {
        return ResponseEntity.status(HttpStatus.OK).body(postService.userLikedPost(postId));
    }

}
