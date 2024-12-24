package com.revature.globetrotters.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.globetrotters.entity.Post;
import com.revature.globetrotters.entity.PostComment;
import com.revature.globetrotters.repository.PostCommentRepository;
import com.revature.globetrotters.repository.PostRepository;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostCommentRepository postCommentRepository;
    
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    // TODO: write custom postRepository method
    public List<Post> findPostsByUserId(Integer userId) {
        return postRepository.findByUserId(userId);
    }

    public Post findPostById(Integer postId) throws Exception {
        Optional<Post> optionalPost = postRepository.findById(postId);
       return optionalPost.orElseThrow(() -> new Exception("Post not found."));
    }

    public void deletePost(Integer postId) throws Exception{
        if (postRepository.existsById(postId)) {
            postRepository.deleteById(postId);
        } else {
            throw new Exception();
        }
        
    }

    // TODO: write custom postRepository method
    public Integer getPostLikes(Integer postId) {
        return postRepository.getPostLikes(postId);
    }
    
    // TODO: write custom postRepository method
    public void likePost(Integer postId) {
        postRepository.likePost(postId);
    }

    // TODO: write custom postCommentRepository method
    public List<PostComment> findCommentsByPostId(Integer postId) {
        return postCommentRepository.findCommentsByPostId(postId);
    }

    // TODO: write custom postCommentRepository method
    public PostComment postComment(Integer postId, PostComment postComment) {
        return postCommentRepository.createPostComment(postId, postComment);
    }

    public PostComment findCommentById(Integer commentId) throws Exception {
        Optional<PostComment> optionalComment = postCommentRepository.findById(commentId);
        return optionalComment.orElseThrow(() -> new Exception("Comment not found."));
    }

    public void deleteComment(Integer commentId) throws Exception {
        if (postCommentRepository.existsById(commentId)) {
            postCommentRepository.deleteById(commentId);
        } else {
            throw new Exception();
        }
    }

    // TODO: write custom postCommentRepository method
    public Integer getCommentLikes(Integer commentId) {
        return postCommentRepository.getCommentLikes(commentId);
    }
}