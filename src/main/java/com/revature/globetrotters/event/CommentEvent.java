package com.revature.globetrotters.event;

public class CommentEvent {
    private String action;
    private Object payload;
    private Integer userId;

    public CommentEvent(String action, Object payload, Integer userId) {
        this.action = action;
        this.payload = payload;
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public Object getPayload() {
        return payload;
    }

    public Integer getUserId() {
        return userId;
    }
}