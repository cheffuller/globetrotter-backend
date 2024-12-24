package com.revature.globetrotters.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "follow")
public class Follow {
    @EmbeddedId
    private FollowId id;

    public Follow() {
    }

    public Follow(FollowId id) {
        this.id = id;
    }

    public Follow(int follower, int following) {
        id = new FollowId(follower, following);
    }

    public FollowId getId() {
        return id;
    }

    public void setId(FollowId id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Follow follow = (Follow) o;
        return Objects.equals(id, follow.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Embeddable
    public static class FollowId implements Serializable {
        @Column(name = "follower")
        private Integer follower;
        @Column(name = "following")
        private Integer following;

        public FollowId() {
        }

        public FollowId(Integer follower, Integer following) {
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
            FollowId followId = (FollowId) o;
            return Objects.equals(follower, followId.follower) && Objects.equals(following, followId.following);
        }

        @Override
        public int hashCode() {
            return Objects.hash(follower, following);
        }
    }
}
