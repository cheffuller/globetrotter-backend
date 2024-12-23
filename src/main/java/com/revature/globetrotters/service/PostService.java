package com.revature.globetrotters.service;

import com.revature.globetrotters.entity.Post;
import com.revature.globetrotters.entity.Comment;
import com.revature.globetrotters.entity.PostLike;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.repository.CommentLikeRepository;
import com.revature.globetrotters.repository.CommentRepository;
import com.revature.globetrotters.repository.PostLikeRepository;
import com.revature.globetrotters.repository.PostRepository;
import com.revature.globetrotters.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentLikeRepository commentLikeRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostLikeRepository postLikeRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    // TODO: write custom postRepository method
    public List<Post> findPostsByUserId(Integer userId) {
        return postRepository.findAllByUserId(userId);
    }

    public Post findPostById(Integer postId) throws NotFoundException {
        Optional<Post> optionalPost = postRepository.findById(postId);
        return optionalPost.orElseThrow(() -> new NotFoundException(String.format("Post with ID %d not found.", postId)));
    }

    public void deletePost(Integer postId) throws NotFoundException {
        if (postRepository.existsById(postId)) {
            postRepository.deleteById(postId);
        } else {
            throw new NotFoundException(String.format("Post with ID %d not found.", postId));
        }
    }

    public Integer getPostLikes(Integer postId) throws NotFoundException {
        if (!postRepository.existsById(postId)) {
            throw new NotFoundException(String.format("Post with ID %d not found.", postId));
        }
        return postLikeRepository.findNumberOfLikesByPostId(postId);
    }

    public void likePost(Integer postId, Integer userId) throws NotFoundException {
        if (!postRepository.existsById(postId)) {
            throw new NotFoundException(String.format("Post with ID %d not found.", postId));
        }
        if (!userAccountRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with ID %d not found.", userId));
        }
        postLikeRepository.save(new PostLike(postId, userId));
    }

    // TODO: write custom postCommentRepository method
    public List<Comment> findCommentsByPostId(Integer postId) {
        return commentRepository.findAllByPostId(postId);
    }

    // TODO: write custom postCommentRepository method
    public Comment postComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment findCommentById(Integer commentId) throws NotFoundException {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        return optionalComment.orElseThrow(() -> new NotFoundException(String.format("Comment with ID %d not found.", commentId)));
    }

    public void deleteComment(Integer commentId) throws Exception {
        if (commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
        } else {
            throw new NotFoundException(String.format("Comment with ID %d not found.", commentId));
        }
    }

    // TODO: write custom postCommentRepository method
    public Integer getCommentLikes(Integer commentId) {
        return commentLikeRepository.findNumberOfLikesByCommentId(commentId);
    }
}