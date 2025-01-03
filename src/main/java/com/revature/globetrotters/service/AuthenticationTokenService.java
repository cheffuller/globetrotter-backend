package com.revature.globetrotters.service;

import com.revature.globetrotters.entity.ModeratorAccount;
import com.revature.globetrotters.entity.UserAccount;
import com.revature.globetrotters.enums.AccountRole;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.repository.ModeratorAccountRepository;
import com.revature.globetrotters.repository.UserAccountRepository;
import com.revature.globetrotters.security.UserAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationTokenService {
    @Autowired
    private ModeratorAccountRepository moderatorAccountRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;

    public UserAuthenticationToken getUserTokenByUsername(String username) throws NotFoundException {
        UserAccount userAccount = userAccountRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException(String.format("User with username %s not found.", username)));
        return new UserAuthenticationToken(
                userAccount.getUsername(),
                userAccount.getPassword(),
                userAccount.getId(),
                AccountRole.Customer,
                List.of()
        );
    }

    public UserAuthenticationToken getModeratorTokenByUsername(String username) throws NotFoundException {
        ModeratorAccount moderatorAccount = moderatorAccountRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException(String.format("Moderator with username %s not found.", username)));
        return new UserAuthenticationToken(
                moderatorAccount.getUsername(),
                moderatorAccount.getPassword(),
                moderatorAccount.getId(),
                AccountRole.Moderator,
                List.of()
        );
    }
}
