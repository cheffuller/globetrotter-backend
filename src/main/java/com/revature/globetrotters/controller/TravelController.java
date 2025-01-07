package com.revature.globetrotters.controller;

import com.revature.globetrotters.entity.TravelPlan;
import com.revature.globetrotters.entity.TravelPlanLocation;
import com.revature.globetrotters.exception.BadRequestException;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.exception.UnauthorizedException;
import com.revature.globetrotters.service.TravelPlanLocationService;
import com.revature.globetrotters.service.TravelPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/plans")
@CrossOrigin(origins = "http://localhost:5173/")
public class TravelController {
    @Autowired
    private TravelPlanService travelPlanService;
    @Autowired
    private TravelPlanLocationService travelPlanLocationService;
    private static final Logger logger = LoggerFactory.getLogger(TravelController.class);

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity handleBadRequestException(BadRequestException exception) {
        logger.info(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(NotFoundException exception) {
        logger.info(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity handleUnauthorizedException(UnauthorizedException exception) {
        logger.info(exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @PostMapping("")
    public ResponseEntity<?> createTravelPlan(@RequestBody TravelPlan travelPlan) {
        //call travel plan service layer to create travel plan
        //and then call travel plan location service layer to create travel plan location
        TravelPlan newTravelPlan = travelPlanService.createTravelPlan(travelPlan);
        //however that may be a bit weird in terms of referencing where the information for travel plan location is coming from
        return ResponseEntity.status(HttpStatus.OK).body(newTravelPlan);
    }

    @GetMapping("/{planId}")
    public ResponseEntity<?> getTravelPlansById(@PathVariable("planId") int travelPlanId) throws NotFoundException {
        // call service layer to get travel plan by id
        TravelPlan travelPlan = travelPlanService.getTravelPlanById(travelPlanId);
        // and then probably call travel plan location service layer to get travel plan location
        return ResponseEntity.status(HttpStatus.OK).body(travelPlan);
    }
    
    @PutMapping("") 
    public ResponseEntity<TravelPlan> updateTravelPlan(@RequestBody TravelPlan travelPlan) {
        // call travel plan service layer to update travel plan by its id
        // and then probably call travel plan location service layer to update travel plan location or times
        TravelPlan updatedTravelPlan = travelPlanService.updateTravelPlan(travelPlan);
        return ResponseEntity.status(HttpStatus.OK).body(updatedTravelPlan);
    }

    @PutMapping("/{planId}/locations")
    public ResponseEntity<TravelPlanLocation> updateTravelPlanLocation(@PathVariable("planId") int travelPlanId, @RequestBody TravelPlanLocation travelPlan) throws NotFoundException, UnauthorizedException, BadRequestException {
        // call travel plan service layer to update travel plan by its id
        // and then probably call travel plan location service layer to update travel plan location or times
        TravelPlanLocation updatedTravelPlan = travelPlanLocationService.updateTravelPlanLocation(travelPlan);
    return ResponseEntity.status(HttpStatus.OK).body(updatedTravelPlan); // Ensure this is the updated object
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<?> deleteTravelPlan(@PathVariable("planId") int travelPlanId) {
        // call travel plan service layer to delete travel plan by its id
        // and then probably call travel plan location service layer to delete travel plan location
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/{planId}/locations")
    public ResponseEntity<List<TravelPlanLocation>> getTravelPlanLocations(@PathVariable("planId") int travelPlanId) throws NotFoundException {
        // call travel plan location service layer to get travel plan location by its id
        List<TravelPlanLocation> locations = travelPlanLocationService.getTravelPlanLocationsByTravelPlanId(travelPlanId);
        return ResponseEntity.status(HttpStatus.OK).body(locations);
    }

    @PostMapping("/{planId}/locations")
    public ResponseEntity<TravelPlanLocation> createTravelPlanLocation(
            @PathVariable("planId") int travelPlanId, @RequestBody TravelPlanLocation travelPlanLocation)
            throws BadRequestException, UnauthorizedException {
        // call travel plan location service layer to create travel plan location
        TravelPlanLocation newLocation = travelPlanLocationService.createTravelPlanLocation(travelPlanLocation);
        return ResponseEntity.status(HttpStatus.OK).body(newLocation);
    }

    @GetMapping("/{planId}/locations/{locationId}")
    public ResponseEntity<TravelPlanLocation> getTravelPlanLocationsById(
            @PathVariable("locationId") int travelPlanId, @PathVariable("planId") int locationId)
            throws NotFoundException {
        // call travel plan location service layer to get travel plan location by its id
        TravelPlanLocation location = travelPlanLocationService.getTravelPlanLocationByIdAndTravelPlanId(travelPlanId, locationId);
        return ResponseEntity.status(HttpStatus.OK).body(location);
    }

    @GetMapping("/recent/{limit}")
    public ResponseEntity<List<TravelPlan>> findRecentPublishedPlans(@PathVariable("limit") int limit)
            throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(travelPlanService.findMostRecentPublicTravelPlan(limit));
    }

    @GetMapping("/{planId}/likes")
    public ResponseEntity<Long> getNumberOfLikesOnPostByTravelPlanId(@PathVariable("planId") int travelPlanId) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(travelPlanService.getNumberOfLikesOnPostByTravelPlanId(travelPlanId));
    }

    @GetMapping("/{planId}/comments")
    public ResponseEntity<Integer> getNumberOfCommentsOnPostByTravelPlanId(@PathVariable("planId") int travelPlanId) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(travelPlanService.getNumberOfCommentsOnPostByTravelPlanId(travelPlanId));
    }

    @GetMapping("users/{accountId}/plans")
    public ResponseEntity<List<TravelPlan>> getTravelPlansByAccountId(@PathVariable("accountId") int accountId) {
        return ResponseEntity.status(HttpStatus.OK).body(travelPlanService.getTravelPlansByAccountId(accountId));
    }

    @DeleteMapping("/{planId}/locations/{locationId}")
    public ResponseEntity<?> deleteTravelPlanLocation(@PathVariable("planId") int travelPlanId, @PathVariable("locationId") int locationId) throws NotFoundException, UnauthorizedException {
        // call travel plan location service layer to delete travel plan location by its id
        travelPlanLocationService.deleteTravelPlanLocation(travelPlanId, locationId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
} 
    
