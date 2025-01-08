package com.revature.globetrotters.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.globetrotters.GlobeTrottersApplication;
import com.revature.globetrotters.entity.Post;
import com.revature.globetrotters.entity.TravelPlan;
import com.revature.globetrotters.service.AuthenticationTokenService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;

import static com.revature.globetrotters.util.DateArgumentConverter.convertToDate;
import static com.revature.globetrotters.util.SecurityUtils.getWebToken;

public class TravelPlanControllerTests {
    @Autowired
    private AuthenticationTokenService authenticationTokenService;
    private ApplicationContext app;
    private HttpClient webClient;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() throws InterruptedException {
        webClient = HttpClient.newHttpClient();
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
    public void createTravelPlanTest() throws IOException, InterruptedException {
        TravelPlan plan = new TravelPlan(false, false);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/plans"))
                .header("Content-Type", "application/json")
                .header("authorization", getWebToken("john_doe", 1))
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(plan)))
                .build();

        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());

        TravelPlan responsePlan = objectMapper.readValue(response.body(), TravelPlan.class);
        Assertions.assertEquals(1, responsePlan.getAccountId());
    }

    @Test
    public void findTravelPlanByIdTest() throws IOException, InterruptedException, ParseException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/plans/1"))
                .header("Content-Type", "application/json")
                .header("authorization", getWebToken("john_doe", 1))
                .GET()
                .build();

        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());

        TravelPlan expectedPlan = new TravelPlan(1, 1, false, false);
        TravelPlan actualPlan = objectMapper.readValue(response.body(), TravelPlan.class);
        Assertions.assertEquals(expectedPlan, actualPlan);

        Post expectedPost = new Post(1, convertToDate("2019-01-01"), 1, "john_doe");
        Assertions.assertEquals(expectedPost, actualPlan.getPost());
        Assertions.assertEquals(1, actualPlan.getPost().getNumberOfLikes());
    }

    @Test
    public void updateTravelPlanTest() throws IOException, InterruptedException {
        TravelPlan plan = new TravelPlan(1, 1, true, false);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/plans"))
                .header("Content-Type", "application/json")
                .header("authorization", getWebToken("john_doe", 1))
                .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(plan)))
                .build();

        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());

        TravelPlan actual = objectMapper.readValue(response.body(), TravelPlan.class);
        Assertions.assertEquals(plan, actual);
    }

    @Test
    public void unauthorizedUpdateTravelPlanTest() throws IOException, InterruptedException {
        TravelPlan plan = new TravelPlan(2, 2, true, false);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/plans"))
                .header("Content-Type", "application/json")
                .header("authorization", getWebToken("john_doe", 1))
                .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(plan)))
                .build();

        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.statusCode());
    }

    @Test
    public void invalidUpdateTravelPlanTest() throws IOException, InterruptedException {
        TravelPlan plan = new TravelPlan(true, false);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/plans"))
                .header("Content-Type", "application/json")
                .header("authorization", getWebToken("john_doe", 1))
                .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(plan)))
                .build();

        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    @Test
    public void updateNonExistentTravelPlanTest() throws IOException, InterruptedException {
        TravelPlan plan = new TravelPlan(10, 1, true, false);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/plans"))
                .header("Content-Type", "application/json")
                .header("authorization", getWebToken("john_doe", 1))
                .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(plan)))
                .build();

        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());
    }

    @Test
    public void deleteTravelPlanTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/plans/1"))
                .header("Content-Type", "application/json")
                .header("authorization", getWebToken("john_doe", 1))
                .DELETE()
                .build();

        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());
    }

    @Test
    public void unauthorizedDeleteTravelPlanTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/plans/2"))
                .header("Content-Type", "application/json")
                .header("authorization", getWebToken("john_doe", 1))
                .DELETE()
                .build();

        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.statusCode());
    }
}
