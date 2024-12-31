package com.revature.globetrotters.controller;

import com.revature.globetrotters.entity.Comment;
import com.revature.globetrotters.entity.Post;
import com.revature.globetrotters.entity.UserAccount;
import com.revature.globetrotters.exception.BadRequestException;
import com.revature.globetrotters.exception.NotFoundException;
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
@CrossOrigin(origins = "http://localhost:5173")
public class PostController {
    @Autowired
    private PostService postService;
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

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

    @GetMapping("users/{userId}/posts")
    public ResponseEntity<List<Post>> getAllPostsByUserId(@PathVariable int userId)
            throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(postService.findPostsByUserId(userId));
    }

    @PostMapping("posts")
    public ResponseEntity<Post> postPost(@RequestBody Post newPost) throws BadRequestException {
        Post post = postService.createPost(newPost);
        return ResponseEntity.status(HttpStatus.OK).body(post);
    }

    @GetMapping("posts/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable int postId) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(postService.findPostById(postId));
    }

    @DeleteMapping("posts/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable int postId) throws NotFoundException {
        postService.deletePost(postId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("posts/{postId}/likes")
    public ResponseEntity<Integer> getNumberOfLikesOnPostById(@PathVariable int postId) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getNumberOfLikesOnPostById(postId));
    }

    @GetMapping("posts/{postId}/comments")
    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable int postId) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(postService.findCommentsByPostId(postId));
    }

    @PostMapping("comments")
    public ResponseEntity<Comment> postComment(@RequestBody Comment comment) throws BadRequestException {
        return ResponseEntity.status(HttpStatus.OK).body(postService.postComment(comment));
    }

    @GetMapping("comments/{commentId}")
    public ResponseEntity<Comment> getCommentById(@PathVariable int commentId) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(postService.findCommentById(commentId));
    }

    @DeleteMapping("comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable int commentId) throws NotFoundException {
        postService.deleteComment(commentId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("comments/{commentId}/likes")
    public ResponseEntity<Integer> getNumberOfLikesOnCommentById(@PathVariable int commentId) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getNumberOfLikesOnCommentById(commentId));
    }

    @PostMapping("comments/{commentId}/likes")
    public ResponseEntity likeComment(@PathVariable int commentId) throws NotFoundException {
        postService.likeComment(commentId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping("comments/{commentId}/likes")
    public ResponseEntity<?> unlikeComment(@PathVariable int commentId) {
        postService.unlikeComment(commentId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("posts/{postId}/likes")
    public ResponseEntity likePost(@PathVariable int postId) throws NotFoundException {
        postService.likePost(postId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping("posts/{postId}/likes")
    public ResponseEntity unlikePost(@PathVariable int postId) {
        postService.unlikePost(postId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
