package com.revature.globetrotters.security;

import com.revature.globetrotters.consts.JwtConsts;
import com.revature.globetrotters.enums.AccountRole;
import com.revature.globetrotters.enums.PublicUrl;
import com.revature.globetrotters.service.AuthenticationTokenService;
import com.revature.globetrotters.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private AuthenticationTokenService authenticationTokenService;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws IOException {
        try {
            String requestURI = request.getRequestURI();
            if (PublicUrl.isPublicUrl(requestURI)) {
                logger.info("{} is a public URL.", requestURI);
                filterChain.doFilter(request, response);
                return;
            }

            final String token = request.getHeader(JwtConsts.AUTHORIZATION);
            logger.info("Token received: {}", token);
            if (token == null) {
                logger.info("Filtered null token.");
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            final String username = JwtUtil.extractSubjectFromToken(token);
            final AccountRole role = AccountRole.valueOf(
                    (String) JwtUtil.extractValueFromTokenByKey(token, JwtConsts.ACCOUNT_ROLE));
            UserAuthenticationToken authenticationToken;
            switch (role) {
                case Moderator:
                    authenticationToken = authenticationTokenService.getModeratorTokenByUsername(username);
                    break;
                default:
                    authenticationToken = authenticationTokenService.getUserTokenByUsername(username);
                    break;
            }
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
            logger.info("Authentication for {} {} successful.", role, username);
        } catch (Exception e) {
            logger.info(e.getMessage());
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
