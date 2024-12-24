package com.revature.globetrotters.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.globetrotters.entity.TravelPlan;
import com.revature.globetrotters.entity.TravelPlanLocation;
import com.revature.globetrotters.repository.TravelPlanLocationRepository;
import com.revature.globetrotters.repository.TravelPlanRepository;

@Service
public class TravelPlanService {
    @Autowired
    TravelPlanRepository travelPlanRepository;
    @Autowired
    TravelPlanLocationRepository travelPlanLocationRepository;

    public TravelPlanService(TravelPlanRepository travelPlanRepository){
        this.travelPlanRepository = travelPlanRepository;
    }

    public TravelPlan createTravelPlan(TravelPlan travelPlan){
        return travelPlanRepository.save(travelPlan);
    }

    public TravelPlan getTravelPlanById(Integer travelPlanId){
        return travelPlanRepository.getTravelPlanById(travelPlanId);
    }

    public TravelPlanLocation updateTravelPlan(TravelPlanLocation travelPlan){
        TravelPlanLocation existingTravelPlan = travelPlanLocationRepository.findLocationsByTravelPlanId(travelPlan.getId());
        if (existingTravelPlan == null) {
            throw new IllegalArgumentException("Travel plan not found");
        }
        TravelPlanLocation updatedTravelPlan = existingTravelPlan;
        updatedTravelPlan.setStartDate(travelPlan.getStartDate());
        updatedTravelPlan.setEndDate(travelPlan.getEndDate());
        updatedTravelPlan.setCity(travelPlan.getCity());
        updatedTravelPlan.setCountry(travelPlan.getCountry());

        return travelPlanLocationRepository.save(updatedTravelPlan);
    }

    public void deleteTravelPlan(Integer travelPlanid){
        if (travelPlanRepository.getTravelPlanById(travelPlanid) == null) {
            throw new IllegalArgumentException("Travel plan not found");
        }
        travelPlanRepository.deleteById(travelPlanid);
    }
}
