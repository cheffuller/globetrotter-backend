package com.revature.globetrotters.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.revature.globetrotters.entity.Post;
import com.revature.globetrotters.entity.PostComment;
import com.revature.globetrotters.entity.UserAccount;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Integer> {

    @Query("SELECT pc FROM PostComment pc WHERE pc.id = :commentId")
    PostComment getPostCommentById(@Param("commentId") Integer commentId);

    @Query("SELECT pc FROM PostComment pc WHERE pc.post.id = :postId")
    List<PostComment> getCommentsByPostId(@Param("postId") Integer postId);

    @Query("SELECT pc FROM PostComment pc WHERE pc.userAccount.id = :userId")
    List<PostComment> getCommentsByUserId(@Param("userId") Integer userId);

    @Query("SELECT pc FROM PostComment pc WHERE LOWER(pc.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<PostComment> getCommentsByKeyword(@Param("keyword") String keyword);

    @Query("SELECT pc FROM PostComment pc WHERE pc.post = :post")
    List<PostComment> findByPost(@Param("post") Post post);

    @Query("SELECT pc FROM PostComment pc WHERE pc.userAccount = :userAccount")
    List<PostComment> findByUserAccount(@Param("userAccount") UserAccount userAccount);
}
