package com.revature.globetrotters.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "travel_plan")
public class TravelPlan {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer planId;
    @Column(name = "account_id")
    private Integer accountId;
    @Column(name = "is_favorited")
    private Boolean isFavorited;
    @Column(name = "is_published")
    private Boolean isPublished;

    public TravelPlan(){

    }

    public TravelPlan(Integer planId, Integer accountId, Boolean isFavorited, Boolean isPublished){
        this.planId = planId;
        this.accountId = accountId;
        this.isFavorited = isFavorited;
        this.isPublished = isPublished;
    }

    public Integer getPlanId(){
        return planId;
    }

    public void setPlanId(Integer planId){
        this.planId = planId;
    }

    public Integer getAccountId(){
        return accountId;
    }

    public void setAccountId(Integer accountId){
        this.accountId = accountId;
    }

    public Boolean getIsFavorited(){
        return isFavorited;
    }

    public void setIsFavorited(Boolean isFavorited){
        this.isFavorited = isFavorited;
    }

    public Boolean getIsPublished(){
        return isPublished;
    }

    public void setIsPublished(Boolean isPublished){
        this.isPublished = isPublished;
    }

}   