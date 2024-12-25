package com.revature.globetrotters.repository;

import com.revature.globetrotters.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, PostLike.PostLikeId> {
    @Query("""
            SELECT COUNT(pl)
            FROM PostLike pl
            WHERE pl.id.postId = :postId""")
    Integer findNumberOfLikesByPostId(@Param("postId") Integer postId);
}
