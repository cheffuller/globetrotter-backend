package com.revature.globetrotters.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.revature.globetrotters.entity.TravelPlan;
import com.revature.globetrotters.entity.TravelPlanLocation;


//no need for path variable in the post 
// no need to have path variable for travelPlan

@Controller
@RequestMapping("/plans")
@CrossOrigin(origins = "http://localhost:5173/")
public class TravelController {
    @PostMapping("")
    public ResponseEntity<?> createTravelPlan(@RequestBody TravelPlan travelPlan) {
        try {
            //call travel plan service layer to create travel plan
            //and then call travel plan location service layer to create travel plan location
            //however that may be a bit weird in terms of referencing where the information for travel plan location is coming from
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTravelPlansById(@PathVariable int travelPlanId) {
        try {
            // call service layer to get travel plan by id
            // and then probably call travel plan location service layer to get travel plan location 
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("")
    public ResponseEntity<?> updateTravelPlan(@RequestBody TravelPlan travelPlan) {
        try {
            // call travel plan service layer to update travel plan by its id
            // and then probably call travel plan location service layer to update travel plan location or times
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTravelPlan(@PathVariable int travelPlanId) {
        try {
            // call travel plan service layer to delete travel plan by its id
            // and then probably call travel plan location service layer to delete travel plan location
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
