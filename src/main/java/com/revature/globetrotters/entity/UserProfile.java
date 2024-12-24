package com.revature.globetrotters.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "user_profile")
public class UserProfile {
    @Column(name = "account_id")
    @Id
    private Integer accountId;
    @Column(name = "bio")
    private String bio;
    @Column(name = "display_name")
    private String displayName;
    @Column(name = "is_private")
    private boolean isPrivate;

    public UserProfile() {
    }

    public UserProfile(Integer accountId, String bio, String displayName, boolean isPrivate) {
        this.accountId = accountId;
        this.bio = bio;
        this.displayName = displayName;
        this.isPrivate = isPrivate;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return isPrivate == that.isPrivate && Objects.equals(accountId, that.accountId) && Objects.equals(bio, that.bio)
                && Objects.equals(displayName, that.displayName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, bio, displayName, isPrivate);
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "accountId=" + accountId +
                ", bio='" + bio + '\'' +
                ", displayName='" + displayName + '\'' +
                ", isPrivate=" + isPrivate +
                '}';
    }
}
