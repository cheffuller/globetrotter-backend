package com.revature.globetrotters.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "post")
public class Post {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "created_at")
    private Date created_at;
    @Column(name = "travel_plan")
    private int travel_plan;
    @Column(name = "posted_date")
    private Date posted_date;

    public Post() {
    }

    public Post(int id, Date created_at, int travel_plan, Date posted_date) {
        this.id = id;
        this.created_at = created_at;
        this.travel_plan = travel_plan;
        this.posted_date = posted_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public int getTravel_plan() {
        return travel_plan;
    }

    public void setTravel_plan(int travel_plan) {
        this.travel_plan = travel_plan;
    }

    public Date getPosted_date() {
        return posted_date;
    }

    public void setPosted_date(Date posted_date) {
        this.posted_date = posted_date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id && travel_plan == post.travel_plan && Objects.equals(created_at, post.created_at)
                && Objects.equals(posted_date, post.posted_date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, created_at, travel_plan, posted_date);
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", created_at=" + created_at +
                ", travel_plan=" + travel_plan +
                ", posted_date=" + posted_date +
                '}';
    }
}
