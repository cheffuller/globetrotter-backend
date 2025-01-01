package com.revature.globetrotters.enums;

public enum AccountRole {
    Customer("Customer"),
    Moderator("Moderator");

    private final String role;

    AccountRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
