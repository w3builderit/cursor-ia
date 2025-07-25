package com.usermanager.repository;

import com.usermanager.domain.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByCode(String code);

    Optional<Role> findByName(String name);

    boolean existsByCode(String code);

    boolean existsByName(String name);

    List<Role> findBySystemRole(Boolean systemRole);

    Page<Role> findBySystemRole(Boolean systemRole, Pageable pageable);

    @Query("SELECT r FROM Role r WHERE r.active = true")
    List<Role> findAllActiveRoles();

    @Query("SELECT r FROM Role r WHERE r.active = true")
    Page<Role> findAllActiveRoles(Pageable pageable);

    @Query("SELECT r FROM Role r WHERE r.systemRole = :systemRole AND r.active = true")
    Page<Role> findBySystemRoleAndActive(@Param("systemRole") Boolean systemRole, Pageable pageable);

    @Query("SELECT r FROM Role r WHERE " +
           "(LOWER(r.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(r.code) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(r.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "r.active = true")
    Page<Role> searchActiveRoles(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT r FROM Role r JOIN r.permissions p WHERE p.code = :permissionCode AND r.active = true")
    List<Role> findByPermissionCodeAndActive(@Param("permissionCode") String permissionCode);

    @Query("SELECT r FROM Role r JOIN r.permissions p WHERE p.id = :permissionId AND r.active = true")
    List<Role> findByPermissionIdAndActive(@Param("permissionId") UUID permissionId);

    @Query("SELECT r FROM Role r JOIN r.users u WHERE u.id = :userId AND r.active = true")
    List<Role> findByUserIdAndActive(@Param("userId") UUID userId);

    @Query("SELECT COUNT(r) FROM Role r WHERE r.systemRole = :systemRole AND r.active = true")
    long countBySystemRoleAndActive(@Param("systemRole") Boolean systemRole);

    @Query("SELECT r FROM Role r LEFT JOIN r.permissions p WHERE r.active = true GROUP BY r HAVING COUNT(p) = 0")
    List<Role> findRolesWithoutPermissions();

    @Query("SELECT r FROM Role r LEFT JOIN r.users u WHERE r.active = true GROUP BY r HAVING COUNT(u) = 0")
    List<Role> findRolesWithoutUsers();
}