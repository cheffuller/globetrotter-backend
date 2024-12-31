package com.revature.globetrotters.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomerAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final Integer userAccountId;

    public CustomerAuthenticationToken(Object principal, Object credentials, Integer userAccountId) {
        super(principal, credentials);
        this.userAccountId = userAccountId;
    }

    public CustomerAuthenticationToken(Object principal, Object credentials, Integer userAccountId,
                                       Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.userAccountId = userAccountId;
    }

    public Integer getUserAccountId() {
        return userAccountId;
    }
}
