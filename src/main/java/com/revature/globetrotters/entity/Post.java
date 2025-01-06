package com.revature.globetrotters.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;
import java.util.Objects;

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
    @JsonProperty("timestamp")
    private Date postedDate;
    @Column(name = "travel_plan")
    private int travelPlanId;

    public Post() {
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
