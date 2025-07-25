package com.usermanager.service;

import com.usermanager.dto.UserDto;
import com.usermanager.domain.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    // CRUD operations
    UserDto createUser(UserDto userDto);
    
    Optional<UserDto> getUserById(UUID id);
    
    Optional<UserDto> getUserByUsername(String username);
    
    Optional<UserDto> getUserByEmail(String email);
    
    Optional<UserDto> getUserByKeycloakId(String keycloakId);
    
    UserDto updateUser(UUID id, UserDto userDto);
    
    void deleteUser(UUID id);
    
    void softDeleteUser(UUID id);
    
    void activateUser(UUID id);
    
    void deactivateUser(UUID id);

    // Search and listing
    Page<UserDto> getAllUsers(Pageable pageable);
    
    Page<UserDto> getActiveUsers(Pageable pageable);
    
    Page<UserDto> getUsersByStatus(UserStatus status, Pageable pageable);
    
    Page<UserDto> searchUsers(String searchTerm, Pageable pageable);
    
    List<UserDto> getUsersByDepartment(String department);
    
    List<UserDto> getUsersByPosition(String position);
    
    List<UserDto> getUsersByRole(String roleCode);

    // User status management
    void lockUser(UUID id, LocalDateTime until);
    
    void unlockUser(UUID id);
    
    void incrementLoginAttempts(UUID id);
    
    void resetLoginAttempts(UUID id);
    
    void updateLastLogin(UUID id);
    
    void verifyEmail(UUID id);

    // Role management
    void assignRole(UUID userId, UUID roleId);
    
    void assignRoles(UUID userId, List<UUID> roleIds);
    
    void removeRole(UUID userId, UUID roleId);
    
    void removeAllRoles(UUID userId);

    // Validation methods
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    boolean existsByKeycloakId(String keycloakId);

    // Statistics and reports
    long countUsersByStatus(UserStatus status);
    
    long countUsersCreatedBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<Object[]> getUserCountByDepartment();

    // Maintenance operations
    List<UserDto> findUsersNotLoggedInSince(LocalDateTime date);
    
    List<UserDto> findUnverifiedUsersCreatedBefore(LocalDateTime date);
    
    List<UserDto> findLockedUsers();
}