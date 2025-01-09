package com.revature.globetrotters.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.revature.globetrotters.consts.JwtConsts;
import com.revature.globetrotters.entity.BannedUser;
import com.revature.globetrotters.entity.ModeratorAccount;
import com.revature.globetrotters.enums.AccountRole;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.exception.UnauthorizedException;
import com.revature.globetrotters.repository.BannedUserRepository;
import com.revature.globetrotters.repository.CommentRepository;
import com.revature.globetrotters.repository.ModeratorAccountRepository;
import com.revature.globetrotters.repository.UserAccountRepository;
import com.revature.globetrotters.utils.JwtUtil;
import static com.revature.globetrotters.utils.SecurityUtil.isModerator;

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
    @Autowired
    private UserAccountRepository userAccountRepository;

    public void banUser(int userId) throws NotFoundException, UnauthorizedException {
        if (!isModerator()) {
            throw new UnauthorizedException("Unauthorized request to ban user.");
        }
        if (!userAccountRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with ID %d does not exist.", userId));
        }
        bannedUserRepository.save(new BannedUser(userId));
    }

    public void unbanUser(int userId) throws NotFoundException, UnauthorizedException {
        if (!isModerator()) {
            throw new UnauthorizedException("Unauthorized request to ban user.");
        }
        if (!bannedUserRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with ID %d does not exist.", userId));
        }
        bannedUserRepository.delete(new BannedUser(userId));
    }

    public void deleteComment(int commentId) throws NotFoundException, UnauthorizedException {
        if (!isModerator()) {
            throw new UnauthorizedException("Unauthorized request to ban user.");
        }
        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundException(String.format("Comment with ID %d does not exist.", commentId));
        }
        commentRepository.deleteById(commentId);
    }

    public String login(ModeratorAccount account) throws UnauthorizedException {
        if (!account.isPasswordValid() || !account.isUsernameValid()) {
            throw new UnauthorizedException("Username and password are required.");
        }

        ModeratorAccount foundAccount = moderatorAccountRepository.findByUsername(account.getUsername())
                .orElseThrow(() -> new UnauthorizedException(String.format(
                        "User with username %s not found.",
                        account.getUsername()
                )));

        if (!passwordEncoder.matches(account.getPassword(), foundAccount.getPassword())) {
            throw new UnauthorizedException("Invalid login credentials." +
                    passwordEncoder.matches(account.getPassword(), foundAccount.getPassword()) +
                    ".\nPassword: " + account.getPassword() +
                    ".\nFound password hash: " + foundAccount.getPassword());
        }

        return JwtUtil.generateTokenFromUserName(account.getUsername(), Map.of(
                JwtConsts.ACCOUNT_ROLE, AccountRole.Moderator.getRole(),
                JwtConsts.ACCOUNT_ID, foundAccount.toString()
        ));
    }
}
