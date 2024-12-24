package com.revature.globetrotters.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "follow_request")
public class FollowRequest {
    @EmbeddedId
    private FollowRequestId id;

    public FollowRequest() {
    }

    public FollowRequest(FollowRequestId id) {
        this.id = id;
    }

    public FollowRequest(int follower, int following) {
        id = new FollowRequestId(follower, following);
    }

    public FollowRequestId getId() {
        return id;
    }

    public void setId(FollowRequestId id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowRequest that = (FollowRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Embeddable
    public static class FollowRequestId implements Serializable {
        @Column(name = "follower")
        private Integer follower;
        @Column(name = "following")
        private Integer following;

        public FollowRequestId() {
        }

        public FollowRequestId(Integer follower, Integer following) {
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FollowRequestId that = (FollowRequestId) o;
            return Objects.equals(follower, that.follower) && Objects.equals(following, that.following);
        }

        @Override
        public int hashCode() {
            return Objects.hash(follower, following);
        }
    }
}
