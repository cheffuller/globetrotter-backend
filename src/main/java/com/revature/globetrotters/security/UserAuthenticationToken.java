package com.revature.globetrotters.security;

import com.revature.globetrotters.enums.AccountRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final Integer userAccountId;
    private final AccountRole accountRole;

    public UserAuthenticationToken(Object principal, Object credentials, Integer userAccountId, AccountRole accountRole) {
        super(principal, credentials);
        this.userAccountId = userAccountId;
        this.accountRole = accountRole;
    }

    public UserAuthenticationToken(Object principal, Object credentials, Integer userAccountId,
                                   AccountRole accountRole, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.userAccountId = userAccountId;
        this.accountRole = accountRole;
    }

    public Integer getUserAccountId() {
        return userAccountId;
    }

    public AccountRole getAccountRole() {
        return accountRole;
    }
}
