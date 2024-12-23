package com.revature.globetrotters.repository;

import com.revature.globetrotters.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Integer> {
    @Query("SELECT f.follower, f.followwing FROM Follow f WHERE f.follower = :follower")
    List<Follow> findFollowerAndFollowingByFollower(@Param("follower") int follower);

    @Query("SELECT f.follower, f.followwing FROM Follow f WHERE f.following = :following")
    List<Follow> findFollowerAndFollowingByFollowing(@Param("following") int following);
}