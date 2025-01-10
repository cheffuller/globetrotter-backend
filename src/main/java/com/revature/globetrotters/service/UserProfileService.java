package com.revature.globetrotters.service;

import com.revature.globetrotters.entity.Follow;
import com.revature.globetrotters.entity.FollowRequest;
import com.revature.globetrotters.entity.UserProfile;
import com.revature.globetrotters.exception.BadRequestException;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.repository.FollowRepository;
import com.revature.globetrotters.repository.FollowRequestRepository;
import com.revature.globetrotters.repository.UserAccountRepository;
import com.revature.globetrotters.repository.UserProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProfileService {
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private FollowRequestRepository followRequestRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserProfileService.class);

    public void updateUserProfile(UserProfile profile) throws NotFoundException, BadRequestException {
        profile.setAccountId(tokenService.getUserAccountId());
        if (!userProfileRepository.existsById(profile.getAccountId())) {
            throw new NotFoundException("User profile not found.");
        }

        if (profile.getDisplayName() == null ||
                profile.getDisplayName().trim().isEmpty()) {
            throw new BadRequestException("Invalid profile details.");
        }

        userProfileRepository.save(profile);

        if (!profile.getIsPrivate()) {
            List<FollowRequest> followRequestList = followRequestRepository.findByFollowing(profile.getAccountId());
            followRequestList.stream().forEach(request -> {
                followRequestRepository.delete(request);
                followRepository.save(new Follow(request.getId().getFollower(), request.getId().getFollowing()));
            });
        }
    }

    public UserProfile findUserProfile(String username) throws NotFoundException {
        return userProfileRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException("User profile not found"));
    }

    public String findDisplayNameFromUsername(String username) throws NotFoundException {
        return userProfileRepository.findDisplayNameFromUsername(username).orElseThrow(() ->
                new NotFoundException(String.format("User profile not found for username %s.", username)));
    }

    public String findDisplayNameFromUserId(Integer userId) throws NotFoundException {
        return userProfileRepository.findDisplayNameFromUserId(userId).orElseThrow(() ->
                new NotFoundException(String.format("User profile not found for userId %d.", userId)));
    }
}
