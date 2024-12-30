package com.revature.globetrotters.service;

import com.revature.globetrotters.entity.UserAccount;
import com.revature.globetrotters.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerDetailService implements UserDetailsService {
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<UserAccount> userAccount = userAccountRepository.findByUsername(username);
        return new User(
                userAccount.get().getUsername(),
                userAccount.get().getPassword(),
                List.of()
        );
    }
}
