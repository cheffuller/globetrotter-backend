package com.revature.globetrotters.service;

import com.revature.globetrotters.entity.TravelPlan;
import com.revature.globetrotters.entity.TravelPlanLocation;
import com.revature.globetrotters.exception.BadRequestException;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.repository.TravelPlanLocationRepository;
import com.revature.globetrotters.repository.TravelPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        if (travelPlanLocation.getCity().isEmpty() || travelPlanLocation.getCountry().isEmpty() || travelPlanLocation.getStartDate() == null || travelPlanLocation.getEndDate() == null) {
            throw new BadRequestException("Invalid travel plan location");
        }
        Optional<TravelPlan> travelPlan = travelPlanRepository.findById(travelPlanLocation.getTravelPlanId());
        if (travelPlan.isEmpty()) {
            throw new BadRequestException("Travel plan does not exist");
        } else {
            //we want to set the travel plan location to the travel plan
            return travelPlanLocationRepository.save(travelPlanLocation);
        }
    }

    public List<TravelPlanLocation> getTravelPlanLocationsByTravelPlanId(int travelPlanId) {
        return travelPlanLocationRepository.findAllByTravelPlanId(travelPlanId);
    }

    public TravelPlanLocation getTravelPlanLocationById(int locationId) throws NotFoundException {
        return travelPlanLocationRepository.findById(locationId).orElseThrow(() ->
                new NotFoundException(String.format("Travel plan location with ID %d not found", locationId)));
    }

    public TravelPlanLocation getTravelPlanLocationByIdAndTravelPlanId(int travelPlanId, int id) throws NotFoundException {
        return travelPlanLocationRepository.findByTravelPlanIdAndId(travelPlanId, id).orElseThrow(() ->
                new NotFoundException(String.format(
                        "Travel plan location with ID %d and travel plan ID %d not found",
                        id,
                        travelPlanId
                )));
    }

    public TravelPlanLocation getTravelPlanLocationWithOffsetByTravelPlanId(int travelPlanId, int offset)
            throws NotFoundException {
        return travelPlanLocationRepository.findByTravelPlanIdAndOffset(travelPlanId, offset)
                .orElseThrow(() -> new NotFoundException(String.format(
                        "Travel plan location with offset %d not found for travel plan with ID %d",
                        offset,
                        travelPlanId
                )));
    }
}
