package com.revature.globetrotters.service;

import com.revature.globetrotters.entity.TravelPlan;
import com.revature.globetrotters.entity.TravelPlanLocation;
import com.revature.globetrotters.repository.TravelPlanLocationRepository;
import com.revature.globetrotters.repository.TravelPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TravelPlanService {
    @Autowired
    TravelPlanRepository travelPlanRepository;
    @Autowired
    TravelPlanLocationRepository travelPlanLocationRepository;

    public TravelPlanService(TravelPlanRepository travelPlanRepository) {
        this.travelPlanRepository = travelPlanRepository;
    }

    public TravelPlan createTravelPlan(TravelPlan travelPlan) {
        return travelPlanRepository.save(travelPlan);
    }

    public TravelPlan getTravelPlanById(Integer travelPlanId) {
        return travelPlanRepository.getTravelPlanById(travelPlanId);
    }

    public TravelPlanLocation updateTravelPlan(TravelPlanLocation travelPlan) {
        Optional<TravelPlanLocation> existingTravelPlan = travelPlanLocationRepository.findById(travelPlan.getId());
        System.out.println(existingTravelPlan);
        if (existingTravelPlan == null) {
            throw new IllegalArgumentException("Travel plan not found");
        }

        TravelPlanLocation updatedTravelPlan = existingTravelPlan.get();

        if (updatedTravelPlan.getCity().isEmpty() || updatedTravelPlan.getCountry().isEmpty() || updatedTravelPlan.getStartDate() == null || updatedTravelPlan.getEndDate() == null) {
            throw new IllegalArgumentException("Invalid travel plan location");
        }

        return travelPlanLocationRepository.save(updatedTravelPlan);
    }

    public void deleteTravelPlan(Integer travelPlanId) {
        if (travelPlanRepository.getTravelPlanById(travelPlanId) == null) {
            throw new IllegalArgumentException("Travel plan not found");
        }
        travelPlanRepository.deleteById(travelPlanId);
    }

    public List<TravelPlan> findMostRecentPublicTravelPlan(int limit) {
        Pageable pageable = Pageable.ofSize(limit);
        return travelPlanRepository.findRecentPublicTravelPlans(pageable);
    }
}
