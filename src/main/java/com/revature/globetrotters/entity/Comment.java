package com.revature.globetrotters.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "post_comment")
public class Comment {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "commented_date", nullable = false)
    private Date commentedDate;
    @Column(name = "commented_on", nullable = false)
    private Integer postId;
    @Column(name = "content", nullable = false, length = 1000)
    private String content;
    @Column(name = "made_by", nullable = false)
    private Integer userId;
    @Transient
    private String username;

    public Comment() {
    }

    public Comment(Date commentedDate, Integer postId, String content, Integer userId) {
        this.commentedDate = commentedDate;
        this.postId = postId;
        this.content = content;
        this.userId = userId;
    }

    public Comment(Integer id, Date commentedDate, Integer postId, String content, Integer userId) {
        this.id = id;
        this.commentedDate = commentedDate;
        this.postId = postId;
        this.content = content;
        this.userId = userId;
    }

    public Comment(String username, Integer userId, String content, Integer postId, Date commentedDate, Integer id) {
        this.username = username;
        this.userId = userId;
        this.content = content;
        this.postId = postId;
        this.commentedDate = commentedDate;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCommentedDate() {
        return commentedDate;
    }

    public void setCommentedDate(Date commentedDate) {
        this.commentedDate = commentedDate;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment that = (Comment) o;
        return Objects.equals(id, that.id) && Objects.equals(commentedDate, that.commentedDate) &&
                Objects.equals(postId, that.postId) && Objects.equals(content, that.content) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, commentedDate, postId, content, userId);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", commentedDate=" + commentedDate +
                ", postId=" + postId +
                ", content='" + content + '\'' +
                ", userId=" + userId +
                '}';
    }
}
