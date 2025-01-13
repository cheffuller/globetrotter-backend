package com.revature.globetrotters.util;

import com.revature.globetrotters.consts.JwtConsts;
import com.revature.globetrotters.enums.AccountRole;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.security.UserAuthenticationToken;
import com.revature.globetrotters.service.AuthenticationTokenService;
import com.revature.globetrotters.utils.JwtUtil;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;

public class SecurityUtils {
    public static void setUpSecurityContextHolder(String username, AuthenticationTokenService authenticationTokenService)
            throws NotFoundException {
        UserAuthenticationToken authentication = authenticationTokenService.getUserTokenByUsername(username);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static String getWebToken(String username, int id) {
        return JwtUtil.generateTokenFromUserName(username, Map.of(
                JwtConsts.ACCOUNT_ROLE, AccountRole.Customer.getRole(),
                JwtConsts.ACCOUNT_ID, id
        ));
    }

    public static String getWebToken() {
        return JwtUtil.generateTokenFromUserName("john_doe", Map.of(
                JwtConsts.ACCOUNT_ROLE, AccountRole.Customer.getRole(),
                JwtConsts.ACCOUNT_ID, "1"
        ));
    }

}
