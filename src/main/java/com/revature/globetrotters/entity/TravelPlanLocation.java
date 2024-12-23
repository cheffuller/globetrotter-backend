package com.revature.globetrotters.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "travel_plan_location")
public class TravelPlanLocation {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "travel_plan_id")
    private Integer travelPlanId;

    public TravelPlanLocation(Integer id, String city, String country, Date endDate, Date startDate, Integer travelPlanId) {
        this.id = id;
        this.city = city;
        this.country = country;
        this.endDate = endDate;
        this.startDate = startDate;
        this.travelPlanId = travelPlanId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Integer getTravelPlanId() {
        return travelPlanId;
    }

    public void setTravelPlanId(Integer travelPlanId) {
        this.travelPlanId = travelPlanId;
    }
    

}
