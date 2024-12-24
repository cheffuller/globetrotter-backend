package com.revature.globetrotters.repository;

import com.revature.globetrotters.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    Optional<Post> findByPostedDate(Date date);

    Optional<Post> findByTravelPlan(Integer travelPlan);

    @Query("""
            SELECT p 
            FROM Post p
            JOIN TravelPlan tp 
            ON p.travelPlanId = tp.id
            JOIN UserAccount ua
            ON tp.accountId = ua.id
            WHERE ua.id = :userId""")
    List<Post> findAllByUserId(@Param("userId") Integer userId);
}