package com.usermanager.dto;

import com.usermanager.domain.enums.PermissionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record PermissionDto(
    UUID id,
    
    @NotBlank(message = "Permission code is required")
    @Size(max = 100, message = "Permission code must not exceed 100 characters")
    String code,
    
    @NotBlank(message = "Permission name is required")
    @Size(max = 100, message = "Permission name must not exceed 100 characters")
    String name,
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    String description,
    
    @NotNull(message = "Permission type is required")
    PermissionType type,
    
    @NotBlank(message = "Resource is required")
    @Size(max = 100, message = "Resource must not exceed 100 characters")
    String resource,
    
    @NotBlank(message = "Action is required")
    @Size(max = 50, message = "Action must not exceed 50 characters")
    String action,
    
    Boolean systemPermission,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Long version,
    Boolean active
) {

    // Alternative constructors
    public PermissionDto(String code, String name, PermissionType type, String resource, String action) {
        this(null, code, name, null, type, resource, action, null, null, null, null, null);
    }

    // Helper methods
    public boolean isSystemPermission() {
        return Boolean.TRUE.equals(systemPermission);
    }

    public boolean isActive() {
        return Boolean.TRUE.equals(active);
    }

    public String getFullCode() {
        return resource + ":" + action;
    }
}