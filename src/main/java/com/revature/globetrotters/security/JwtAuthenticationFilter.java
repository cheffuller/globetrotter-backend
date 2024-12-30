package com.revature.globetrotters.security;

import com.revature.globetrotters.consts.JwtConsts;
import com.revature.globetrotters.repository.UserAccountRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private UserAccountRepository userAccountRepository;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String token = request.getHeader(JwtConsts.AUTHORIZATION);
        logger.info("TOKEN RECEIVED {}", token);
        if (token == null) {
            logger.info("FILTERED");
            filterChain.doFilter(request, response);
            return;
        }
        final String username = JwtUtil.extractSubjectFromToken(token);
        logger.info("USERNAME RECEIVED: {}", username);
        if (userAccountRepository.findByUsername(username).isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
    }
}
