package com.revature.globetrotters.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import com.revature.globetrotters.entity.TravelPlan;
import com.revature.globetrotters.entity.TravelPlanLocation;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@Controller
@RequestMapping("/accounts")
@CrossOrigin(origins = "http://localhost:5173/")
public class TravelController {
    @PatchMapping("/plans/{id}")
    public ResponseEntity<?> updateTravelTimes(@PathVariable TravelPlanLocation travelPlan) {
        try {
            // call service layer to update travel plan by 
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
