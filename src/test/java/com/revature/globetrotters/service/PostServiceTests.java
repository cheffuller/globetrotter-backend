package com.revature.globetrotters.service;
import com.revature.globetrotters.GlobeTrottersApplication;
import com.revature.globetrotters.entity.Comment;
import com.revature.globetrotters.entity.Post;
import com.revature.globetrotters.exception.NotFoundException;

import com.revature.globetrotters.exception.BadRequestException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;    
import org.springframework.context.ApplicationContext;


import com.revature.globetrotters.util.DateArgumentConverter;

import java.util.Date;
import java.util.List;

@SpringBootTest
public class PostServiceTests {
    @Autowired
    PostService postService;
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
    public void createPostTest() throws BadRequestException {
        DateArgumentConverter converter = new DateArgumentConverter();

        Post post = new Post(0, (Date) converter.convert("2020-01-01", null), 1);

        Post expectedPost = new Post(
            2,
            (Date) converter.convert("2020-01-01", null),
            1
        );

        Post actualPost = postService.createPost(post);
        Assertions.assertEquals(expectedPost, actualPost);
    }

    @ParameterizedTest
    @CsvSource({
        "1, '2019-01-01', 1"
    })
    public void findPostsByUserIdTest(int id, @ConvertWith(DateArgumentConverter.class) Date date, Integer travelPlanId) throws NotFoundException {
        List<Post> expectedPosts = List.of(new Post(id, date, travelPlanId));
        List<Post> actualPosts = postService.findPostsByUserId(1);
        Assertions.assertEquals(expectedPosts, actualPosts);
    }

    @ParameterizedTest
    @CsvSource({
        "1, '2019-01-01', 1"
    })
    public void findPostByIdTest(int id, @ConvertWith(DateArgumentConverter.class) Date date, Integer travelPlanId) throws NotFoundException {
        Post expectedPost = new Post(id, date, travelPlanId);
        
        Post actualPost = postService.findPostById(id);
        Assertions.assertEquals(expectedPost, actualPost);
    }

    @Test
    public void deletePostTest() throws NotFoundException {
        postService.deletePost(1);
        Assertions.assertThrows(NotFoundException.class, () -> postService.findPostById(1));
    }

    @ParameterizedTest
    @CsvSource({
        "1, 3"
    })
    public void getPostLikesTest(int postId, int userId) throws NotFoundException {
        int expectedLike = 1;
        
        Integer actualLiked = postService.getNumberOfLikesOnPostById(postId);

        Assertions.assertEquals(expectedLike, actualLiked);
    }
    
    @Test
    public void likePostTest() throws NotFoundException {
        postService.likePost(1, 1);
        int expectedLike = 2;
        Integer actualLiked = postService.getNumberOfLikesOnPostById(1);
        Assertions.assertEquals(expectedLike, actualLiked);
    }

    @Test
    public void unlikePostTest() throws BadRequestException, NotFoundException {
        postService.unlikePost(1, 3);
        int expectedLike = 0;
        Integer actualLiked = postService.getNumberOfLikesOnPostById(1);
        Assertions.assertEquals(expectedLike, actualLiked);
    }

    @ParameterizedTest
    @CsvSource({
        "1, '2019-01-01', 1, 'content', 3"
    })
    public void findCommentsByPostIdTest(Integer id, @ConvertWith(DateArgumentConverter.class) Date date, Integer postId, String content, Integer userId) throws NotFoundException {
        List<Comment> expectedComments = List.of(new Comment(id, date, postId, content, userId));
        List<Comment> actualComments = postService.findCommentsByPostId(1);
        Assertions.assertEquals(expectedComments, actualComments);
    }

    @Test
    public void postCommentTest() throws BadRequestException {
        DateArgumentConverter converter = new DateArgumentConverter();

        Comment comment = new Comment(null, (Date) converter.convert("2019-01-02", null), 1, "more-content", 3);

        Comment expectedComment = new Comment(
            2,
            (Date) converter.convert("2019-01-02", null),
            1,
            "more-content",
            3
        );

        Comment actualComment = postService.postComment(comment);
        Assertions.assertEquals(expectedComment, actualComment);
    }

    @ParameterizedTest
    @CsvSource({
        "1, '2019-01-01', 1, 'content', 3"
    })
    public void findCommentByIdTest(Integer id, @ConvertWith(DateArgumentConverter.class) Date date, Integer postId, String content, Integer userId) throws NotFoundException {
        Comment expectedComment = new Comment(id, date, postId, content, userId);
        Comment actualComment = postService.findCommentById(id);
        Assertions.assertEquals(expectedComment, actualComment);
    }

    @Test
    public void deleteCommentTest() throws Exception {
        postService.deleteComment(1);
        Assertions.assertThrows(NotFoundException.class, () -> postService.findCommentById(1));
    }

    @ParameterizedTest
    @CsvSource({
        "1, 3"
    })
    public void getCommentLikesTest(Integer commentId, Integer userId) throws NotFoundException {
        int expectedLike = 1;
        Integer actualLiked = postService.getNumberOfLikesOnCommentById(commentId);
        Assertions.assertEquals(expectedLike, actualLiked);
    }
    
    @Test
    public void likeCommentTest() throws NotFoundException {
        postService.likeComment(1, 1);
        int expectedLike = 2;
        Integer actualLiked = postService.getNumberOfLikesOnCommentById(1);
        Assertions.assertEquals(expectedLike, actualLiked);
    }

    @Test
    public void unlikeCommentTest() throws BadRequestException, NotFoundException {
        postService.unlikeComment(1, 3);
        int expectedLike = 0;
        Integer actualLiked = postService.getNumberOfLikesOnCommentById(1);
        Assertions.assertEquals(expectedLike, actualLiked);
    }
}
