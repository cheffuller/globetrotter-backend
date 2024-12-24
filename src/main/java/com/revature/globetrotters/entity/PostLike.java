package com.revature.globetrotters.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "post_like")
public class PostLike {
    @EmbeddedId
    private PostLikeId postLikeId;

    public PostLike() {
    }

    public PostLike(Integer postId, Integer userId) {
        postLikeId = new PostLikeId(postId, userId);
    }

    public PostLike(PostLikeId postLikeId) {
        this.postLikeId = postLikeId;
    }

    public PostLikeId getPostLikeId() {
        return postLikeId;
    }

    public void setPostLikeId(PostLikeId postLikeId) {
        this.postLikeId = postLikeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostLike postLike = (PostLike) o;
        return Objects.equals(postLikeId, postLike.postLikeId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(postLikeId);
    }

    @Override
    public String toString() {
        return "PostLike{" +
                "postLikeId=" + postLikeId +
                '}';
    }

    @Embeddable
    public static class PostLikeId implements Serializable {
        @Column(name = "post_id")
        private Integer postId;
        @Column(name = "user_id")
        private Integer userId;

        public PostLikeId() {
        }

        public PostLikeId(Integer postId, Integer userId) {
            this.postId = postId;
            this.userId = userId;
        }

        public Integer getPostId() {
            return postId;
        }

        public void setPostId(Integer postId) {
            this.postId = postId;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PostLikeId that = (PostLikeId) o;
            return Objects.equals(postId, that.postId) && Objects.equals(userId, that.userId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(postId, userId);
        }

        @Override
        public String toString() {
            return "PostLikeId{" +
                    "postId=" + postId +
                    ", userId=" + userId +
                    '}';
        }
    }

}
