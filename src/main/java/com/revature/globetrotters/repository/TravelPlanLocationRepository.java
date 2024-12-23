package com.revature.globetrotters.repository;

import com.revature.globetrotters.entity.TravelPlanLocation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/*
 * CREATE TABLE travel_plan_location (
	id INT AUTO_INCREMENT,
    city VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    end_date DATE NOT NULL,
    start_date DATE NOT NULL,
    travel_plan_id INT NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(travel_plan_id) REFERENCES travel_plan(id)
);
 */
@Repository
public interface TravelPlanLocationRepository extends JpaRepository<TravelPlanLocation, Integer> {
    @Query("SELECT t.city, t.country, t.end_date, t.start_date FROM TravelPlanLocation t WHERE t.travel_plan_id = :travel_plan_id")
    List<TravelPlanLocation> findLocationsByTravelPlanId(@Param("travel_plan_id") int travel_plan_id);

    @Query("SELECT t.city,t.country, t.end_date, t.start_date, t.travel_plan_id FROM TravelPlanLocation t WHERE t.city = :city AND t.country = :country")
    List<TravelPlanLocation> findLocationsByCityAndCountry(@Param("city") String city, @Param("country") String country);

    @Query("SELECT t.start_date, t.end_date FROM TravelPlanLocation t WHERE t.city = :city AND t.country = :country")
    List<TravelPlanLocation> findDatesByCityAndCountry(@Param("city") String city, @Param("country") String country);

    @Query("SELECT t.start_date, t.end_date FROM TravelPlanLocation t WHERE t.travel_plan_id = :travel_plan_id")
    List<TravelPlanLocation> findDatesByTravelPlanId(@Param("travel_plan_id") int travel_plan_id);
}
