package com.revature.globetrotters.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "comment_like")
public class CommentLike {
    @EmbeddedId
    private CommentLikeId id;

    public CommentLike() {
    }

    public CommentLike(Integer commentId, Integer userId) {
        id = new CommentLikeId(commentId, userId);
    }

    public CommentLike(CommentLikeId id) {
        this.id = id;
    }

    public CommentLikeId getId() {
        return id;
    }

    public void setId(CommentLikeId id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentLike that = (CommentLike) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CommentLike{" +
                "id=" + id +
                '}';
    }

    @Embeddable
    public static class CommentLikeId implements Serializable {
        @Column(name = "post_id")
        private Integer commentId;
        @Column(name = "user_id")
        private Integer userId;

        public CommentLikeId() {
        }

        public CommentLikeId(Integer commentId, Integer userId) {
            this.commentId = commentId;
            this.userId = userId;
        }

        public Integer getCommentId() {
            return commentId;
        }

        public void setCommentId(Integer commentId) {
            this.commentId = commentId;
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
            CommentLikeId that = (CommentLikeId) o;
            return Objects.equals(commentId, that.commentId) && Objects.equals(userId, that.userId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(commentId, userId);
        }

        @Override
        public String toString() {
            return "CommentLikeId{" +
                    "postId=" + commentId +
                    ", commentId=" + userId +
                    '}';
        }
    }
}
