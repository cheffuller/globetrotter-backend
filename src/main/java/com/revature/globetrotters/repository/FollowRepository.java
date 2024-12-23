package com.revature.globetrotters.repository;

import com.revature.globetrotters.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Integer> {
    @Query("")
    List<Follow> findFollowerAndFollowingByFollower(int follower);
}
