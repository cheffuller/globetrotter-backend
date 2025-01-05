package com.revature.globetrotters.enums;

public enum FollowingStatus {
    Following("Following"),
    FollowRequested("Follow Requested"),
    NotFollowing("Not Following");

    private final String status;

    FollowingStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
