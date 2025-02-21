package com.revature.globetrotters.service;

import com.revature.globetrotters.entity.Collaborator;
import com.revature.globetrotters.entity.Follow;
import com.revature.globetrotters.entity.Post;
import com.revature.globetrotters.entity.TravelPlan;
import com.revature.globetrotters.exception.BadRequestException;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.exception.UnauthorizedException;
import com.revature.globetrotters.repository.CollaboratorRepository;
import com.revature.globetrotters.repository.CommentRepository;
import com.revature.globetrotters.repository.FollowRepository;
import com.revature.globetrotters.repository.PostLikeRepository;
import com.revature.globetrotters.repository.PostRepository;
import com.revature.globetrotters.repository.TravelPlanLocationRepository;
import com.revature.globetrotters.repository.TravelPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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
    private FollowRepository followRepository;
    @Autowired
    private PostLikeRepository postLikeRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostService postService;
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

    // TODO: add and remove collaborator function

    public TravelPlan getTravelPlanById(Integer travelPlanId) throws NotFoundException {
        TravelPlan plan = travelPlanRepository.findById(travelPlanId).orElseThrow(() ->
                new NotFoundException(String.format("Travel plan with ID %d not found.", travelPlanId))
        );
        try {
            int postId = postService.findPostIdByTravelPlanId(travelPlanId);
            plan.setPost(postService.findPostByIdIncludingAllFields(postId));
        } catch (Exception e) {
        }
        return plan;
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

    public TravelPlan updateTravelPlan(TravelPlan travelPlan) throws NotFoundException, UnauthorizedException, BadRequestException {
        if (travelPlan == null ||
                travelPlan.getId() == null ||
                travelPlan.getAccountId() == null ||
                travelPlan.getIsFavorited() == null ||
                travelPlan.getIsPublished() == null) {
            throw new BadRequestException("Invalid travel plan details.");
        }

        TravelPlan plan = travelPlanRepository.findById(travelPlan.getId()).orElseThrow(() ->
                new NotFoundException("Travel plan not found."));

        if (!Objects.equals(plan.getAccountId(), tokenService.getUserAccountId())) {
            throw new UnauthorizedException("Unauthorized to update travel plan");
        }

        return travelPlanRepository.save(travelPlan);
    }

    public List<TravelPlan> findMostRecentPublicTravelPlan(int limit) throws NotFoundException {
        Pageable pageable = Pageable.ofSize(limit);
        List<TravelPlan> plans = travelPlanRepository.findRecentPublicTravelPlans(pageable);
        for (TravelPlan plan : plans) {
            int postId = postService.findPostIdByTravelPlanId(plan.getId());
            plan.setPost(postService.findPostByIdIncludingAllFields(postId));
        }
        return plans;
    }

    public Long getNumberOfLikesOnPostByTravelPlanId(Integer planId) throws NotFoundException {
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

    public List<TravelPlan> findTravelPlansFromFollowingList() {
        Integer id = tokenService.getUserAccountId();
        List<Follow> followList = followRepository.findByFollower(id);
        List<Integer> idList = followList.stream()
                .map(follow -> follow.getId().getFollowing())
                .toList();
        List<TravelPlan> plans = travelPlanRepository.findByAccountIdIn(idList);
        for (TravelPlan plan : plans) {
            try {
                int postId = postService.findPostIdByTravelPlanId(plan.getId());
                plan.setPost(postService.findPostByIdIncludingAllFields(postId));
            } catch (Exception e) {
            }
        }
        return plans;
    }
}
