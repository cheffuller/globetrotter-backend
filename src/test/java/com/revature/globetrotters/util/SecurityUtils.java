package com.revature.globetrotters.util;

import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.security.UserAuthenticationToken;
import com.revature.globetrotters.service.AuthenticationTokenService;
import com.revature.globetrotters.utils.JwtUtil;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;

public class SecurityUtils {
    public static void setUpSecurityContextHolder(String username, AuthenticationTokenService authenticationTokenService)
            throws NotFoundException {
        UserAuthenticationToken authentication = authenticationTokenService.getUserTokenByUsername(username);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static String getWebToken(String username) {
        return JwtUtil.generateTokenFromUserName(username, new HashMap<>());
    }

}
