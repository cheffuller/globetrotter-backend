package com.revature.globetrotters.service;

import com.revature.globetrotters.consts.JwtConsts;
import com.revature.globetrotters.entity.BannedUser;
import com.revature.globetrotters.entity.ModeratorAccount;
import com.revature.globetrotters.enums.AccountRole;
import com.revature.globetrotters.exception.BadRequestException;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.repository.BannedUserRepository;
import com.revature.globetrotters.repository.CommentRepository;
import com.revature.globetrotters.repository.ModeratorAccountRepository;
import com.revature.globetrotters.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class ModeratorService {
    @Autowired
    private BannedUserRepository bannedUserRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ModeratorAccountRepository moderatorAccountRepository;

    public void banUser(int userId) throws NotFoundException {
        if (!moderatorAccountRepository.existsById(userId)) {
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

    public String login(ModeratorAccount account) throws NotFoundException, BadRequestException {
        if (!account.isPasswordValid() || !account.isUsernameValid()) {
            throw new BadRequestException("Username and password are required.");
        }

        Optional<ModeratorAccount> foundAccount = moderatorAccountRepository.findByUsername(account.getUsername());
        if (foundAccount.isEmpty()) {
            throw new NotFoundException(String.format("User with username %s not found.", account.getUsername()));
        }

        if (!passwordEncoder.matches(account.getPassword(), foundAccount.get().getPassword())) {
            throw new BadRequestException("Invalid login credentials." + passwordEncoder.matches(account.getPassword(), foundAccount.get().getPassword()) +
                    ".\nPassword: " + account.getPassword() + ".\nFound password hash: " + foundAccount.get().getPassword());
        }

        return JwtUtil.generateTokenFromUserName(account.getUsername(), Map.of(
                JwtConsts.ACCOUNT_ROLE, AccountRole.Moderator.getRole()
        ));
    }
}
