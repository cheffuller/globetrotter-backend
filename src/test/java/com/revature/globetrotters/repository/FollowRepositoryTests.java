package com.revature.globetrotters.repository;

import com.revature.globetrotters.GlobeTrottersApplication;
import com.revature.globetrotters.entity.Follow;
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
public class FollowRepositoryTests {
    @Autowired
    FollowRepository followRepository;
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
    public void findByFollowingTest() {
        int following = 1;
        List<Follow> actualResult = followRepository.findByFollowing(following);
        List<Follow> expectedResult = List.of(new Follow(2, 1));

        Assertions.assertEquals(expectedResult, actualResult,
                "Expected: " + expectedResult + ". Actual: " + actualResult);
    }

    @Test
    public void findByFollowerTest() {
        int follower = 1;
        List<Follow> actualResult = followRepository.findByFollower(follower);
        List<Follow> expectedResult = List.of(
                new Follow(1, 2),
                new Follow(1, 3));

        Assertions.assertEquals(expectedResult, actualResult,
                "Expected: " + expectedResult + ". Actual: " + actualResult);
    }
}
