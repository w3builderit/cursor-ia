package com.usermanager.domain.enums;

public enum ProfileType {
    PERSONAL("Personal"),
    PROFESSIONAL("Professional"),
    DEPARTMENTAL("Departmental"),
    PROJECT("Project"),
    ROLE_BASED("Role Based"),
    TEMPORARY("Temporary"),
    SYSTEM("System");

    private final String displayName;

    ProfileType(String displayName) {
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