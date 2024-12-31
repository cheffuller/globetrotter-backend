package com.revature.globetrotters.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomerDetails implements UserDetails {
    private final String username;
    private final String password;
    private final Integer userAccountId;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomerDetails(String username, String password, Integer userAccountId, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.userAccountId = userAccountId;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public Integer getUserAccountId() {
        return userAccountId;
    }
}
