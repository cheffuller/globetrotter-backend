package com.revature.globetrotters.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.globetrotters.GlobeTrottersApplication;
import com.revature.globetrotters.entity.Follow;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import java.net.http.HttpClient;
import java.util.List;

public class FollowTests {
    @Autowired
    FollowRepository followRepository;
    ApplicationContext app;
    ObjectMapper objectMapper;


    @BeforeEach
    public void setUp() throws InterruptedException {
        objectMapper = new ObjectMapper();
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
    public void getFollowTest() {
//        int follower = 1;
//        List<Follow> actualResult = followRepository.findFollowerAndFollowingByFollowing(follower);
//        List<Follow> expectedResult = List.of();
//
//        Assertions.assertEquals(expectedResult, actualResult,
//                "Expected: " + expectedResult + ". Actual: " + actualResult);
    }
}
