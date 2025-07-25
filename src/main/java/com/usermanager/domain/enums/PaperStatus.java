package com.usermanager.domain.enums;

public enum PaperStatus {
    DRAFT("Draft"),
    PENDING_REVIEW("Pending Review"),
    UNDER_REVIEW("Under Review"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    PUBLISHED("Published"),
    ARCHIVED("Archived"),
    EXPIRED("Expired");

    private final String displayName;

    PaperStatus(String displayName) {
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