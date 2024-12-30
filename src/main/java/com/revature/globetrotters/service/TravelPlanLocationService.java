package com.revature.globetrotters.service;

import org.springframework.stereotype.Service;
import com.revature.globetrotters.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;

import com.revature.globetrotters.entity.TravelPlan;
import com.revature.globetrotters.entity.TravelPlanLocation;
import com.revature.globetrotters.repository.TravelPlanRepository;
import com.revature.globetrotters.repository.TravelPlanLocationRepository;

import java.util.List;
import java.util.Optional;


@Service
public class TravelPlanLocationService {
    
    @Autowired
    TravelPlanRepository travelPlanRepository;
    @Autowired
    TravelPlanLocationRepository travelPlanLocationRepository;

    public TravelPlanLocationService(TravelPlanLocationRepository travelPlanLocationRepository, TravelPlanRepository travelPlanRepository) {
        this.travelPlanLocationRepository = travelPlanLocationRepository;
        this.travelPlanRepository = travelPlanRepository;
    }

    public TravelPlanLocation createTravelPlanLocation(TravelPlanLocation travelPlanLocation) throws BadRequestException {
        if(travelPlanLocation.getCity().isEmpty() || travelPlanLocation.getCountry().isEmpty() || travelPlanLocation.getStartDate() == null || travelPlanLocation.getEndDate() == null) {
            throw new BadRequestException("Invalid travel plan location");
        }
        Optional<TravelPlan> travelPlan = travelPlanRepository.findById(travelPlanLocation.getTravelPlanId());
        if(travelPlan.isEmpty()) {
            throw new BadRequestException("Travel plan does not exist");
        } else {
            //we want to set the travel plan location to the travel plan
            return travelPlanLocationRepository.save(travelPlanLocation);
        }
    }

    public List<TravelPlanLocation> getTravelPlanLocationsByTravelPlanId(int travelPlanId) {
        return travelPlanLocationRepository.findLocationsByTravelPlanId(travelPlanId);
    }

    public TravelPlanLocation getTravelPlanLocationById(int travelPlanId, int locationId) throws BadRequestException {
        if(travelPlanRepository.findById(travelPlanId).isEmpty()) {
            throw new BadRequestException("Travel plan does not exist");
        } 
        Optional<TravelPlanLocation> travelPlanLocation = travelPlanLocationRepository.findById(locationId);
        if(travelPlanLocation.isEmpty()) {
            throw new BadRequestException("Travel plan location does not exist");
        } else {
            return travelPlanLocation.get();
        }
    }
}
