package com.revature.globetrotters.entity;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class PostComment {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "commented_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date commentedDate;

    @ManyToOne
    @JoinColumn(name = "commented_on", nullable = false)
    private Post post;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @ManyToOne
    @JoinColumn(name = "made_by", nullable = false)
    private UserAccount userAccount;

    public PostComment(Date commentedDate, Post post, String content, UserAccount userAccount) {
        this.commentedDate = commentedDate;
        this.post = post;
        this.content = content;
        this.userAccount = userAccount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Date getDateCommented() {
        return commentedDate;
    }

    public void setDateCommented(Date commentedDate) {
        this.commentedDate = commentedDate;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }
}
