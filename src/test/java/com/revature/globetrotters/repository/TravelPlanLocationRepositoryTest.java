package com.revature.globetrotters.repository;

import com.revature.globetrotters.GlobeTrottersApplication;
import com.revature.globetrotters.entity.TravelPlanLocation;
import com.revature.globetrotters.exception.NotFoundException;
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
public class TravelPlanLocationRepositoryTest {
    @Autowired
    TravelPlanLocationRepository travelPlanLocationRepository;
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

    @Test
    public void findAllByTravelPlanIdTest() throws ParseException {
        List<TravelPlanLocation> expected = List.of(
                new TravelPlanLocation(1, "Sydney", "Australia", convertToDate("2018-12-31"),
                        convertToDate("2018-12-01"), 1)
        );
        List<TravelPlanLocation> actual = travelPlanLocationRepository.findAllByTravelPlanId(1);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findByIdAndTravelPlanIdTest() throws ParseException, NotFoundException {
        TravelPlanLocation expected = new TravelPlanLocation(1, "Sydney", "Australia", convertToDate("2018-12-31"),
                convertToDate("2018-12-01"), 1);
        TravelPlanLocation actual = travelPlanLocationRepository.findByTravelPlanIdAndId(1, 1)
                .orElseThrow(() -> new NotFoundException("Not found"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findByTravelPlanIdAndOffsetTest() throws ParseException, NotFoundException {
        TravelPlanLocation expected = new TravelPlanLocation(1, "Sydney", "Australia", convertToDate("2018-12-31"),
                convertToDate("2018-12-01"), 1);
        TravelPlanLocation actual = travelPlanLocationRepository.findByTravelPlanIdAndOffset(1, 0)
                .orElseThrow(() -> new NotFoundException("Not found"));
        Assertions.assertEquals(expected, actual);
    }
}
