package com.revature.globetrotters.repository;

import com.revature.globetrotters.entity.TravelPlanLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TravelPlanLocationRepository extends JpaRepository<TravelPlanLocation, Integer> {
    @Query("SELECT tpl FROM TravelPlanLocation tpl WHERE tpl.travelPlanId = :travelPlanId")
    List<TravelPlanLocation> findLocationsByTravelPlanId(@Param("travelPlanId") int travelPlanId);

    @Query("SELECT tpl FROM TravelPlanLocation tpl WHERE tpl.travelPlanId = :travelPlanId AND tpl.id = :locationId")
    Optional<TravelPlanLocation> findLocationByTravelPlanIdAndLocationId(@Param("travelPlanId") int travelPlanId,
                                                                         @Param("locationId") int locationId);

    @NativeQuery("""
            SELECT * 
            FROM travel_plan_location
            WHERE plan_id = :travelPlanId
            OFFSET :offset
            LIMIT 1""")
    Optional<TravelPlanLocation> findNthLocationByTravelPlanId(@Param("travelPlanId") int travelPlanId,
                                                               @Param("offset") int offset);
}
