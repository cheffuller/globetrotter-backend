package com.revature.globetrotters.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.globetrotters.event.CommentEvent;

@Service
public class KafkaProducerService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    private static final String COMMENT_TOPIC = "comments";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;


    private String createMessage(String action, Object payload, Integer userId) throws JsonProcessingException {
        return objectMapper.writeValueAsString(new CommentEvent(action, payload, userId));
    }

    public void publishMessage(String action, Object payload, Integer userId) throws JsonProcessingException {
        String message = createMessage(action, payload, userId);
        kafkaTemplate.send(COMMENT_TOPIC, message);
    }

}
