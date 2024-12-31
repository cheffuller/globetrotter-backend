package com.revature.globetrotters.service;

import com.revature.globetrotters.security.CustomerAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    public Integer getUserAccountId() {
        return ((CustomerAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getUserAccountId();
    }
}
