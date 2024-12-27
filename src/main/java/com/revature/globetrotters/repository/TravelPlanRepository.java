package com.revature.globetrotters.repository;

import com.revature.globetrotters.entity.TravelPlan;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelPlanRepository extends JpaRepository<TravelPlan, Integer> {
    List<TravelPlan> findAll();

    @Query("SELECT tp FROM TravelPlan tp WHERE tp.id = :planId")
    TravelPlan getTravelPlanById(Integer planId);

    @Query("SELECT tp FROM TravelPlan tp WHERE tp.accountId = :accountId")
    List<TravelPlan> getTravelPlansByAccountId(Integer accountId);

    @Query("SELECT tp FROM TravelPlan tp WHERE tp.accountId = :accountId AND tp.isPublished = :isPublished")
    List<TravelPlan> findByAccountIdAndIsPublished(@Param("accountId") Integer accountId, @Param("isPublished") boolean isPublished);

    @Query("SELECT tp FROM TravelPlan tp WHERE tp.accountId = :accountId AND tp.isFavorited = :isFavorited")
    List<TravelPlan> findByAccountIdAndIsFavorited(@Param("accountId") Integer accountId, @Param("isFavorited") boolean isFavorited);

    @Query("""
            SELECT tp 
            FROM TravelPlan tp
            JOIN UserProfile up 
            ON tp.accountId = up.accountId
            WHERE tp.isPublished = true
            AND up.isPrivate = false""")
    List<TravelPlan> findRecentPublicTravelPlans(Pageable pageable);
}
