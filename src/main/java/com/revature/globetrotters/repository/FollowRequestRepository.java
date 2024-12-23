package com.revature.globetrotters.repository;

import com.revature.globetrotters.entity.Follow;
import com.revature.globetrotters.entity.FollowRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRequestRepository extends JpaRepository<FollowRequest, Integer> {
    @Query("SELECT f.id.follower, f.id.following FROM FollowRequest f WHERE f.id.follower = :follower")
    List<Follow> findFollowerAndFollowingByFollower(@Param("follower") int follower);

    @Query("SELECT f.id.follower, f.id.following FROM FollowRequest f WHERE f.id.following = :following")
    List<Follow> findFollowerAndFollowingByFollowing(@Param("following") int following);
}
