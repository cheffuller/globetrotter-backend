package com.revature.globetrotters.service;

import com.revature.globetrotters.entity.Comment;
import com.revature.globetrotters.entity.CommentLike;
import com.revature.globetrotters.entity.Post;
import com.revature.globetrotters.entity.PostLike;
import com.revature.globetrotters.entity.TravelPlan;
import com.revature.globetrotters.exception.BadRequestException;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.exception.UnauthorizedException;
import com.revature.globetrotters.repository.CommentLikeRepository;
import com.revature.globetrotters.repository.CommentRepository;
import com.revature.globetrotters.repository.PostLikeRepository;
import com.revature.globetrotters.repository.PostRepository;
import com.revature.globetrotters.repository.TravelPlanLocationRepository;
import com.revature.globetrotters.repository.TravelPlanRepository;
import com.revature.globetrotters.repository.UserAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.revature.globetrotters.utils.SecurityUtil.isModerator;
import static com.revature.globetrotters.utils.SecurityUtil.userIdMatchAuthentication;

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
    private TravelPlanLocationRepository travelPlanLocationRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    public Post createPost(Post post) throws BadRequestException, UnauthorizedException {
        TravelPlan travelPlan = travelPlanRepository.findById(post.getTravelPlanId())
                .orElseThrow(() -> new BadRequestException("Cannot make a post for a travel plan that doesn't exist."));
        if (!userIdMatchAuthentication(travelPlan.getAccountId())) {
            throw new UnauthorizedException("User is not authorized to create a post for this travel plan.");
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
        Post post = postRepository.findByIdIncludingUsername(postId)
                .orElseThrow(() -> new NotFoundException(String.format("Post with ID %d not found.", postId)));
        post.setNumberOfLikes(postLikeRepository.findNumberOfLikesByPostId(postId));
        return post;
    }

    public Post findPostByIdIncludingAllFields(Integer postId) throws NotFoundException {
        Post post = postRepository.findByIdIncludingUsername(postId)
                .orElseThrow(() -> new NotFoundException(String.format("Post with ID %d not found.", postId)));
        post.setNumberOfLikes(postLikeRepository.findNumberOfLikesByPostId(postId));
        post.setComments(commentRepository.findAllByPostIdIncludingUsername(post.getId()));
        post.setLocations(travelPlanLocationRepository.findAllByTravelPlanId(post.getTravelPlanId()));
        return post;
    }

    public Integer findPostIdByTravelPlanId(Integer travelPlanId) throws NotFoundException {
        Optional<Post> optionalPost = postRepository.findByTravelPlanId(travelPlanId);
        Post post = optionalPost.orElseThrow(
                () -> new NotFoundException(String.format("Post with Travel Plan ID %d not found.", travelPlanId)));
        return post.getId();
    }

    public void deletePost(Integer postId) throws NotFoundException, UnauthorizedException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(String.format("Post with ID %d not found.", postId)));
        int posterId = travelPlanRepository.findById(post.getTravelPlanId()).get().getAccountId();
        if (!isModerator() && !userIdMatchAuthentication(posterId)) {
            throw new UnauthorizedException(String.format(
                    "User with ID %d is not authorized to delete post with ID %d",
                    tokenService.getUserAccountId(),
                    postId));
        }
        postRepository.deleteById(postId);
    }

    public Long getNumberOfLikesOnPostById(Integer postId) throws NotFoundException {
        if (!postRepository.existsById(postId)) {
            throw new NotFoundException(String.format("Post with ID %d not found.", postId));
        }
        return postLikeRepository.findNumberOfLikesByPostId(postId);
    }

    public void likePost(Integer postId) throws NotFoundException {
        if (!postRepository.existsById(postId)) {
            throw new NotFoundException(String.format("Post with ID %d not found.", postId));
        }
        postLikeRepository.save(new PostLike(postId, tokenService.getUserAccountId()));
    }

    public void unlikePost(Integer postId) {
        postLikeRepository.delete(new PostLike(postId, tokenService.getUserAccountId()));
    }

    public List<Comment> findCommentsByPostId(Integer postId) throws NotFoundException {
        if (!postRepository.existsById(postId)) {
            throw new NotFoundException(String.format("Post with ID %d does not exist.", postId));
        }
        return commentRepository.findAllByPostIdIncludingUsername(postId);
    }

    public Comment postComment(Comment comment) throws BadRequestException {
        comment.setUserId(tokenService.getUserAccountId());
        if (!postRepository.existsById(comment.getPostId())) {
            throw new BadRequestException(String.format("Post with ID %d does not exist.", comment.getPostId()));
        }
        return commentRepository.save(comment);
    }

    public Comment findCommentById(Integer commentId) throws NotFoundException {
        Optional<Comment> optionalComment = commentRepository.findByIdIncludingUsername(commentId);
        return optionalComment
                .orElseThrow(() -> new NotFoundException(String.format("Comment with ID %d not found.", commentId)));
    }

    public Comment updateCommentContentById(Integer commentId, Comment comment) throws NotFoundException {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        Comment foundComment = optionalComment
                .orElseThrow(() -> new NotFoundException(String.format("Comment with ID %d not found.", commentId)));
        foundComment.setContent(comment.getContent());
        return commentRepository.save(foundComment);
    }

    // Add authorization so only the commenter or a moderator can delete a comment
    public void deleteComment(Integer commentId) throws NotFoundException, UnauthorizedException {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(String.format("Comment with ID %d not found.", commentId)));
        if (!isModerator() && !userIdMatchAuthentication(comment.getUserId())) {
            throw new UnauthorizedException(String.format(
                    "User with ID %d is not authorized to delete comment with ID %d",
                    tokenService.getUserAccountId(),
                    commentId));
        }
        commentRepository.deleteById(commentId);
    }

    public Integer getNumberOfLikesOnCommentById(Integer commentId) throws NotFoundException {
        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundException(String.format("Comment with ID %d does not exist.", commentId));
        }
        return commentLikeRepository.findNumberOfLikesByCommentId(commentId);
    }

    public void likeComment(Integer commentId) throws NotFoundException {
        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundException(String.format("Comment with ID %d not found.", commentId));
        }
        commentLikeRepository.save(new CommentLike(commentId, tokenService.getUserAccountId()));
    }

    public void unlikeComment(Integer commentId) {
        commentLikeRepository.delete(new CommentLike(commentId, tokenService.getUserAccountId()));
    }

    public boolean userLikedPost(Integer postId) throws BadRequestException {
        if (!postRepository.existsById(postId)) {
            throw new BadRequestException(String.format("Post with ID %d does not exist.", postId));
        }

        return postLikeRepository.findById(new PostLike.PostLikeId(postId, tokenService.getUserAccountId()))
                .isPresent();
    }

    public boolean userLikedComment(Integer commentId) throws BadRequestException {
        if (!commentRepository.existsById(commentId)) {
            throw new BadRequestException(String.format("Comment with ID %d does not exist.", commentId));
        }

        return commentLikeRepository.findById(new CommentLike.CommentLikeId(commentId, tokenService.getUserAccountId()))
                .isPresent();
    }
}