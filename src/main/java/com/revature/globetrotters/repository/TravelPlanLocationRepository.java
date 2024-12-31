package com.revature.globetrotters.repository;

import com.revature.globetrotters.entity.TravelPlanLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TravelPlanLocationRepository extends JpaRepository<TravelPlanLocation, Integer> {
    List<TravelPlanLocation> findAllByTravelPlanId(int travelPlanId);

    Optional<TravelPlanLocation> findByTravelPlanIdAndId(int travelPlanId, int id);

    @NativeQuery("""
            SELECT * 
            FROM travel_plan_location
            WHERE travel_plan_id = :travelPlanId
            ORDER BY id ASC
            LIMIT 1 
            OFFSET :offset""")
    Optional<TravelPlanLocation> findByTravelPlanIdAndOffset(@Param("travelPlanId") int travelPlanId,
                                                             @Param("offset") int offset);
}
