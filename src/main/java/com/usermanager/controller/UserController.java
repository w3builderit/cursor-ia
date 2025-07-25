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
import java.util.UUID;

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
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
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
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID id, @Valid @RequestBody UserDto userDto) {
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
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
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
    public ResponseEntity<Void> activateUser(@PathVariable UUID id) {
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
    public ResponseEntity<Void> deactivateUser(@PathVariable UUID id) {
        try {
            userService.deactivateUser(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Search users")
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'USER_VIEWER')")
    public ResponseEntity<Page<UserDto>> searchUsers(
            @Parameter(description = "Search term") @RequestParam String q,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<UserDto> users = userService.searchUsers(q, pageable);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get users by status")
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'USER_VIEWER')")
    public ResponseEntity<Page<UserDto>> getUsersByStatus(
            @PathVariable UserStatus status,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<UserDto> users = userService.getUsersByStatus(status, pageable);
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

    @Operation(summary = "Lock user")
    @PostMapping("/{id}/lock")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER')")
    public ResponseEntity<Void> lockUser(
            @PathVariable UUID id,
            @RequestParam(required = false) LocalDateTime until) {
        try {
            LocalDateTime lockUntil = until != null ? until : LocalDateTime.now().plusHours(24);
            userService.lockUser(id, lockUntil);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Unlock user")
    @PostMapping("/{id}/unlock")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER')")
    public ResponseEntity<Void> unlockUser(@PathVariable UUID id) {
        try {
            userService.unlockUser(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Verify user email")
    @PostMapping("/{id}/verify-email")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER')")
    public ResponseEntity<Void> verifyEmail(@PathVariable UUID id) {
        try {
            userService.verifyEmail(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Assign role to user")
    @PostMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Void> assignRole(@PathVariable UUID userId, @PathVariable UUID roleId) {
        try {
            userService.assignRole(userId, roleId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Assign multiple roles to user")
    @PostMapping("/{userId}/roles")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Void> assignRoles(@PathVariable UUID userId, @RequestBody List<UUID> roleIds) {
        try {
            userService.assignRoles(userId, roleIds);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Remove role from user")
    @DeleteMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Void> removeRole(@PathVariable UUID userId, @PathVariable UUID roleId) {
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
    public ResponseEntity<Void> removeAllRoles(@PathVariable UUID userId) {
        try {
            userService.removeAllRoles(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get user count by status")
    @GetMapping("/stats/count-by-status")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER')")
    public ResponseEntity<Long> countUsersByStatus(@RequestParam UserStatus status) {
        long count = userService.countUsersByStatus(status);
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Get user count by department")
    @GetMapping("/stats/count-by-department")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER')")
    public ResponseEntity<List<Object[]>> getUserCountByDepartment() {
        List<Object[]> stats = userService.getUserCountByDepartment();
        return ResponseEntity.ok(stats);
    }

    @Operation(summary = "Get inactive users")
    @GetMapping("/maintenance/inactive")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER')")
    public ResponseEntity<List<UserDto>> getInactiveUsers(@RequestParam int daysSince) {
        LocalDateTime date = LocalDateTime.now().minusDays(daysSince);
        List<UserDto> users = userService.findUsersNotLoggedInSince(date);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get unverified users")
    @GetMapping("/maintenance/unverified")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER')")
    public ResponseEntity<List<UserDto>> getUnverifiedUsers(@RequestParam int daysSince) {
        LocalDateTime date = LocalDateTime.now().minusDays(daysSince);
        List<UserDto> users = userService.findUnverifiedUsersCreatedBefore(date);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get locked users")
    @GetMapping("/maintenance/locked")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER')")
    public ResponseEntity<List<UserDto>> getLockedUsers() {
        List<UserDto> users = userService.findLockedUsers();
        return ResponseEntity.ok(users);
    }
}