package com.revature.globetrotters.controller;

import com.revature.globetrotters.entity.Comment;
import com.revature.globetrotters.entity.Post;
import com.revature.globetrotters.entity.UserAccount;
import com.revature.globetrotters.exception.BadRequestException;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.service.PostService;
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

import java.util.List;

@Controller
@CrossOrigin(origins = "http://localhost:5173")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping("users/{userId}/posts")
    public ResponseEntity<List<Post>> getAllPosts(@PathVariable int userId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(postService.findPostsByUserId(userId));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("posts")
    public ResponseEntity<Post> postPost(Post newPost) {
        try {
            Post post = postService.createPost(newPost);
            return ResponseEntity.status(HttpStatus.OK).body(post);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("posts/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable int postId) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(postService.findPostById(postId));
    }

    @DeleteMapping("posts/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable int postId) {
        try {
            postService.deletePost(postId);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("posts/{postId}/likes")
    public ResponseEntity<Integer> getPostLikes(@PathVariable int postId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(postService.getPostLikes(postId));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("posts/{postId}/likes")
    public ResponseEntity<String> postLikePost(@PathVariable int postId, @RequestBody UserAccount account) {
        try {
            postService.likePost(postId, account.getId());
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("posts/{postId}/comments")
    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable int postId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(postService.findCommentsByPostId(postId));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("comments")
    public ResponseEntity<Comment> postComment(@RequestBody Comment comment) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(postService.postComment(comment));
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("comments/{commentId}")
    public ResponseEntity<Comment> getCommentById(@PathVariable int commentId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(postService.findCommentById(commentId));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable int commentId) {
        try {
            postService.deleteComment(commentId);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("comments/{commentId}/likes")
    public ResponseEntity<Integer> getCommentLikes(@PathVariable int commentId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(postService.getCommentLikes(commentId));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
