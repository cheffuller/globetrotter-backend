package com.revature.globetrotters.service;

import com.revature.globetrotters.entity.BannedUser;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.repository.BannedUserRepository;
import com.revature.globetrotters.repository.CommentRepository;
import com.revature.globetrotters.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModeratorService {
    @Autowired
    private BannedUserRepository bannedUserRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;

    public void banUser(int userId) throws NotFoundException {
        if (!userAccountRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with ID %d does not exist.", userId));
        }
        bannedUserRepository.save(new BannedUser(userId));
    }

    public void unbanUser(int userId) throws NotFoundException {
        if (!bannedUserRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with ID %d does not exist.", userId));
        }
        bannedUserRepository.delete(new BannedUser(userId));
    }

    public void deleteComment(int commentId) throws NotFoundException {
        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundException(String.format("Comment with ID %d does not exist.", commentId));
        }
        commentRepository.deleteById(commentId);
    }
}
