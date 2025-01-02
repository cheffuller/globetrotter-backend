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

    public TravelPlanLocation createTravelPlanLocation(TravelPlanLocation travelPlanLocation)
            throws BadRequestException, UnauthorizedException {
        if (travelPlanLocation.getCity().isEmpty() ||
                travelPlanLocation.getCountry().isEmpty() ||
                travelPlanLocation.getStartDate() == null ||
                travelPlanLocation.getEndDate() == null) {
            throw new BadRequestException("Invalid travel plan location");
        }

        TravelPlan plan = travelPlanRepository.findById(travelPlanLocation.getTravelPlanId()).orElseThrow(() ->
                new BadRequestException(String.format(
                        "Travel plan with ID %d does not exist",
                        travelPlanLocation.getTravelPlanId()
                ))
        );

        int authTokenUserId = tokenService.getUserAccountId();
        List<Collaborator> collaborators = collaboratorRepository.findAllByPlanId(plan.getId());
        collaborators.stream().filter(collaborator -> collaborator.getId().getCollaboratorId() == authTokenUserId);
        if (collaborators.isEmpty()) {
            throw new UnauthorizedException("User is unauthorized to create a location for this travel plan.");
        }

        return travelPlanLocationRepository.save(travelPlanLocation);
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
}
