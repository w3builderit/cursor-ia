package com.usermanager.service;

import com.usermanager.dto.UserDto;
import com.usermanager.domain.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserService {

    // CRUD operations
    UserDto createUser(UserDto userDto);
    
    Optional<UserDto> getUserById(String id);
    
    Optional<UserDto> getUserByUsername(String username);
    
    Optional<UserDto> getUserByEmail(String email);
    
    Optional<UserDto> getUserByKeycloakId(String keycloakId);
    
    UserDto updateUser(String id, UserDto userDto);
    
    void deleteUser(String id);
    
    void softDeleteUser(String id);
    
    void activateUser(String id);
    
    void deactivateUser(String id);

    // Search and listing
    Page<UserDto> getAllUsers(Pageable pageable);
    
    Page<UserDto> getActiveUsers(Pageable pageable);
    
    Page<UserDto> getUsersByStatus(UserStatus status, Pageable pageable);
    
    Page<UserDto> searchUsers(String searchTerm, Pageable pageable);
    
    List<UserDto> getUsersByDepartment(String department);
    
    List<UserDto> getUsersByPosition(String position);
    
    List<UserDto> getUsersByRole(String roleCode);

    // User status management
    void lockUser(String id, LocalDateTime until);
    
    void unlockUser(String id);
    
    void incrementLoginAttempts(String id);
    
    void resetLoginAttempts(String id);
    
    void updateLastLogin(String id);
    
    void verifyEmail(String id);

    // Role management
    void assignRole(String userId, String roleId);
    
    void assignRoles(String userId, List<String> roleIds);
    
    void removeRole(String userId, String roleId);
    
    void removeAllRoles(String userId);

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