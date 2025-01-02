package com.revature.globetrotters.utils;

import com.revature.globetrotters.enums.AccountRole;
import com.revature.globetrotters.security.UserAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static boolean isModerator() {
        UserAuthenticationToken token = (UserAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return token.getAccountRole().equals(AccountRole.Moderator);
    }

    public static boolean userIdMatchAuthentication(int userId) {
        UserAuthenticationToken token = (UserAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return userId == token.getUserAccountId();
    }
}
