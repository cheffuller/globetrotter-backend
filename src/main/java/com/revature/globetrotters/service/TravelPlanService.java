package com.revature.globetrotters.service;

import com.revature.globetrotters.entity.Collaborator;
import com.revature.globetrotters.entity.Post;
import com.revature.globetrotters.entity.TravelPlan;
import com.revature.globetrotters.entity.TravelPlanLocation;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.exception.UnauthorizedException;
import com.revature.globetrotters.repository.CollaboratorRepository;
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

import static com.revature.globetrotters.utils.SecurityUtil.isModerator;
import static com.revature.globetrotters.utils.SecurityUtil.userIdMatchAuthentication;

@Service
public class TravelPlanService {
    @Autowired
    private CollaboratorRepository collaboratorRepository;
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
        TravelPlan plan = travelPlanRepository.save(travelPlan);
        collaboratorRepository.save(new Collaborator(travelPlan.getAccountId(), travelPlan.getId()));
        return plan;
    }

    public TravelPlan getTravelPlanById(Integer travelPlanId) throws NotFoundException {
        return travelPlanRepository.findById(travelPlanId)
                .orElseThrow(() -> new NotFoundException(String.format("Travel plan with ID %d not found.", travelPlanId))
        );
    }

    // TODO: fix this function. It's not saving the updated information to the database
    // Add authorization so only collaborators can update
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

    public void deleteTravelPlan(Integer travelPlanId) throws NotFoundException, UnauthorizedException {
        TravelPlan plan = travelPlanRepository.findById(travelPlanId)
                .orElseThrow(() -> new NotFoundException("Travel plan not found."));

        if (!isModerator() && !userIdMatchAuthentication(plan.getAccountId())) {
            throw new UnauthorizedException(String.format(
                    "User with ID %d is not authorized to delete travel plan with ID %d",
                    tokenService.getUserAccountId(),
                    travelPlanId
            ));
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
}
