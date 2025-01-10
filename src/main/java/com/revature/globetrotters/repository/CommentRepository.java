package com.revature.globetrotters.repository;

import com.revature.globetrotters.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Optional<Comment> findByPostId(Integer postId);

    Optional<Comment> findByUserId(Integer madeBy);

    List<Comment> findAllByPostId(Integer postId);

    @Query("""
            SELECT COUNT(c.id)
            FROM Comment c
            WHERE c.postId = :postId
            """)
    Integer findNumberOfCommentsByPostId(@Param("postId") Integer postId);

    @Query("""
            SELECT new Comment(
                ua.username,
                c.userId,
                c.content,
                c.postId,
                c.commentedDate,
                c.id
            ) FROM Comment c
            JOIN UserAccount ua
            ON c.userId = ua.id
            WHERE c.id = :id
            """)
    Optional<Comment> findByIdIncludingUsername(@Param("id") Integer id);

    @Query("""
            SELECT new Comment(
                ua.username,
                c.userId,
                c.content,
                c.postId,
                c.commentedDate,
                c.id
            ) FROM Comment c
            JOIN UserAccount ua
            ON c.userId = ua.id
            JOIN Post p
            ON c.postId = p.id
            WHERE p.id = :postId""")
    List<Comment> findAllByPostIdIncludingUsername(@Param("postId") Integer postId);
}
