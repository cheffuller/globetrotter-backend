package com.revature.globetrotters.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "post_comment")
public class PostComment {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "commented_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date commentedDate;

    @Column(name = "commented_on", nullable = false)
    private Integer postId;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Column(name = "made_by", nullable = false)
    private Integer userAccountId;

    public PostComment() {
    }

    public PostComment(Integer id, Date commentedDate, Integer postId, String content, Integer userAccountId) {
        this.id = id;
        this.commentedDate = commentedDate;
        this.postId = postId;
        this.content = content;
        this.userAccountId = userAccountId;
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

    public Integer getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(Integer userAccountId) {
        this.userAccountId = userAccountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostComment that = (PostComment) o;
        return Objects.equals(id, that.id) && Objects.equals(commentedDate, that.commentedDate) && Objects.equals(postId, that.postId) && Objects.equals(content, that.content) && Objects.equals(userAccountId, that.userAccountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, commentedDate, postId, content, userAccountId);
    }

    @Override
    public String toString() {
        return "PostComment{" +
                "id=" + id +
                ", commentedDate=" + commentedDate +
                ", postId=" + postId +
                ", content='" + content + '\'' +
                ", userAccountId=" + userAccountId +
                '}';
    }
}
