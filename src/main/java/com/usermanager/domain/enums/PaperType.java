package com.usermanager.domain.enums;

public enum PaperType {
    DOCUMENT("Document"),
    FORM("Form"),
    REPORT("Report"),
    TEMPLATE("Template"),
    POLICY("Policy"),
    PROCEDURE("Procedure"),
    MANUAL("Manual"),
    GUIDELINE("Guideline"),
    SPECIFICATION("Specification"),
    CONTRACT("Contract");

    private final String displayName;

    PaperType(String displayName) {
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