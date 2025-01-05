package com.revature.globetrotters.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.revature.globetrotters.entity.UserProfile;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
    @Query("""
            SELECT up 
            FROM UserProfile up
            JOIN UserAccount ua
            ON ua.id = up.accountId
            WHERE ua.username = :username""")
    Optional<UserProfile> findByUsername(@Param("username") String username);
}
