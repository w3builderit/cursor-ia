package com.usermanager.domain.enums;

public enum PermissionType {
    MENU("Menu Access"),
    SCREEN("Screen Access"),
    PAPER("Paper/Document Access"),
    PROFILE("Profile Access"),
    API("API Access"),
    FUNCTIONAL("Functional Permission");

    private final String displayName;

    PermissionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}