package com.revature.globetrotters.repository;

import com.revature.globetrotters.entity.TravelPlanLocation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelPlanLocationRepository extends JpaRepository<TravelPlanLocation, Integer> {
    @Query("SELECT tpl FROM TravelPlanLocation tpl WHERE tpl.travelPlanId = :travelPlanId")
    List<TravelPlanLocation> findLocationsByTravelPlanId(@Param("travelPlanId") int travelPlanId);
}
