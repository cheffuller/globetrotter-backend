package com.revature.globetrotters.service;

import com.revature.globetrotters.GlobeTrottersApplication;
import com.revature.globetrotters.entity.TravelPlanLocation;
import com.revature.globetrotters.service.TravelPlanLocationService;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;    
import org.springframework.context.ApplicationContext;


import com.revature.globetrotters.util.DateArgumentConverter;

import java.util.List;
import java.util.Date;

@SpringBootTest
public class TravelPlanLocationServiceTests {
    @Autowired
    TravelPlanLocationService travelPlanLocationService;
    ApplicationContext app;

    @BeforeEach
    public void setUp() throws InterruptedException {
        String[] args = new String[]{};
        app = SpringApplication.run(GlobeTrottersApplication.class, args);
        Thread.sleep(500);
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        Thread.sleep(500);
        SpringApplication.exit(app);
    }
    
    @Test
    public void createTravelPlanLocationTest() throws BadRequestException  {
        DateArgumentConverter converter = new DateArgumentConverter();

        TravelPlanLocation travelPlanLocation = new TravelPlanLocation(
            null, 
            "New York",
            "United States",
            (Date) converter.convert("2020-12-31", null),
            (Date) converter.convert("2020-12-01", null),
            1
        );

        TravelPlanLocation expectedResult = new TravelPlanLocation(
            2, 
            "New York",
            "United States",
            (Date) converter.convert("2020-12-31", null),
            (Date) converter.convert("2020-12-01", null),
            1
        );

        TravelPlanLocation actualResult = travelPlanLocationService.createTravelPlanLocation(travelPlanLocation);

        Assertions.assertEquals(expectedResult, actualResult,
                "Expected: " + expectedResult + ". Actual: " + actualResult);
    }
  
    @Test
    void getTravelPlanLocationsByTravelPlanIdTest() {
        DateArgumentConverter converter = new DateArgumentConverter();
        List<TravelPlanLocation> expectedResult = List.of(
            (new TravelPlanLocation(1, "Sydney", "Australia", 
            (Date) converter.convert("2018-12-31", null),
            (Date) converter.convert("2018-12-01", null), 1))
        );
        List<TravelPlanLocation> actualResult = travelPlanLocationService.getTravelPlanLocationsByTravelPlanId(1);
        Assertions.assertEquals(expectedResult, actualResult,
                "Expected: " + expectedResult + ". Actual: " + actualResult);
    }

    @Test
    void getTravelPlanLocationByIdTest() throws BadRequestException {
        DateArgumentConverter converter = new DateArgumentConverter();
        TravelPlanLocation expectedResult = new TravelPlanLocation(1, "Sydney", "Australia", 
            (Date) converter.convert("2018-12-31", null),
            (Date) converter.convert("2018-12-01", null), 1);
        TravelPlanLocation actualResult = travelPlanLocationService.getTravelPlanLocationById(1, 1);

        Assertions.assertEquals(expectedResult, actualResult,
                "Expected: " + expectedResult + ". Actual: " + actualResult);
    }
}
