package com.revature.globetrotters.repository;

import com.revature.globetrotters.GlobeTrottersApplication;
import com.revature.globetrotters.entity.Comment;
import com.revature.globetrotters.util.DateArgumentConverter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.Date;

@SpringBootTest
public class CommentRepositoryTest {
    @Autowired
    CommentRepository commentRepository;
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

    @ParameterizedTest
    @CsvSource({
            "1, '2019-01-01', 1, 'content', 3"
    })
    public void findPostCommentByIdTest(Integer commentId, @ConvertWith(DateArgumentConverter.class) Date date,
                                    Integer postId, String content, Integer userId) {
        Comment expected = new Comment(commentId, date, postId, content, userId);
        Comment actual = commentRepository.findById(commentId).get();
        Assertions.assertEquals(expected, actual,
                "Expected: " + expected + ". Actual: " + actual);
    }
}
