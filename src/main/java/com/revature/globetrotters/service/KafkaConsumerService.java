package com.revature.globetrotters.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.globetrotters.entity.CommentLike;
import com.revature.globetrotters.entity.PostLike;
import com.revature.globetrotters.exception.NotFoundException;
import com.revature.globetrotters.exception.UnknownActionException;
import com.revature.globetrotters.repository.CommentLikeRepository;
import com.revature.globetrotters.repository.CommentRepository;
import com.revature.globetrotters.repository.PostLikeRepository;
import com.revature.globetrotters.repository.PostRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    CommentLikeRepository commentLikeRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostLikeRepository postLikeRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = "comments", groupId = "comment-service-group")
    public void consumeCommentEvent(String message) throws UnknownActionException {
        try {
            JsonNode event = objectMapper.readTree(message);
            String action = event.get("action").asText();
            JsonNode payload = event.get("payload");
            Integer userId = event.get("userId").asInt();

            switch (action) {
                case "like comment":
                    handleLikeCommentEvent(payload, userId);
                    break;
                case "unlike comment":
                    handleUnlikeCommentEvent(payload, userId);
                    break;
                case "like post":
                    handleLikePostEvent(payload, userId);
                    break;
                case "unlike post":
                    handleUnLikePostEvent(payload, userId);
                    break;
                default:
                    throw new UnknownActionException("Unknown action received: " + action);
            }

        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    private void handleLikeCommentEvent(JsonNode payload, Integer userId) {
        try {
            int commentId = payload.asInt();
            if (!commentRepository.existsById(commentId)) {
                throw new NotFoundException(String.format("Comment with ID %d does not exist.", commentId));
            }
            commentLikeRepository.save(new CommentLike(commentId, userId));
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    private void handleUnlikeCommentEvent(JsonNode payload, Integer userId) {
        try {
            int commentId = payload.asInt();
            if (!commentRepository.existsById(commentId)) {
                throw new NotFoundException(String.format("Comment with ID %d does not exist.", commentId));
            }
            commentLikeRepository.delete(new CommentLike(commentId, userId));
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    private void handleLikePostEvent(JsonNode payload, Integer userId) {
        try {
            int postId = payload.asInt();
            if (!postRepository.existsById(postId)) {
                throw new NotFoundException(String.format("Post with ID %d does not exist.", postId));
            }
            postLikeRepository.save(new PostLike(postId, userId));
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    private void handleUnLikePostEvent(JsonNode payload, Integer userId) {
        try {
            int postId = payload.asInt();
            if (!postRepository.existsById(postId)) {
                throw new NotFoundException(String.format("Post with ID %d does not exist.", postId));
            }
            postLikeRepository.delete(new PostLike(postId, userId));
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
}