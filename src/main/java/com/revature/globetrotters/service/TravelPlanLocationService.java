package com.revature.globetrotters.service;

import com.revature.globetrotters.entity.Collaborator;
import com.revature.globetrotters.entity.TravelPlan;
import com.revature.globetrotters.entity.TravelPlanLocation;
import com.revature.globetrotters.exception.BadRequestException;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.exception.UnauthorizedException;
import com.revature.globetrotters.repository.CollaboratorRepository;
import com.revature.globetrotters.repository.TravelPlanLocationRepository;
import com.revature.globetrotters.repository.TravelPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public TravelPlanLocation updateTravelPlanLocation(TravelPlanLocation location) throws NotFoundException, UnauthorizedException, BadRequestException {
        TravelPlanLocation locationFound = travelPlanLocationRepository.findById(location.getId())
                .orElseThrow(() -> new NotFoundException("Travel plan location not found."));

        TravelPlan plan = travelPlanRepository.findById(locationFound.getTravelPlanId()).orElseThrow(() ->
                new NotFoundException("Travel plan not found."));

        if (isNotACollaborator(plan.getId())) {
            throw new UnauthorizedException("User is unauthorized to create a location for this travel plan.");
        }

        if (isInvalidLocation(location)) {
            throw new BadRequestException("Invalid location details.");
        }

        return travelPlanLocationRepository.save(location);
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
