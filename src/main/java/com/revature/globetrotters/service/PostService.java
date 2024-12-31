package com.revature.globetrotters.service;

import com.revature.globetrotters.entity.Comment;
import com.revature.globetrotters.entity.CommentLike;
import com.revature.globetrotters.entity.Post;
import com.revature.globetrotters.entity.PostLike;
import com.revature.globetrotters.exception.BadRequestException;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.repository.CommentLikeRepository;
import com.revature.globetrotters.repository.CommentRepository;
import com.revature.globetrotters.repository.PostLikeRepository;
import com.revature.globetrotters.repository.PostRepository;
import com.revature.globetrotters.repository.TravelPlanRepository;
import com.revature.globetrotters.repository.UserAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private TokenService tokenService;
    @Autowired
    private TravelPlanRepository travelPlanRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    public Post createPost(Post post) throws BadRequestException {
        if (!travelPlanRepository.existsById(post.getTravelPlanId())) {
            throw new BadRequestException(String.format("Travel plan with ID %d does not exist.", post.getTravelPlanId()));
        }
        return postRepository.save(post);
    }

    public List<Post> findPostsByUserId(Integer userId) throws NotFoundException {
        if (!userAccountRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with ID %d does not exist.", userId));
        }
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

    public Integer getNumberOfLikesOnPostById(Integer postId) throws NotFoundException {
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

    public void unlikePost(Integer postId) {
        postLikeRepository.delete(new PostLike(postId, tokenService.getUserAccountId()));
    }

    public List<Comment> findCommentsByPostId(Integer postId) throws NotFoundException {
        if (!postRepository.existsById(postId)) {
            throw new NotFoundException(String.format("Post with ID %d does not exist.", postId));
        }
        return commentRepository.findAllByPostId(postId);
    }

    public Comment postComment(Comment comment) throws BadRequestException {
        if (!userAccountRepository.existsById(comment.getUserId())) {
            throw new BadRequestException(String.format("User with ID %d does not exist.", comment.getUserId()));
        }
        if (!postRepository.existsById(comment.getPostId())) {
            throw new BadRequestException(String.format("Post with ID %d does not exist.", comment.getPostId()));
        }
        return commentRepository.save(comment);
    }

    public Comment findCommentById(Integer commentId) throws NotFoundException {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        return optionalComment.orElseThrow(() -> new NotFoundException(String.format("Comment with ID %d not found.", commentId)));
    }

    public void deleteComment(Integer commentId) throws NotFoundException {
        if (commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
        } else {
            throw new NotFoundException(String.format("Comment with ID %d not found.", commentId));
        }
    }

    public Integer getNumberOfLikesOnCommentById(Integer commentId) throws NotFoundException {
        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundException(String.format("Comment with ID %d does not exist.", commentId));
        }
        return commentLikeRepository.findNumberOfLikesByCommentId(commentId);
    }

    public void likeComment(Integer commentId, Integer userId) throws NotFoundException {
        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundException(String.format("Comment with ID %d not found.", commentId));
        }
        if (!userAccountRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with ID %d not found.", userId));
        }
        commentLikeRepository.save(new CommentLike(commentId, userId));
    }

    public void unlikeComment(Integer commentId, Integer userId) {
        commentLikeRepository.delete(new CommentLike(commentId, userId));
    }
}