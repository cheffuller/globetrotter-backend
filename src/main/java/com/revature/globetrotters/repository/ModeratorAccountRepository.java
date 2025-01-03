package com.revature.globetrotters.repository;

import com.revature.globetrotters.entity.ModeratorAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModeratorAccountRepository extends JpaRepository<ModeratorAccount, Integer> {
    Optional<ModeratorAccount> findByUsername(String username);
}
