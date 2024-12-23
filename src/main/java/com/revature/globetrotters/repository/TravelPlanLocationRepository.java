package com.revature.globetrotters.repository;

import com.revature.globetrotters.entity.TravelPlanLocation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelPlanLocationRepository extends JpaRepository<TravelPlanLocation, Integer> {
    @Query("SELECT t.city, t.country, t.endDate, t.startDate FROM TravelPlanLocation t WHERE t.travelPlanId = :travelPlanId")
    List<TravelPlanLocation> findLocationsByTravelPlanId(@Param("travelPlanId") int travelPlanId);

    @Query("SELECT t.city,t.country, t.endDate, t.startDate, t.travelPlanId FROM TravelPlanLocation t WHERE t.city = :city AND t.country = :country")
    List<TravelPlanLocation> findLocationsByCityAndCountry(@Param("city") String city, @Param("country") String country);

    @Query("SELECT t.startDate, t.endDate FROM TravelPlanLocation t WHERE t.city = :city AND t.country = :country")
    List<TravelPlanLocation> findDatesByCityAndCountry(@Param("city") String city, @Param("country") String country);

    @Query("SELECT t.startDate, t.endDate FROM TravelPlanLocation t WHERE t.travelPlanId = :travelPlanId")
    List<TravelPlanLocation> findDatesByTravelPlanId(@Param("travelPlanId") int travelPlanId);
}
