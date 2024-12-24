package com.revature.globetrotters.service;

import org.springframework.stereotype.Service;

import com.revature.globetrotters.entity.ModeratorAccount;
import com.revature.globetrotters.repository.ModeratorAccountRepository;

@Service
public class ModeratorAccountService {
private final ModeratorAccountRepository moderatorAccountRepository;

public ModeratorAccountService(ModeratorAccountRepository moderatorAccountRepository){
    this.moderatorAccountRepository = moderatorAccountRepository;
}

}
