package com.revature.globetrotters.service;

import com.revature.globetrotters.GlobeTrottersApplication;
import com.revature.globetrotters.entity.Comment;
import com.revature.globetrotters.entity.Post;
import com.revature.globetrotters.exception.BadRequestException;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.exception.UnauthorizedException;
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
import java.util.Date;
import java.util.List;

import static com.revature.globetrotters.util.DateArgumentConverter.convertToDate;
import static com.revature.globetrotters.util.SecurityUtils.setUpSecurityContextHolder;

@SpringBootTest
public class PostServiceTests {
    @Autowired
    private AuthenticationTokenService authenticationTokenService;
    @Autowired
    private PostService postService;
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
                new Post(1, convertToDate("2019-01-01"), 1),
                new Post(5, convertToDate("2019-01-01"), 5));
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

    @Test
    public void likePostTest() throws NotFoundException {
        setUpSecurityContextHolder("john_doe", authenticationTokenService);
        postService.likePost(1);
        int expectedLike = 2;
        Long actualLiked = postService.getNumberOfLikesOnPostById(1);
        Assertions.assertEquals(expectedLike, actualLiked);
    }

    @Test
    public void unlikePostTest() throws BadRequestException, NotFoundException {
        setUpSecurityContextHolder("clark_kent", authenticationTokenService);
        postService.unlikePost(1);
        int expectedLike = 0;
        Long actualLiked = postService.getNumberOfLikesOnPostById(1);
        Assertions.assertEquals(expectedLike, actualLiked);
    }

    @Test
    public void findCommentsByPostIdTest() throws NotFoundException, ParseException {
        List<Comment> expectedComments = List.of(
                new Comment(1, convertToDate("2019-01-01"), 1, "WOW! This trip looks amazing!", 3));
        List<Comment> actualComments = postService.findCommentsByPostId(1);
        Assertions.assertEquals(expectedComments, actualComments);
    }

    @ParameterizedTest
    @CsvSource({
            "1, '2019-01-01', 1, 'WOW! This trip looks amazing!', 3, 'clark_kent'"
    })
    public void findCommentByIdTest(Integer id, @ConvertWith(DateArgumentConverter.class) Date date, Integer postId,
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
    public void likeCommentTest() throws NotFoundException {
        setUpSecurityContextHolder("john_doe", authenticationTokenService);
        postService.likeComment(1);
        int expectedLike = 2;
        Integer actualLiked = postService.getNumberOfLikesOnCommentById(1);
        Assertions.assertEquals(expectedLike, actualLiked);
    }

    @Test
    public void unlikeCommentTest() throws BadRequestException, NotFoundException {
        setUpSecurityContextHolder("clark_kent", authenticationTokenService);
        postService.unlikeComment(1);
        int expectedLike = 0;
        Integer actualLiked = postService.getNumberOfLikesOnCommentById(1);
        Assertions.assertEquals(expectedLike, actualLiked);
    }
}
