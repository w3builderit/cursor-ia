package com.usermanager.controller;

import com.usermanager.domain.enums.UserStatus;
import com.usermanager.dto.UserDto;
import com.usermanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "API for managing users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "409", description = "User already exists")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER')")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto createdUser = userService.createUser(userDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all users with pagination")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'USER_VIEWER')")
    public ResponseEntity<Page<UserDto>> getAllUsers(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<UserDto> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'USER_VIEWER') or @userService.getUserById(#id).orElse(null)?.username == authentication.name")
    public ResponseEntity<UserDto> getUserById(@PathVariable String id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get current user profile")
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
        return userService.getUserByUsername(authentication.getName())
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update user")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER') or @userService.getUserById(#id).orElse(null)?.username == authentication.name")
    public ResponseEntity<UserDto> updateUser(@PathVariable String id, @Valid @RequestBody UserDto userDto) {
        try {
            UserDto updatedUser = userService.updateUser(id, userDto);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Update current user profile")
    @PutMapping("/me")
    public ResponseEntity<UserDto> updateCurrentUser(@Valid @RequestBody UserDto userDto, Authentication authentication) {
        return userService.getUserByUsername(authentication.getName())
                .map(currentUser -> {
                    UserDto updatedUser = userService.updateUser(currentUser.getId(), userDto);
                    return ResponseEntity.ok(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete user")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Soft delete user (deactivate)")
    @DeleteMapping("/{id}/soft")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER')")
    public ResponseEntity<Void> softDeleteUser(@PathVariable String id) {
        try {
            userService.softDeleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Activate user")
    @PostMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER')")
    public ResponseEntity<Void> activateUser(@PathVariable String id) {
        try {
            userService.activateUser(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Deactivate user")
    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER')")
    public ResponseEntity<Void> deactivateUser(@PathVariable String id) {
        try {
            userService.deactivateUser(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get active users")
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'USER_VIEWER')")
    public ResponseEntity<Page<UserDto>> getActiveUsers(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<UserDto> users = userService.getActiveUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get users by status")
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'USER_VIEWER')")
    public ResponseEntity<Page<UserDto>> getUsersByStatus(
            @PathVariable UserStatus status,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<UserDto> users = userService.getUsersByStatus(status, pageable);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Search users")
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'USER_VIEWER')")
    public ResponseEntity<Page<UserDto>> searchUsers(
            @RequestParam String searchTerm,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<UserDto> users = userService.searchUsers(searchTerm, pageable);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get users by department")
    @GetMapping("/department/{department}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'USER_VIEWER')")
    public ResponseEntity<List<UserDto>> getUsersByDepartment(@PathVariable String department) {
        List<UserDto> users = userService.getUsersByDepartment(department);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get users by position")
    @GetMapping("/position/{position}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'USER_VIEWER')")
    public ResponseEntity<List<UserDto>> getUsersByPosition(@PathVariable String position) {
        List<UserDto> users = userService.getUsersByPosition(position);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get users by role")
    @GetMapping("/role/{roleCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'USER_VIEWER')")
    public ResponseEntity<List<UserDto>> getUsersByRole(@PathVariable String roleCode) {
        List<UserDto> users = userService.getUsersByRole(roleCode);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Lock user account")
    @PostMapping("/{id}/lock")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER')")
    public ResponseEntity<Void> lockUser(@PathVariable String id, @RequestParam LocalDateTime until) {
        try {
            userService.lockUser(id, until);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Unlock user account")
    @PostMapping("/{id}/unlock")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER')")
    public ResponseEntity<Void> unlockUser(@PathVariable String id) {
        try {
            userService.unlockUser(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Verify user email")
    @PostMapping("/{id}/verify-email")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER') or @userService.getUserById(#id).orElse(null)?.username == authentication.name")
    public ResponseEntity<Void> verifyEmail(@PathVariable String id) {
        try {
            userService.verifyEmail(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Assign role to user")
    @PostMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER')")
    public ResponseEntity<Void> assignRole(@PathVariable String userId, @PathVariable String roleId) {
        try {
            userService.assignRole(userId, roleId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Assign multiple roles to user")
    @PostMapping("/{userId}/roles")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER')")
    public ResponseEntity<Void> assignRoles(@PathVariable String userId, @RequestBody List<String> roleIds) {
        try {
            userService.assignRoles(userId, roleIds);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Remove role from user")
    @DeleteMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER')")
    public ResponseEntity<Void> removeRole(@PathVariable String userId, @PathVariable String roleId) {
        try {
            userService.removeRole(userId, roleId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Remove all roles from user")
    @DeleteMapping("/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeAllRoles(@PathVariable String userId) {
        try {
            userService.removeAllRoles(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get user statistics")
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER')")
    public ResponseEntity<Object> getUserStatistics() {
        return ResponseEntity.ok(Map.of(
            "activeUsers", userService.countUsersByStatus(UserStatus.ACTIVE),
            "inactiveUsers", userService.countUsersByStatus(UserStatus.INACTIVE),
            "lockedUsers", userService.countUsersByStatus(UserStatus.LOCKED)
        ));
    }

    @Operation(summary = "Get users not logged in since date")
    @GetMapping("/inactive-since")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER')")
    public ResponseEntity<List<UserDto>> getUsersNotLoggedInSince(@RequestParam LocalDateTime date) {
        List<UserDto> users = userService.findUsersNotLoggedInSince(date);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get unverified users created before date")
    @GetMapping("/unverified-before")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER')")
    public ResponseEntity<List<UserDto>> getUnverifiedUsersCreatedBefore(@RequestParam LocalDateTime date) {
        List<UserDto> users = userService.findUnverifiedUsersCreatedBefore(date);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get locked users")
    @GetMapping("/locked")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER')")
    public ResponseEntity<List<UserDto>> getLockedUsers() {
        List<UserDto> users = userService.findLockedUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Check if username exists")
    @GetMapping("/check/username/{username}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER')")
    public ResponseEntity<Boolean> checkUsernameExists(@PathVariable String username) {
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }

    @Operation(summary = "Check if email exists")
    @GetMapping("/check/email/{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER')")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }
}