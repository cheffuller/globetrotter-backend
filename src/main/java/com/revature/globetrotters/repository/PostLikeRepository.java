package com.revature.globetrotters.repository;

import com.revature.globetrotters.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostLikeRepository extends JpaRepository<PostLike, PostLike.PostLikeId> {
    @Query("""
            SELECT COUNT(pl)
            FROM PostLike pl
            WHERE pl.postId = :postId""")
    Integer findNumberOfLikesByPostId(Integer postId);
}
