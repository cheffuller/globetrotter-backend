package com.revature.globetrotters.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "follow_request")
public class FollowRequest {
    @Column(name = "follower")
    private Integer follower;
    @Column(name = "following")
    private Integer following;

    public FollowRequest() {
    }

    public FollowRequest(Integer follower, Integer following) {
        this.follower = follower;
        this.following = following;
    }

    public Integer getFollower() {
        return follower;
    }

    public void setFollower(Integer follower) {
        this.follower = follower;
    }

    public Integer getFollowing() {
        return following;
    }

    public void setFollowing(Integer following) {
        this.following = following;
    }
}
