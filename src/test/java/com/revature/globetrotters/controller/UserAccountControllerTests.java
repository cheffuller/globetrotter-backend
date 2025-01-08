package com.revature.globetrotters.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.globetrotters.GlobeTrottersApplication;
import com.revature.globetrotters.entity.UserAccount;
import com.revature.globetrotters.service.AuthenticationTokenService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.revature.globetrotters.util.SecurityUtils.getWebToken;
import static com.revature.globetrotters.utils.JwtUtil.tokenMatches;

public class UserAccountControllerTests {
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
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        SpringApplication.exit(app);
    }

    @Test
    public void createAccountTest() throws IOException, InterruptedException {
        UserAccount account = new UserAccount("address", "city", "country", "email", "firstname", "lastname",
                "password", "username1111");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/users/register"))
                .header("Content-Type", "application/json")
                .header("authorization", getWebToken())
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(account)))
                .build();

        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());
    }

    @Test
    public void createAccountWithNullValueTest() throws IOException, InterruptedException {
        UserAccount account = new UserAccount("address", "city", "country", "email", "firstname", "lastname",
                "password", null);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/users/register"))
                .header("Content-Type", "application/json")
                .header("authorization", getWebToken())
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(account)))
                .build();

        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    @Test
    public void createAccountWithDuplicateUsernameTest() throws IOException, InterruptedException {
        UserAccount account = new UserAccount("address", "city", "country", "email",
                "firstname", "lastname", "password", "john_doe");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/users/register"))
                .header("Content-Type", "application/json")
                .header("authorization", getWebToken())
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(account)))
                .build();

        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    @Test
    public void loginWithValidCredentials() throws IOException, InterruptedException {
        UserAccount account = new UserAccount();
        account.setPassword("password");
        account.setUsername("john_doe");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/users/login"))
                .header("Content-Type", "application/json")
                .header("authorization", getWebToken())
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(account)))
                .build();

        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());
        Assertions.assertTrue(tokenMatches(getWebToken("john_doe", 1), response.body()));
    }

    @Test
    public void loginWithInvalidCredentials() throws IOException, InterruptedException {
        UserAccount account = new UserAccount();
        account.setPassword("password");
        account.setUsername("john_do");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/users/login"))
                .header("Content-Type", "application/json")
                .header("authorization", getWebToken())
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(account)))
                .build();

        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.statusCode());
    }

    @Test
    public void findUserByIdTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/users/1"))
                .header("Content-Type", "application/json")
                .header("authorization", getWebToken())
                .GET()
                .build();

        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());

        UserAccount expected = new UserAccount(1, "1234", "New York", "United States",
                "test@gmail.com", "John", "Doe",
                "$2a$10$seol2uAfLTyKI/gYKbL7G.XOAuOzZ2EAseMrgyI21Z9K9l9bhG.GO", "john_doe");
        UserAccount actual = objectMapper.readValue(response.body(), UserAccount.class);
        Assertions.assertEquals(expected, actual);
    }
}
