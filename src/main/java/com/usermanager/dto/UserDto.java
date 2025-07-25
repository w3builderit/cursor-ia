package com.usermanager.dto;

import com.usermanager.domain.enums.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record UserDto(
    UUID id,
    
    @NotBlank(message = "Username is required")
    @Size(max = 100, message = "Username must not exceed 100 characters")
    String username,
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    String email,
    
    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    String firstName,
    
    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    String lastName,
    
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    String phoneNumber,
    
    String keycloakId,
    UserStatus status,
    Boolean emailVerified,
    LocalDateTime lastLoginAt,
    Integer loginAttempts,
    LocalDateTime lockedUntil,
    
    @Size(max = 1000, message = "Profile picture URL must not exceed 1000 characters")
    String profilePictureUrl,
    
    @Size(max = 500, message = "Bio must not exceed 500 characters")
    String bio,
    
    @Size(max = 100, message = "Department must not exceed 100 characters")
    String department,
    
    @Size(max = 100, message = "Position must not exceed 100 characters")
    String position,
    
    Set<RoleDto> roles,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Long version,
    Boolean active
) {

    // Helper methods
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean isLocked() {
        return lockedUntil != null && lockedUntil.isAfter(LocalDateTime.now());
    }

    public boolean isEmailVerified() {
        return Boolean.TRUE.equals(emailVerified);
    }

    public boolean isActive() {
        return Boolean.TRUE.equals(active);
    }
}