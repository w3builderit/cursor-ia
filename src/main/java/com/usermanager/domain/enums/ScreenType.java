package com.usermanager.domain.enums;

public enum ScreenType {
    PAGE("Page"),
    COMPONENT("Component"),
    MODAL("Modal"),
    DIALOG("Dialog"),
    FORM("Form"),
    REPORT("Report"),
    DASHBOARD("Dashboard");

    private final String displayName;

    ScreenType(String displayName) {
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