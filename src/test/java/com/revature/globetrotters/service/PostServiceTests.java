package com.revature.globetrotters.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.revature.globetrotters.GlobeTrottersApplication;
import com.revature.globetrotters.controller.CommentController;
import com.revature.globetrotters.controller.PostController;
import com.revature.globetrotters.entity.Comment;
import com.revature.globetrotters.entity.Post;
import com.revature.globetrotters.exception.BadRequestException;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.exception.UnauthorizedException;
import com.revature.globetrotters.repository.CommentLikeRepository;
import com.revature.globetrotters.repository.PostLikeRepository;
import com.revature.globetrotters.security.UserAuthenticationToken;
import com.revature.globetrotters.util.DateArgumentConverter;
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
import org.springframework.security.core.context.SecurityContextHolder;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;

import static com.revature.globetrotters.util.DateArgumentConverter.convertToLocalDateTime;
import static com.revature.globetrotters.util.SecurityUtils.setUpSecurityContextHolder;

@SpringBootTest
public class PostServiceTests {
    @Autowired
    private AuthenticationTokenService authenticationTokenService;
    @Autowired
    private PostService postService;
    @Autowired
    private PostController postController;
    @Autowired
    private PostLikeRepository postLikeRepository;
    @Autowired
    private CommentController commentController;
    @Autowired
    private CommentLikeRepository commentLikeRepository;

    private ApplicationContext app;

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
    public void createPostTest() throws BadRequestException, UnauthorizedException, NotFoundException {
        setUpSecurityContextHolder("john_doe", authenticationTokenService);
        Post post = new Post(1);
        Post actualPost = postService.createPost(post);
        Post expectedPost = new Post(7,1);
        expectedPost.setPostedDate(actualPost.getPostedDate()); // Since postedDate is set automatically on creation
        Assertions.assertEquals(expectedPost, actualPost);
    }

    @Test
    public void findPostsByUserIdTest() throws NotFoundException, ParseException {
        List<Post> expectedPosts = List.of(
                new Post(1, convertToLocalDateTime("2019-01-01"), 1),
                new Post(5, convertToLocalDateTime("2019-01-01"), 5));
        List<Post> actualPosts = postService.findPostsByUserId(1);
        Assertions.assertEquals(expectedPosts, actualPosts);
    }

    @ParameterizedTest
    @CsvSource({
            "1, '2019-01-01', 1"
    })
    public void findPostByIdTest(int id, @ConvertWith(DateArgumentConverter.class) LocalDateTime date, Integer travelPlanId) throws NotFoundException {
        Post expectedPost = new Post(id, date, travelPlanId);

        Post actualPost = postService.findPostById(id);
        Assertions.assertEquals(expectedPost, actualPost);
    }

    @Test
    public void deletePostTest() throws NotFoundException, UnauthorizedException {
        setUpSecurityContextHolder("john_doe", authenticationTokenService);
        postService.deletePost(1);
        Assertions.assertThrows(NotFoundException.class, () -> postService.findPostById(1));
    }

    @ParameterizedTest
    @CsvSource({
            "1, 3"
    })
    public void getPostLikesTest(int postId, int userId) throws NotFoundException {
        int expectedLike = 1;

        Long actualLiked = postService.getNumberOfLikesOnPostById(postId);

        Assertions.assertEquals(expectedLike, actualLiked);
    }

    @Test void likePostTest() throws NotFoundException, JsonProcessingException {
        setUpSecurityContextHolder("john_doe", authenticationTokenService);
        postController.likePost(1);
        int expectedLike = 2;
        try {
            Thread.sleep(2000); // Pause for 2 seconds to allow for kafka event to process
        } catch (InterruptedException e) {
            System.err.println("Interrupted: " + e.getMessage());
        }
        Long actualLiked = postLikeRepository.findNumberOfLikesByPostId(1);
        Assertions.assertEquals(expectedLike, actualLiked);
    }

    @Test void unlikePostTest() throws BadRequestException, NotFoundException, JsonProcessingException {
        setUpSecurityContextHolder("clark_kent", authenticationTokenService);
        postController.unlikePost(1);
        int expectedLike = 0;
        try {
            Thread.sleep(2000); // Pause for 2 seconds to allow for kafka event to process
        } catch (InterruptedException e) {
            System.err.println("Interrupted: " + e.getMessage());
        }
        Long actualLiked = postLikeRepository.findNumberOfLikesByPostId(1);
        Assertions.assertEquals(expectedLike, actualLiked);
    }

    @Test
    public void findCommentsByPostIdTest() throws NotFoundException, ParseException {
        List<Comment> expectedComments = List.of(
                new Comment(1, convertToLocalDateTime("2019-01-01"), 1, "WOW! This trip looks amazing!", 3));
        List<Comment> actualComments = postService.findCommentsByPostId(1);
        Assertions.assertEquals(expectedComments, actualComments);
    }

    @ParameterizedTest
    @CsvSource({
            "1, '2019-01-01', 1, 'WOW! This trip looks amazing!', 3, 'clark_kent'"
    })
    public void findCommentByIdTest(Integer id, @ConvertWith(DateArgumentConverter.class) LocalDateTime date, Integer postId,
                                    String content, Integer userId, String username)
            throws NotFoundException {
        setUpSecurityContextHolder(username, authenticationTokenService);
        Comment expectedComment = new Comment(id, date, postId, content, userId);
        Comment actualComment = postService.findCommentById(id);
        Assertions.assertEquals(expectedComment, actualComment);
    }

    @Test
    public void deleteCommentTest() throws Exception {
        setUpSecurityContextHolder("clark_kent", authenticationTokenService);
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
    public void likeCommentTest() throws NotFoundException, JsonProcessingException {
        setUpSecurityContextHolder("john_doe", authenticationTokenService);
        commentController.likeComment(1);
        int expectedLike = 2;
        try {
            Thread.sleep(2000); // Pause for 2 seconds to allow for kafka event to process
        } catch (InterruptedException e) {
            System.err.println("Interrupted: " + e.getMessage());
        }
        Integer actualLiked = commentLikeRepository.findNumberOfLikesByCommentId(1);
        Assertions.assertEquals(expectedLike, actualLiked);
    }

    @Test
    public void unlikeCommentTest() throws NotFoundException, JsonProcessingException {
        setUpSecurityContextHolder("clark_kent", authenticationTokenService);
        commentController.unlikeComment(1);
        int expectedLike = 0;
        try {
            Thread.sleep(2000); // Pause for 2 seconds to allow for kafka event to process
        } catch (InterruptedException e) {
            System.err.println("Interrupted: " + e.getMessage());
        }
        Integer actualLiked = commentLikeRepository.findNumberOfLikesByCommentId(1);
        Assertions.assertEquals(expectedLike, actualLiked);
    }
}
