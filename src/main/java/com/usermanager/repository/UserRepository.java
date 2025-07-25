package com.usermanager.repository;

import com.usermanager.domain.entity.User;
import com.usermanager.domain.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByKeycloakId(String keycloakId);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByKeycloakId(String keycloakId);

    List<User> findByStatus(UserStatus status);

    Page<User> findByStatus(UserStatus status, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.active = true")
    List<User> findAllActiveUsers();

    @Query("SELECT u FROM User u WHERE u.active = true")
    Page<User> findAllActiveUsers(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.status = :status AND u.active = true")
    Page<User> findByStatusAndActive(@Param("status") UserStatus status, Pageable pageable);

    @Query("SELECT u FROM User u WHERE " +
           "(LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "u.active = true")
    Page<User> searchActiveUsers(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.department = :department AND u.active = true")
    List<User> findByDepartmentAndActive(@Param("department") String department);

    @Query("SELECT u FROM User u WHERE u.position = :position AND u.active = true")
    List<User> findByPositionAndActive(@Param("position") String position);

    @Query("SELECT u FROM User u WHERE u.lastLoginAt < :date")
    List<User> findUsersNotLoggedInSince(@Param("date") LocalDateTime date);

    @Query("SELECT u FROM User u WHERE u.emailVerified = false AND u.createdAt < :date")
    List<User> findUnverifiedUsersCreatedBefore(@Param("date") LocalDateTime date);

    @Query("SELECT u FROM User u WHERE u.lockedUntil IS NOT NULL AND u.lockedUntil > CURRENT_TIMESTAMP")
    List<User> findLockedUsers();

    @Query("SELECT u FROM User u WHERE u.loginAttempts >= :maxAttempts")
    List<User> findUsersWithExcessiveLoginAttempts(@Param("maxAttempts") Integer maxAttempts);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.code = :roleCode AND u.active = true")
    List<User> findByRoleCodeAndActive(@Param("roleCode") String roleCode);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.id = :roleId AND u.active = true")
    Page<User> findByRoleIdAndActive(@Param("roleId") UUID roleId, Pageable pageable);

    @Query("SELECT COUNT(u) FROM User u WHERE u.status = :status AND u.active = true")
    long countByStatusAndActive(@Param("status") UserStatus status);

    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :startDate AND u.createdAt <= :endDate")
    long countUsersCreatedBetween(@Param("startDate") LocalDateTime startDate, 
                                  @Param("endDate") LocalDateTime endDate);

    @Query("SELECT u.department, COUNT(u) FROM User u WHERE u.active = true GROUP BY u.department")
    List<Object[]> countUsersByDepartment();

    @Query("SELECT u FROM User u WHERE u.active = true ORDER BY u.lastLoginAt DESC")
    Page<User> findRecentlyActiveUsers(Pageable pageable);
}