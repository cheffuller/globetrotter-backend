package com.revature.globetrotters.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;

import com.revature.globetrotters.entity.TravelPlan;
import com.revature.globetrotters.entity.TravelPlanLocation;


@Controller
@RequestMapping("/travels")
@CrossOrigin(origins = "http://localhost:5173/")
public class TravelController {
    @PostMapping("/plans")
    public ResponseEntity<?> createTravelPlan(@PathVariable TravelPlan travelPlan) {
        try {

            //call travel plan service layer to create travel plan
            //and then call travel plan location service layer to create travel plan location
            //however that may be a bit weird in terms of referencing where the information for travel plan location is coming from
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/plans/{id}")
    public ResponseEntity<?> getTravelPlansById(@PathVariable TravelPlan travelPlanId) {
        try {
            // call service layer to get travel plan by id
            // and then probably call travel plan location service layer to get travel plan location 
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("plans/{id}")
    public ResponseEntity<?> updateTravelPlan(@PathVariable TravelPlan travelPlanId) {
        try {
            // call travel plan service layer to update travel plan by its id
            // and then probably call travel plan location service layer to update travel plan location or times
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("plans/{id}")
    public ResponseEntity<?> deleteTravelPlan(@PathVariable TravelPlan travelPlanId) {
        try {
            // call travel plan service layer to delete travel plan by its id
            // and then probably call travel plan location service layer to delete travel plan location
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
