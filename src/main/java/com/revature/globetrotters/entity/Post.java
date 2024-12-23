package com.revature.globetrotters.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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

}
