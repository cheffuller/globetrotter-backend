package com.revature.globetrotters.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Transient;

@Entity
@Table(name = "post")
public class Post {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "created_at")
    @CreationTimestamp
    private Date postedDate;
    @Column(name = "travel_plan")
    private int travelPlanId;
    @Transient
    private String username;
    @Transient
    private Long numberOfLikes;
    @Transient
    private List<Comment> comments;
    @Transient
    private List<TravelPlanLocation> locations;

    public Post() {
    }

    public Post(int travelPLanId) {
        this.travelPlanId = travelPLanId;
    }

    public Post(Date postedDate, int travelPlanId) {
        this.postedDate = postedDate;
        this.travelPlanId = travelPlanId;
    }

    public Post(int id, Date postedDate, int travelPlanId) {
        this.id = id;
        this.postedDate = postedDate;
        this.travelPlanId = travelPlanId;
    }

    public Post(int id, Date postedDate, int travelPlanId, String username) {
        this.id = id;
        this.postedDate = postedDate;
        this.travelPlanId = travelPlanId;
        this.username = username;
    }

    public Post(int id, Date postedDate, int travelPlanId, String username, Long numberOfLikes,
                List<Comment> comments, List<TravelPlanLocation> locations) {
        this.id = id;
        this.postedDate = postedDate;
        this.travelPlanId = travelPlanId;
        this.username = username;
        this.numberOfLikes = numberOfLikes;
        this.comments = comments;
        this.locations = locations;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public int getTravelPlanId() {
        return travelPlanId;
    }

    public void setTravelPlanId(int travelPlanId) {
        this.travelPlanId = travelPlanId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(Long numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<TravelPlanLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<TravelPlanLocation> locations) {
        this.locations = locations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id && travelPlanId == post.travelPlanId && Objects.equals(postedDate, post.postedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, postedDate, travelPlanId);
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", postedDate=" + postedDate +
                ", travelPlanId=" + travelPlanId +
                '}';
    }
}
