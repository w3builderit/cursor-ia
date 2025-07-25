package com.usermanager.repository;

import com.usermanager.domain.entity.Permission;
import com.usermanager.domain.enums.PermissionType;
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
public interface PermissionRepository extends JpaRepository<Permission, UUID> {

    Optional<Permission> findByCode(String code);

    boolean existsByCode(String code);

    List<Permission> findByType(PermissionType type);

    Page<Permission> findByType(PermissionType type, Pageable pageable);

    List<Permission> findByResource(String resource);

    Page<Permission> findByResource(String resource, Pageable pageable);

    List<Permission> findByResourceAndAction(String resource, String action);

    List<Permission> findBySystemPermission(Boolean systemPermission);

    Page<Permission> findBySystemPermission(Boolean systemPermission, Pageable pageable);

    @Query("SELECT p FROM Permission p WHERE p.active = true")
    List<Permission> findAllActivePermissions();

    @Query("SELECT p FROM Permission p WHERE p.active = true")
    Page<Permission> findAllActivePermissions(Pageable pageable);

    @Query("SELECT p FROM Permission p WHERE p.type = :type AND p.active = true")
    Page<Permission> findByTypeAndActive(@Param("type") PermissionType type, Pageable pageable);

    @Query("SELECT p FROM Permission p WHERE p.resource = :resource AND p.active = true")
    Page<Permission> findByResourceAndActive(@Param("resource") String resource, Pageable pageable);

    @Query("SELECT p FROM Permission p WHERE " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.code) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.resource) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.action) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "p.active = true")
    Page<Permission> searchActivePermissions(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT p FROM Permission p JOIN p.roles r WHERE r.id = :roleId AND p.active = true")
    List<Permission> findByRoleIdAndActive(@Param("roleId") UUID roleId);

    @Query("SELECT p FROM Permission p JOIN p.roles r WHERE r.code = :roleCode AND p.active = true")
    List<Permission> findByRoleCodeAndActive(@Param("roleCode") String roleCode);

    @Query("SELECT COUNT(p) FROM Permission p WHERE p.type = :type AND p.active = true")
    long countByTypeAndActive(@Param("type") PermissionType type);

    @Query("SELECT p.resource, COUNT(p) FROM Permission p WHERE p.active = true GROUP BY p.resource")
    List<Object[]> countPermissionsByResource();

    @Query("SELECT p FROM Permission p LEFT JOIN p.roles r WHERE p.active = true GROUP BY p HAVING COUNT(r) = 0")
    List<Permission> findPermissionsWithoutRoles();

    @Query("SELECT DISTINCT p.resource FROM Permission p WHERE p.active = true ORDER BY p.resource")
    List<String> findDistinctResources();

    @Query("SELECT DISTINCT p.action FROM Permission p WHERE p.resource = :resource AND p.active = true ORDER BY p.action")
    List<String> findDistinctActionsByResource(@Param("resource") String resource);
}