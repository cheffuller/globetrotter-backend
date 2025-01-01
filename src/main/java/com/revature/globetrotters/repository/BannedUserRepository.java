package com.revature.globetrotters.repository;

import com.revature.globetrotters.entity.BannedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannedUserRepository extends JpaRepository<BannedUser, Integer> {
}
