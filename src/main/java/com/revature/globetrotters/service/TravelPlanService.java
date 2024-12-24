package com.revature.globetrotters.service;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.revature.globetrotters.entity.TravelPlan;

import com.revature.globetrotters.repository.TravelPlanRepository;

@Service
public class TravelPlanService {
    TravelPlanRepository travelPlanRepository;

    public TravelPlanService(TravelPlanRepository travelPlanRepository) {
        this.travelPlanRepository = travelPlanRepository;
    }

    @Transactional
    public TravelPlan createTravelPlan(TravelPlan travelPlan) {
        return travelPlanRepository.save(travelPlan);
    }
}
