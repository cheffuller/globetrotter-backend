package com.revature.globetrotters.repository;

import com.revature.globetrotters.entity.TravelPlan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelPlanRepository extends JpaRepository<TravelPlan, Integer> {

    @Query("SELECT tp FROM TravelPlan tp WHERE tp.id = :planId")
    TravelPlan getTravelPlanById(Integer planId);

    @Query("SELECT tp FROM TravelPlan tp WHERE tp.account.id = :accountId")
    List<TravelPlan> getTravelPlansByAccountId(Integer accountId);

    @Query("SELECT * FROM TravelPlan")
    List<TravelPlan> getAllTravelPlans();

    @Query("SELECT tp FROM TravelPlan tp WHERE tp.account.id = :accountId AND tp.isPublished = false")
    List<TravelPlan> findByAccountIdAndIsPublishedFalse(@Param("accountId") Integer accountId);

    @Query("SELECT tp FROM TravelPlan tp WHERE tp.account.id = :accountId AND tp.isPublished = true")
    List<TravelPlan> findByAccountIdAndIsPublishedTrue(@Param("accountId") Integer accountId);

    @Query("SELECT tp FROM TravelPlan tp WHERE tp.account.id = :accountId AND tp.IsFavorited = false")
    List<TravelPlan> findByAccountIdAndIsFavoritedFalse(@Param("accountId") Integer accountId);

    @Query("SELECT tp FROM TravelPlan tp WHERE tp.account.id = :accountId AND tp.IsFavorited = true")
    List<TravelPlan> findByAccountIdAndIsFavoritedTrue(@Param("accountId") Integer accountId);

}
