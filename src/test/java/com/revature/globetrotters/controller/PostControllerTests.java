package com.revature.globetrotters.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.globetrotters.GlobeTrottersApplication;
import com.revature.globetrotters.entity.Comment;
import com.revature.globetrotters.entity.Post;
import com.revature.globetrotters.util.DateArgumentConverter;

public class PostControllerTests {
        ApplicationContext app;
        HttpClient webClient;
        ObjectMapper objectMapper;

        @BeforeEach
        public void setUp() throws InterruptedException {
                webClient = HttpClient.newHttpClient();
                objectMapper = new ObjectMapper();
                String[] args = new String[] {};
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
                        "'2020-01-01', 1"
        })
        public void postPostTest(@ConvertWith(DateArgumentConverter.class) Date date, Integer travelPlanId)
                        throws IOException, InterruptedException, Exception {
                // JSONObject jsonObject = new JSONObject();
                // jsonObject.put("postedDate", date);
                // jsonObject.put("travelPlanId", travelPlanId);

                Post newPost = new Post();
                newPost.setPostedDate(date);
                newPost.setTravelPlanId(travelPlanId);
                HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/posts"))
                                .POST(HttpRequest.BodyPublishers.ofString(newPost.toString()))
                                .build();
                HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
                // int status = response.statusCode();
                // Assertions.assertEquals(200, status);

                Post actualPost = objectMapper.readValue(response.body(), Post.class);
                Post expectedPost = new Post(actualPost.getId(), date, travelPlanId);
                Assertions.assertEquals(expectedPost, actualPost);
        }

        @ParameterizedTest
        @CsvSource({
                        "1, '2019-01-01', 1"
        })
        public void getPostByIDTest(Integer postId, @ConvertWith(DateArgumentConverter.class) Date date,
                        Integer travelPlanId) throws IOException, InterruptedException, Exception {
                HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/posts/1"))
                                .build();
                HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());

                Post expectedPost = new Post(postId, date, travelPlanId);
                Post actualPost = objectMapper.readValue(response.body(), Post.class);
                Assertions.assertEquals(expectedPost, actualPost);
        }

        @ParameterizedTest
        @CsvSource({
                        "1"
        })
        public void getPostLikesTest(Integer postId) throws IOException, InterruptedException, Exception {
                HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/posts/" + postId + "/likes"))
                                .build();
                HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());

                Integer expectedLikes = 1;
                Integer actualLikes = objectMapper.readValue(response.body(), Integer.class);
                Assertions.assertEquals(expectedLikes, actualLikes);
        }

        @ParameterizedTest
        @CsvSource({
                        "1, '2019-01-01', 1, 'content', 3"
        })
        public void getCommentByIdTest(Integer commentId, @ConvertWith(DateArgumentConverter.class) Date date,
                        Integer postId, String content, Integer userId)
                        throws IOException, InterruptedException, Exception {
                HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/comments/" + commentId))
                                .GET()
                                .build();
                HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());

                Comment expectedComment = new Comment(commentId, date, postId, content, userId);
                Comment actualComment = objectMapper.readValue(response.body(), Comment.class);
                Assertions.assertEquals(expectedComment, actualComment);
        }
}
