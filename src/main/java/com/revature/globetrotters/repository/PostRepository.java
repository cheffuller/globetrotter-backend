package com.revature.globetrotters.repository;

import com.revature.globetrotters.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    Optional<Post> findByPostedDate(LocalDateTime date);

    Optional<Post> findByTravelPlanId(Integer travelPlanId);

    @Query("""
            SELECT p
            FROM Post p
            JOIN TravelPlan tp
            ON p.travelPlanId = tp.id
            JOIN UserAccount ua
            ON tp.accountId = ua.id
            WHERE ua.id = :userId""")
    List<Post> findAllByUserId(@Param("userId") Integer userId);

    @Query("""
            SELECT new Post(
                p.id,
                p.postedDate,
                p.travelPlanId,
                ua.username
            ) FROM Post p
            JOIN TravelPlan tp
            ON p.travelPlanId = tp.id
            JOIN UserAccount ua
            ON ua.id = tp.accountId
            WHERE p.id = :id
            GROUP BY p.id
            """)
    Optional<Post> findByIdIncludingUsername(@Param("id") Integer id);
}