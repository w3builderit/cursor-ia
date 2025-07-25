package com.usermanager.dto;

import com.usermanager.domain.enums.PermissionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionDto {

    private UUID id;

    @NotBlank(message = "Permission code is required")
    @Size(max = 100, message = "Permission code must not exceed 100 characters")
    private String code;

    @NotBlank(message = "Permission name is required")
    @Size(max = 100, message = "Permission name must not exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Permission type is required")
    private PermissionType type;

    @NotBlank(message = "Resource is required")
    @Size(max = 100, message = "Resource must not exceed 100 characters")
    private String resource;

    @NotBlank(message = "Action is required")
    @Size(max = 50, message = "Action must not exceed 50 characters")
    private String action;

    private Boolean systemPermission;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;
    private Boolean active;

    // Custom constructor for business use
    public PermissionDto(String code, String name, PermissionType type, String resource, String action) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.resource = resource;
        this.action = action;
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