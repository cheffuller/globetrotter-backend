package com.revature.globetrotters.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.globetrotters.GlobeTrottersApplication;
import com.revature.globetrotters.consts.JwtConsts;
import com.revature.globetrotters.entity.Comment;
import com.revature.globetrotters.entity.Post;
import com.revature.globetrotters.entity.UserAccount;
import com.revature.globetrotters.security.JwtUtil;
import com.revature.globetrotters.util.DateArgumentConverter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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

    private String getWebtoken() {
        return JwtUtil.generateTokenFromUserName("john_doe", new HashMap<>());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1, '2019-01-01', 1"
    })
    public void getAllPostsByUserIdTest(Integer userId, Integer postId, @ConvertWith(DateArgumentConverter.class) Date date,
                                        Integer travelPlanId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/users/" + userId + "/posts"))
                .header(JwtConsts.AUTHORIZATION, getWebtoken())
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assertions.assertEquals(200, status);

        Post post = new Post(postId, date, travelPlanId);
        List<Post> expectedPosts = new ArrayList<>();
        expectedPosts.add(post);

        List<Post> actualPosts = objectMapper.readValue(response.body(), new TypeReference<>() {
        });
        Assertions.assertEquals(expectedPosts, actualPosts,
                String.format("Expected: %s.\nActual: %s.", expectedPosts, actualPosts));
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
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(newPost)))
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assertions.assertEquals(200, status);

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
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assertions.assertEquals(200, status);

        Post expectedPost = new Post(postId, date, travelPlanId);
        Post actualPost = objectMapper.readValue(response.body(), Post.class);
        Assertions.assertEquals(expectedPost, actualPost);
    }

    @ParameterizedTest
    @CsvSource({
            "1"
    })
    public void deletePostSuccesfulTest(Integer postId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/posts/" + postId))
                .DELETE()
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assertions.assertEquals(200, status);
    }

    @ParameterizedTest
    @CsvSource({
            "12345"
    })
    public void deletePostNotFoundTest(Integer postId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/posts/" + postId))
                .DELETE()
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assertions.assertEquals(404, status);
    }

    @ParameterizedTest
    @CsvSource({
            "1"
    })
    public void getNumberOfLikesOnPostByIdTest(Integer postId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/posts/" + postId + "/likes"))
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assertions.assertEquals(200, status);

        Integer expectedLikes = 1;
        Integer actualLikes = objectMapper.readValue(response.body(), Integer.class);
        Assertions.assertEquals(expectedLikes, actualLikes);
    }

    @ParameterizedTest
    @CsvSource({
            "1, '2019-01-01', 1, 'content', 3"
    })
    public void getCommentsByPostIdTest(Integer commentId, @ConvertWith(DateArgumentConverter.class) Date date,
                                        Integer postId, String content, Integer userId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/posts/" + postId + "/comments"))
                .GET()
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assertions.assertEquals(200, status);

        Comment comment = new Comment(commentId, date, postId, content, userId);
        List<Comment> expectedComments = new ArrayList<>();
        expectedComments.add(comment);

        List<Comment> actualComments = objectMapper.readValue(response.body(), new TypeReference<>() {
        });
        Assertions.assertEquals(expectedComments, actualComments);
    }

    @ParameterizedTest
    @CsvSource({
            "'2020-01-01', 1, 'content', 3"
    })
    public void postCommentTest(@ConvertWith(DateArgumentConverter.class) Date date,
                                Integer postId, String content, Integer userId) throws IOException, InterruptedException {
        Comment expectedComment = new Comment(date, postId, content, userId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/comments"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(expectedComment)))
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assertions.assertEquals(200, status);

        Comment actualComment = objectMapper.readValue(response.body(), Comment.class);
        expectedComment.setId(actualComment.getId());
        ;
        Assertions.assertEquals(expectedComment, actualComment);
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

    @ParameterizedTest
    @CsvSource({
            "1"
    })
    public void deleteCommentTest(Integer commentId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/comments/" + commentId))
                .DELETE()
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assertions.assertEquals(200, status);
    }

    @ParameterizedTest
    @CsvSource({
            "1"
    })
    public void getNumberOfLikesOnCommenttByIdTest(Integer commentId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/comments/" + commentId + "/likes"))
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assertions.assertEquals(200, status);

        Integer expectedLikes = 1;
        Integer actualLikes = objectMapper.readValue(response.body(), Integer.class);
        Assertions.assertEquals(expectedLikes, actualLikes);
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
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(userAccount)))
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assertions.assertEquals(200, status);
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
                .method("DELETE", HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(userAccount)))
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assertions.assertEquals(200, status);
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
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(userAccount)))
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assertions.assertEquals(200, status);
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
                .method("DELETE", HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(userAccount)))
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assertions.assertEquals(200, status);
    }
}