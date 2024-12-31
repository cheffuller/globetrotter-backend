package com.revature.globetrotters.service;

import com.revature.globetrotters.entity.Comment;
import com.revature.globetrotters.entity.Post;
import com.revature.globetrotters.entity.TravelPlan;
import com.revature.globetrotters.entity.TravelPlanLocation;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.repository.CommentRepository;
import com.revature.globetrotters.repository.PostLikeRepository;
import com.revature.globetrotters.repository.PostRepository;
import com.revature.globetrotters.repository.TravelPlanLocationRepository;
import com.revature.globetrotters.repository.TravelPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TravelPlanService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostLikeRepository postLikeRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private TravelPlanRepository travelPlanRepository;
    @Autowired
    private TravelPlanLocationRepository travelPlanLocationRepository;

    public TravelPlanService(TravelPlanRepository travelPlanRepository) {
        this.travelPlanRepository = travelPlanRepository;
    }

    public TravelPlan createTravelPlan(TravelPlan travelPlan) {
        travelPlan.setAccountId(tokenService.getUserAccountId());
        // Add validation for travel plan
        return travelPlanRepository.save(travelPlan);
    }

    public TravelPlan getTravelPlanById(Integer travelPlanId) {
        return travelPlanRepository.getTravelPlanById(travelPlanId);
    }

    // Add authorization so only the poster or a moderator can update
    public TravelPlanLocation updateTravelPlan(TravelPlanLocation travelPlan) {
        Optional<TravelPlanLocation> existingTravelPlan = travelPlanLocationRepository.findById(travelPlan.getId());
        if (existingTravelPlan == null) {
            throw new IllegalArgumentException("Travel plan not found");
        }

        TravelPlanLocation updatedTravelPlan = existingTravelPlan.get();

        if (updatedTravelPlan.getCity().isEmpty() || updatedTravelPlan.getCountry().isEmpty() || updatedTravelPlan.getStartDate() == null || updatedTravelPlan.getEndDate() == null) {
            throw new IllegalArgumentException("Invalid travel plan location");
        }

        return travelPlanLocationRepository.save(updatedTravelPlan);
    }

    // Add authorization so only the poster or a moderator can delete
    public void deleteTravelPlan(Integer travelPlanId) {
        if (travelPlanRepository.getTravelPlanById(travelPlanId) == null) {
            throw new IllegalArgumentException("Travel plan not found");
        }
        travelPlanRepository.deleteById(travelPlanId);
    }

    public List<TravelPlan> findMostRecentPublicTravelPlan(int limit) {
        Pageable pageable = Pageable.ofSize(limit);
        return travelPlanRepository.findRecentPublicTravelPlans(pageable);
    }

    public Integer getNumberOfLikesOnPostByTravelPlanId(Integer planId) throws NotFoundException {
        if (!travelPlanRepository.existsById(planId)) {
            throw new NotFoundException(String.format("Travel plan with ID %d not found.", planId));
        }

        Optional<Post> optionalPost = postRepository.findByTravelPlanId(planId);

        Post post = optionalPost.orElseThrow(() ->
            new NotFoundException(String.format("Post with Travel Plan ID %d not found.", planId))
        );

        return postLikeRepository.findNumberOfLikesByPostId(post.getId());
    }

    public Integer getNumberOfCommentsOnPostByTravelPlanId(Integer planId) throws NotFoundException {
        if (!travelPlanRepository.existsById(planId)) {
            throw new NotFoundException(String.format("Travel plan with ID %d not found.", planId));
        }

        Optional<Post> optionalPost = postRepository.findByTravelPlanId(planId);

        Post post = optionalPost.orElseThrow(() ->
            new NotFoundException(String.format("Post with Travel Plan ID %d not found.", planId))
        );

        return commentRepository.findNumberOfCommentsByPostId(post.getId());
    }

    public List<TravelPlan> getTravelPlansByAccountId(Integer accountId) {
        return travelPlanRepository.getTravelPlansByAccountId(accountId);
    }
}
