package com.revature.globetrotters.service;

import com.revature.globetrotters.entity.UserProfile;
import com.revature.globetrotters.exception.BadRequestException;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.repository.UserAccountRepository;
import com.revature.globetrotters.repository.UserProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class UserProfileService {
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
    }

    public UserProfile findUserProfile(String username) throws NotFoundException {
        return userProfileRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException("User profile not found"));
    }
}
