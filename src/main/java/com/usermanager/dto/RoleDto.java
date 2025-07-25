package com.usermanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record RoleDto(
    UUID id,
    
    @NotBlank(message = "Role name is required")
    @Size(max = 100, message = "Role name must not exceed 100 characters")
    String name,
    
    @NotBlank(message = "Role code is required")
    @Size(max = 50, message = "Role code must not exceed 50 characters")
    String code,
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    String description,
    
    Boolean systemRole,
    Set<PermissionDto> permissions,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Long version,
    Boolean active
) {

    // Alternative constructors
    public RoleDto(String name, String code) {
        this(null, name, code, null, null, null, null, null, null, null);
    }

    public RoleDto(String name, String code, String description) {
        this(null, name, code, description, null, null, null, null, null, null);
    }

    // Helper methods
    public boolean isSystemRole() {
        return Boolean.TRUE.equals(systemRole);
    }

    public boolean isActive() {
        return Boolean.TRUE.equals(active);
    }
}