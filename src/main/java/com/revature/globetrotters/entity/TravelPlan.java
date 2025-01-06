package com.revature.globetrotters.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "travel_plan")
public class TravelPlan {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "account_id")
    private Integer accountId;
    @Column(name = "is_favorited")
    private Boolean isFavorited;
    @Column(name = "is_published")
    private Boolean isPublished;
    @Transient
    Post post;

    public TravelPlan() {

    }

    public TravelPlan(Integer id, Integer accountId, Boolean isFavorited, Boolean isPublished) {
        this.id = id;
        this.accountId = accountId;
        this.isFavorited = isFavorited;
        this.isPublished = isPublished;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Boolean getIsFavorited() {
        return isFavorited;
    }

    public void setIsFavorited(Boolean isFavorited) {
        this.isFavorited = isFavorited;
    }

    public Boolean getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TravelPlan that = (TravelPlan) o;
        return Objects.equals(id, that.id) && Objects.equals(accountId, that.accountId)
                && Objects.equals(isFavorited, that.isFavorited) && Objects.equals(isPublished, that.isPublished);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountId, isFavorited, isPublished);
    }

    @Override
    public String toString() {
        return "TravelPlan{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", isFavorited=" + isFavorited +
                ", isPublished=" + isPublished +
                '}';
    }
}