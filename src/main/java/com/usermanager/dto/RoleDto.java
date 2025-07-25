package com.usermanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDto {

    private UUID id;

    @NotBlank(message = "Role name is required")
    @Size(max = 100, message = "Role name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Role code is required")
    @Size(max = 50, message = "Role code must not exceed 50 characters")
    private String code;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private Boolean systemRole;
    private Set<PermissionDto> permissions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;
    private Boolean active;

    // Custom constructors for business use
    public RoleDto(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public RoleDto(String name, String code, String description) {
        this.name = name;
        this.code = code;
        this.description = description;
    }

    // Helper methods
    public boolean isSystemRole() {
        return Boolean.TRUE.equals(systemRole);
    }

    public boolean isActive() {
        return Boolean.TRUE.equals(active);
    }
}