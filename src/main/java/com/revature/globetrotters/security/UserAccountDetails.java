package com.revature.globetrotters.security;

import com.revature.globetrotters.enums.AccountRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserAccountDetails implements UserDetails {
    private final String username;
    private final String password;
    private final Integer userAccountId;
    private final AccountRole role;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserAccountDetails(String username, String password, Integer userAccountId, AccountRole role,
                              Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.userAccountId = userAccountId;
        this.role = role;
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

    public AccountRole getRole() {
        return role;
    }
}
