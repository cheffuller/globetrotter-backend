package com.revature.globetrotters.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.globetrotters.GlobeTrottersApplication;
import com.revature.globetrotters.consts.JwtConsts;
import com.revature.globetrotters.entity.Comment;
import com.revature.globetrotters.entity.Post;
import com.revature.globetrotters.entity.UserAccount;
import com.revature.globetrotters.util.DateArgumentConverter;
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
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static com.revature.globetrotters.util.DateArgumentConverter.convertToDate;
import static com.revature.globetrotters.util.SecurityUtils.getWebToken;

public class PostControllerTests {
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
    public void getAllPostsByUserIdTest() throws IOException, InterruptedException, ParseException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/users/1/posts"))
                .header(JwtConsts.AUTHORIZATION, getWebToken())
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());

        List<Post> expectedPosts = List.of(
                new Post(1, convertToDate("2019-01-01"), 1),
                new Post(5, convertToDate("2019-01-01"), 5)
        );

        List<Post> actualPosts = objectMapper.readValue(response.body(), new TypeReference<>() {
        });
        Assertions.assertEquals(expectedPosts, actualPosts,
                String.format("Expected: %s.\nActual: %s.", expectedPosts, actualPosts));
    }

    @Test
    public void getAllPostsByInvalidUserIdTest() throws IOException, InterruptedException, ParseException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/users/100/posts"))
                .header(JwtConsts.AUTHORIZATION, getWebToken())
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());
    }

    @ParameterizedTest
    @CsvSource({
            "'2020-01-01', 1"
    })
    public void postPostTest(@ConvertWith(DateArgumentConverter.class) Date date, Integer travelPlanId)
            throws IOException, InterruptedException {
        Post newPost = new Post();
        newPost.setPostedDate(date);
        newPost.setTravelPlanId(travelPlanId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/posts"))
                .header("Content-Type", "application/json")
                .header(JwtConsts.AUTHORIZATION, getWebToken())
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(newPost)))
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());

        Post actualPost = objectMapper.readValue(response.body(), Post.class);
        Post expectedPost = new Post(actualPost.getId(), date, travelPlanId);
        Assertions.assertEquals(expectedPost, actualPost);
    }

    @ParameterizedTest
    @CsvSource({
            "1, '2019-01-01', 1"
    })
    public void getPostByIDTest(Integer postId, @ConvertWith(DateArgumentConverter.class) Date date,
                                Integer travelPlanId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/posts/1"))
                .header(JwtConsts.AUTHORIZATION, getWebToken())
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());

        Post expectedPost = new Post(postId, date, travelPlanId);
        Post actualPost = objectMapper.readValue(response.body(), Post.class);
        Assertions.assertEquals(expectedPost, actualPost);
    }

    @Test
    public void getPostByInvalidIDTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/posts/100"))
                .header(JwtConsts.AUTHORIZATION, getWebToken())
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());
    }

    @ParameterizedTest
    @CsvSource({
            "1"
    })
    public void deletePostSuccessfulTest(Integer postId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/posts/" + postId))
                .header(JwtConsts.AUTHORIZATION, getWebToken())
                .DELETE()
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());
    }

    @ParameterizedTest
    @CsvSource({
            "12345"
    })
    public void deletePostNotFoundTest(Integer postId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/posts/" + postId))
                .header(JwtConsts.AUTHORIZATION, getWebToken())
                .DELETE()
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());
    }

    @ParameterizedTest
    @CsvSource({
            "1"
    })
    public void getNumberOfLikesOnPostByIdTest(Integer postId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/posts/" + postId + "/likes"))
                .header(JwtConsts.AUTHORIZATION, getWebToken())
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());

        Integer expectedLikes = 1;
        Integer actualLikes = objectMapper.readValue(response.body(), Integer.class);
        Assertions.assertEquals(expectedLikes, actualLikes);
    }

    @Test
    public void getNumberOfLikesOnNonExistentPostTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/posts/100/likes"))
                .header(JwtConsts.AUTHORIZATION, getWebToken())
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());
    }

    @Test
    public void getCommentsByPostIdTest() throws IOException, InterruptedException, ParseException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/posts/1/comments"))
                .header(JwtConsts.AUTHORIZATION, getWebToken())
                .GET()
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());

        List<Comment> expectedComments = List.of(
                new Comment(1, convertToDate("2019-01-01"), 1, "WOW! This trip looks amazing!", 3)
        );
        List<Comment> actualComments = objectMapper.readValue(response.body(), new TypeReference<>() {
        });
        Assertions.assertEquals(expectedComments, actualComments);
    }

    @Test
    public void getCommentsByNonExistentPostIdTest() throws IOException, InterruptedException, ParseException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/posts/100/comments"))
                .header(JwtConsts.AUTHORIZATION, getWebToken())
                .GET()
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());
    }

    @ParameterizedTest
    @CsvSource({
            "'2020-01-01', 1, 'content', 1"
    })
    public void postCommentTest(
            @ConvertWith(DateArgumentConverter.class) Date date,
            Integer postId, String content, Integer userId) throws IOException, InterruptedException {
        Comment expectedComment = new Comment(date, postId, content, userId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/comments"))
                .header("Content-Type", "application/json")
                .header(JwtConsts.AUTHORIZATION, getWebToken())
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(expectedComment)))
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());

        Comment actualComment = objectMapper.readValue(response.body(), Comment.class);
        expectedComment.setId(actualComment.getId());
        Assertions.assertEquals(expectedComment, actualComment);
    }

    @ParameterizedTest
    @CsvSource({
            "1, '2019-01-01', 1, 'WOW! This trip looks amazing!', 3"
    })
    public void getCommentByIdTest(
            Integer commentId,
            @ConvertWith(DateArgumentConverter.class) Date date,
            Integer postId,
            String content,
            Integer userId)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/comments/" + commentId))
                .header(JwtConsts.AUTHORIZATION, getWebToken())
                .GET()
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());

        Comment expectedComment = new Comment(commentId, date, postId, content, userId);
        Comment actualComment = objectMapper.readValue(response.body(), Comment.class);
        Assertions.assertEquals(expectedComment, actualComment);
    }

    @Test
    public void getNonExistentCommentByIdTest()
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/comments/100"))
                .header(JwtConsts.AUTHORIZATION, getWebToken())
                .GET()
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());
    }

    @ParameterizedTest
    @CsvSource({
            "4"
    })
    public void deleteCommentTest(Integer commentId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/comments/" + commentId))
                .header(JwtConsts.AUTHORIZATION, getWebToken())
                .DELETE()
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());
    }

    @Test
    public void deleteNonExistentCommentTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/comments/100"))
                .header(JwtConsts.AUTHORIZATION, getWebToken())
                .DELETE()
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());
    }

    @ParameterizedTest
    @CsvSource({
            "1"
    })
    public void getNumberOfLikesOnCommentByIdTest(Integer commentId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/comments/" + commentId + "/likes"))
                .header(JwtConsts.AUTHORIZATION, getWebToken())
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());

        Integer expectedLikes = 1;
        Integer actualLikes = objectMapper.readValue(response.body(), Integer.class);
        Assertions.assertEquals(expectedLikes, actualLikes);
    }

    @Test
    public void getNumberOfLikesOnNonExistentCommentByIdTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/comments/100/likes"))
                .header(JwtConsts.AUTHORIZATION, getWebToken())
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 2"
    })
    public void likeCommentTest(Integer commentId, Integer userId) throws IOException, InterruptedException {
        UserAccount userAccount = new UserAccount();
        userAccount.setId(userId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/comments/" + commentId + "/likes"))
                .header("Content-Type", "application/json")
                .header(JwtConsts.AUTHORIZATION, getWebToken())
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(userAccount)))
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());
    }

    @Test
    public void likeNonExistentCommentTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/comments/100/likes"))
                .header(JwtConsts.AUTHORIZATION, getWebToken())
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 3"
    })
    public void unlikeCommentTest(Integer commentId, Integer userId) throws IOException, InterruptedException {
        UserAccount userAccount = new UserAccount();
        userAccount.setId(userId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/comments/" + commentId + "/likes"))
                .header("Content-Type", "application/json")
                .header(JwtConsts.AUTHORIZATION, getWebToken())
                .method("DELETE", HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(userAccount)))
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 2"
    })
    public void likePostTest(Integer postId, Integer userId) throws IOException, InterruptedException {
        UserAccount userAccount = new UserAccount();
        userAccount.setId(userId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/posts/" + postId + "/likes"))
                .header("Content-Type", "application/json")
                .header(JwtConsts.AUTHORIZATION, getWebToken())
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(userAccount)))
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());
    }

    @Test
    public void likeNonExistentPostTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/posts/100/likes"))
                .header(JwtConsts.AUTHORIZATION, getWebToken())
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 3"
    })
    public void unlikePostTest(Integer postId, Integer userId) throws IOException, InterruptedException {
        UserAccount userAccount = new UserAccount();
        userAccount.setId(userId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/posts/" + postId + "/likes"))
                .header("Content-Type", "application/json")
                .header(JwtConsts.AUTHORIZATION, getWebToken())
                .method("DELETE", HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(userAccount)))
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());
    }
}