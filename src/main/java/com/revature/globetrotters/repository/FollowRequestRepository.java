package com.revature.globetrotters.repository;

import com.revature.globetrotters.entity.FollowRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRequestRepository extends JpaRepository<FollowRequest, FollowRequest.FollowRequestId> {
    @Query("SELECT fr FROM FollowRequest fr WHERE fr.id.follower = :follower")
    List<FollowRequest> findByFollower(@Param("follower") Integer follower);

    @Query("SELECT fr FROM FollowRequest fr WHERE fr.id.following = :following")
    List<FollowRequest> findByFollowing(@Param("following") Integer following);
}
