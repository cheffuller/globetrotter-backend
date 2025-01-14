package com.revature.globetrotters.service;

import com.revature.globetrotters.controller.UserAccountController;
import com.revature.globetrotters.entity.Collaborator;
import com.revature.globetrotters.entity.TravelPlan;
import com.revature.globetrotters.entity.TravelPlanLocation;
import com.revature.globetrotters.exception.BadRequestException;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.exception.UnauthorizedException;
import com.revature.globetrotters.repository.CollaboratorRepository;
import com.revature.globetrotters.repository.TravelPlanLocationRepository;
import com.revature.globetrotters.repository.TravelPlanRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;




@Service
public class TravelPlanLocationService {
    @Autowired
    private CollaboratorRepository collaboratorRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private TravelPlanRepository travelPlanRepository;
    @Autowired
    private TravelPlanLocationRepository travelPlanLocationRepository;
    private static final Logger logger = LoggerFactory.getLogger(TravelPlanLocationService.class);


    public TravelPlanLocation createTravelPlanLocation(TravelPlanLocation location)
            throws BadRequestException, UnauthorizedException {
        TravelPlan plan = travelPlanRepository.findById(location.getTravelPlanId()).orElseThrow(() ->
                new BadRequestException(String.format(
                        "Travel plan with ID %d does not exist",
                        location.getTravelPlanId()
                ))
        );

        if (isNotACollaborator(plan.getId())) {
            throw new UnauthorizedException("User is unauthorized to create a location for this travel plan.");
        }

        if (isInvalidLocation(location)) {
            throw new BadRequestException("Invalid location details.");
        }

        return travelPlanLocationRepository.save(location);
    }

    public List<TravelPlanLocation> getTravelPlanLocationsByTravelPlanId(int travelPlanId) throws NotFoundException {
        if (!travelPlanRepository.existsById(travelPlanId)) {
            throw new NotFoundException(String.format("Travel plan with ID %d does not exist", travelPlanId));
        }
        return travelPlanLocationRepository.findAllByTravelPlanId(travelPlanId);
    }

    public TravelPlanLocation getTravelPlanLocationById(int locationId) throws NotFoundException {
        return travelPlanLocationRepository.findById(locationId).orElseThrow(() ->
                new NotFoundException(String.format("Travel plan location with ID %d not found", locationId)));
    }

    public TravelPlanLocation getTravelPlanLocationByIdAndTravelPlanId(int travelPlanId, int id) throws NotFoundException {
        return travelPlanLocationRepository.findByTravelPlanIdAndId(travelPlanId, id)
                .orElseThrow(() -> new NotFoundException(String.format(
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

    public List<TravelPlanLocation> updateTravelPlanLocations(int travelPlanId, List<TravelPlanLocation> locations) throws NotFoundException, UnauthorizedException, BadRequestException {
        logger.info("Current Locations:");
        for(TravelPlanLocation location : locations) {
            logger.info(location.toString());
        }
        List<TravelPlanLocation> locationsFound = travelPlanLocationRepository.findAllByTravelPlanId(travelPlanId);
        logger.info("Locations found");
        for(TravelPlanLocation location : locationsFound) {
            logger.info(location.toString());
        }

        TravelPlan plan = travelPlanRepository.findById(travelPlanId).orElseThrow(() ->
                new NotFoundException("Travel plan not found."));

        if (isNotACollaborator(plan.getId())) {
            throw new UnauthorizedException("User is unauthorized to create a location for this travel plan.");
        }

        for(TravelPlanLocation existingLocation : locationsFound) {
            boolean isPresent = false;
            for(TravelPlanLocation location : locations) {
                if(location.getId() != null && location.getId().equals(existingLocation.getId())) {
                    isPresent = true;
                    break;
                }
            }
            if(!isPresent) {
                travelPlanLocationRepository.deleteById(existingLocation.getId());
            }
        }

        for(TravelPlanLocation location : locations) {
            if(location.getId() == null || location.getId() <= 0) {
                location.setTravelPlanId(travelPlanId);
                travelPlanLocationRepository.save(location);
            } else {
                for (TravelPlanLocation existing : locationsFound) {
                    if (existing.getId().equals(location.getId())) {
                        existing.setCity(location.getCity());
                        existing.setCountry(location.getCountry());
                        existing.setStartDate(location.getStartDate());
                        existing.setEndDate(location.getEndDate());
                        travelPlanLocationRepository.save(existing);
                        break;
                    }
                }
            }
        }

        return travelPlanLocationRepository.findAllByTravelPlanId(travelPlanId);
    }

    public void deleteTravelPlanLocation(int travelPlanId, int locationId) throws NotFoundException, UnauthorizedException {

        TravelPlanLocation location = travelPlanLocationRepository.findById(locationId)
                .orElseThrow(() -> new NotFoundException(String.format("Travel plan location %d not found.", locationId)));

        TravelPlan plan = travelPlanRepository.findById(travelPlanId).orElseThrow(() ->
                new NotFoundException("Travel plan not found."));

        if (isNotACollaborator(plan.getId())) {
            throw new UnauthorizedException("User is unauthorized to delete a location for this travel plan.");
        }
        System.out.println("Deleting location");
        travelPlanLocationRepository.deleteById(locationId);
    }

    private boolean isInvalidLocation(TravelPlanLocation location) {
        return location.getCity() == null ||
                location.getCity().trim().isEmpty() ||
                location.getCountry() == null ||
                location.getCountry().trim().isEmpty() ||
                location.getStartDate() == null ||
                location.getEndDate() == null ||
                location.getEndDate().before(location.getStartDate());
    }

    private boolean isNotACollaborator(int planId) {
        List<Collaborator> collaborators = collaboratorRepository.findAllByPlanId(planId);
        return collaborators.stream()
                .filter(collaborator ->
                        Objects.equals(collaborator.getId().getCollaboratorId(), tokenService.getUserAccountId())
                ).toList()
                .isEmpty();
    }
}
