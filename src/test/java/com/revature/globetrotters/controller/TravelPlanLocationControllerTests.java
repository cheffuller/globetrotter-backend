package com.revature.globetrotters.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.globetrotters.GlobeTrottersApplication;
import com.revature.globetrotters.entity.TravelPlanLocation;
import com.revature.globetrotters.util.DateArgumentConverter;

import org.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;


public class TravelPlanLocationControllerTests {
    ApplicationContext app;
    HttpClient webClient;
    ObjectMapper objectMapper;

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

    @ParameterizedTest
    @CsvSource({
            "'New York', 'United States', '2020-12-31', '2020-12-01', 1"
    })
    public void createTravelPlanLocationTest(String city, String country, @ConvertWith(DateArgumentConverter.class) Date endDate, @ConvertWith(DateArgumentConverter.class) Date startDate, Integer travelPlanId) throws IOException, InterruptedException, JSONException {
        TravelPlanLocation travelPlanLocation = new TravelPlanLocation(
            city,
            country,
            endDate,
            startDate,
            travelPlanId
        );

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/plans/" + travelPlanId + "/locations"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(travelPlanLocation)))
            .build();

        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());
    }

    @Test
    public void getTravelPlanLocationsTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/plans/1/locations"))
            .header("Content-Type", "application/json")
            .GET()
            .build();

        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());
    }

    @ParameterizedTest
    @CsvSource({
        "1, 'Sydney', 'Australia', '2018-12-31', '2018-12-01', 1"
    })
    public void getTravelPlanLocationByIdTest(Integer locationId, String city, String country, 
    @ConvertWith(DateArgumentConverter.class) Date endDate, @ConvertWith(DateArgumentConverter.class) Date starDate, 
    Integer travelPlanId) throws IOException, InterruptedException, JSONException {
        TravelPlanLocation expected = new TravelPlanLocation(locationId, city, country, endDate, starDate, travelPlanId);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/plans/" + travelPlanId + "/locations/" + locationId))
            .header("Content-Type", "application/json")
            .GET()
            .build();

        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        TravelPlanLocation actual = objectMapper.readValue(response.body(), TravelPlanLocation.class);
        Assertions.assertEquals(expected, actual);
    }



}
