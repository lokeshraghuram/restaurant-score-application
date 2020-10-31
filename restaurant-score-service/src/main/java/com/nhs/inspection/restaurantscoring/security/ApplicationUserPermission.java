package com.nhs.inspection.restaurantscoring.security;

public enum ApplicationUserPermission {
    SCORE_READ("score:read"),
    SCORE_WRITE("score:write");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
