package com.revature.globetrotters.service;

import com.revature.globetrotters.GlobeTrottersApplication;
import com.revature.globetrotters.entity.TravelPlanLocation;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.util.DateArgumentConverter;
import com.revature.globetrotters.exception.BadRequestException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.text.ParseException;
import java.util.List;

import static com.revature.globetrotters.util.DateArgumentConverter.convertToDate;

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
    public void createTravelPlanLocationTest() throws BadRequestException, ParseException {
        DateArgumentConverter converter = new DateArgumentConverter();

        TravelPlanLocation travelPlanLocation = new TravelPlanLocation(
                null,
                "New York",
                "United States",
                convertToDate("2020-12-01"),
                convertToDate("2020-12-31"),
                1
        );

        TravelPlanLocation expectedResult = new TravelPlanLocation(
                7,
                "New York",
                "United States",
                convertToDate("2020-12-01"),
                convertToDate("2020-12-31"),
                1
        );

        TravelPlanLocation actualResult = travelPlanLocationService.createTravelPlanLocation(travelPlanLocation);

        Assertions.assertEquals(expectedResult, actualResult,
                "Expected: " + expectedResult + ". Actual: " + actualResult);
    }

    @Test
    void getTravelPlanLocationsByTravelPlanIdTest() throws ParseException {
        DateArgumentConverter converter = new DateArgumentConverter();
        List<TravelPlanLocation> expectedResult = List.of(
                (new TravelPlanLocation(
                        1,
                        "Sydney",
                        "Australia",
                        convertToDate("2018-12-31"),
                        convertToDate("2018-12-01"),
                        1))
        );
        List<TravelPlanLocation> actualResult = travelPlanLocationService.getTravelPlanLocationsByTravelPlanId(1);
        Assertions.assertEquals(expectedResult, actualResult,
                "Expected: " + expectedResult + ". Actual: " + actualResult);
    }

    @Test
    void getTravelPlanLocationByIdTest() throws ParseException, NotFoundException {
        TravelPlanLocation expectedResult = new TravelPlanLocation(
                1,
                "Sydney",
                "Australia",
                convertToDate("2018-12-31"),
                convertToDate("2018-12-01"),
                1);
        TravelPlanLocation actualResult = travelPlanLocationService.getTravelPlanLocationById(1, 1);

        Assertions.assertEquals(expectedResult, actualResult,
                "Expected: " + expectedResult + ". Actual: " + actualResult);
    }
}
