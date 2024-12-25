package com.revature.globetrotters.repository;

import com.revature.globetrotters.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, CommentLike.CommentLikeId> {
    @Query("""
            SELECT COUNT(cl)
            FROM CommentLike cl
            WHERE cl.id.commentId = :commentId""")
    Integer findNumberOfLikesByCommentId(@Param("commentId") Integer commentId);
}
