package com.revature.globetrotters.repository;

import com.revature.globetrotters.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Follow.FollowId> {
    @Query("SELECT f FROM Follow f WHERE f.id.follower = :follower")
    List<Follow> findByFollower(@Param("follower") Integer follower);

    @Query("SELECT f FROM Follow f WHERE f.id.following = :following")
    List<Follow> findByFollowing(@Param("following") Integer following);
}