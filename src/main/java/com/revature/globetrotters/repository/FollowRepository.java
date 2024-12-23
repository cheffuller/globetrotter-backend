package com.revature.globetrotters.repository;

import com.revature.globetrotters.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Integer> {
    @Query("SELECT f.id.follower, f.id.following FROM Follow f WHERE f.id.follower = :follower")
    List<Follow> findFollowerAndFollowingByFollower(@Param("follower") Integer follower);

    @Query("SELECT f.id.follower, f.id.following FROM Follow f WHERE f.id.following = :following")
    List<Follow> findFollowerAndFollowingByFollowing(@Param("following") Integer following);
}