package com.revature.globetrotters.repository;

import com.revature.globetrotters.GlobeTrottersApplication;
import com.revature.globetrotters.entity.FollowRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootTest
public class FollowRequestRepositoryTests {
    @Autowired
    FollowRequestRepository followRequestRepository;
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
    public void findByFollowingTest() {
        int following = 4;
        List<FollowRequest> actualResult = followRequestRepository.findByFollowing(following);
        List<FollowRequest> expectedResult = List.of(
                new FollowRequest(1, 4),
                new FollowRequest(2, 4),
                new FollowRequest(3, 4));

        Assertions.assertEquals(expectedResult, actualResult,
                "Expected: " + expectedResult + ". Actual: " + actualResult);
    }

    @Test
    public void findByFollowerTest() {
        int follower = 1;
        List<FollowRequest> actualResult = followRequestRepository.findByFollower(follower);
        List<FollowRequest> expectedResult = List.of(new FollowRequest(1, 4));

        Assertions.assertEquals(expectedResult, actualResult,
                "Expected: " + expectedResult + ". Actual: " + actualResult);
    }
}
