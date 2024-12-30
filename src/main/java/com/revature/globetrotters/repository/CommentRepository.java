package com.revature.globetrotters.repository;

import com.revature.globetrotters.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Optional<Comment> findByCommentedDate(Date date);

    Optional<Comment> findByPostId(Integer postId);

    Optional<Comment> findByUserId(Integer madeBy);

    List<Comment> findAllByPostId(Integer postId);

    @Query("""
            SELECT COUNT(c)
            FROM Comment c
            WHERE c.postId = :postId
            """)
    Integer findNumberOfCommentsByPostId(@Param("postId") Integer postId);
}
