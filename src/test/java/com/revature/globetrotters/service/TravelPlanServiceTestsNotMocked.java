package com.revature.globetrotters.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import com.revature.globetrotters.GlobeTrottersApplication;
import com.revature.globetrotters.exception.BadRequestException;
import com.revature.globetrotters.exception.NotFoundException;

@SpringBootTest
public class TravelPlanServiceTestsNotMocked {
    @Autowired
    TravelPlanService travelPlanService;
    ApplicationContext app;

    @BeforeEach
    public void setUp() throws InterruptedException {
        String[] args = new String[]{};
        app = SpringApplication.run(GlobeTrottersApplication.class, args);
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        SpringApplication.exit(app);
    }

    @ParameterizedTest
    @CsvSource({
        "3, 2"
    })
    public void getNumberOfLikesOnPostByTravelPlanId(Integer planId, int expectedLikes) throws BadRequestException, NotFoundException {
        long actualLikes = travelPlanService.getNumberOfLikesOnPostByTravelPlanId(planId);
        Assertions.assertEquals(expectedLikes, actualLikes);
    }

    @ParameterizedTest
    @CsvSource({
        "1, 1"
    })
    public void getNumberofCommentsOnPostByTravelPlanId(Integer planId, int expectedComments) throws BadRequestException, NotFoundException {
        int actualComments = travelPlanService.getNumberOfCommentsOnPostByTravelPlanId(planId);
        Assertions.assertEquals(expectedComments, actualComments);
    }
}
