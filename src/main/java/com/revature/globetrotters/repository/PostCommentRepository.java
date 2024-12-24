package com.revature.globetrotters.repository;

import com.revature.globetrotters.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Integer> {
    Optional<PostComment> findByCommentedDate(Date date);
    Optional<PostComment> findByPostId(Integer postId);
    Optional<PostComment> findByUserId(Integer madeBy);
}
