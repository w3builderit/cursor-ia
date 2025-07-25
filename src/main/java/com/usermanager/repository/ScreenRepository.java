package com.usermanager.repository;

import com.usermanager.domain.entity.Screen;
import com.usermanager.domain.enums.ScreenType;
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
public interface ScreenRepository extends JpaRepository<Screen, UUID> {

    Optional<Screen> findByCode(String code);

    boolean existsByCode(String code);

    List<Screen> findByType(ScreenType type);

    Page<Screen> findByType(ScreenType type, Pageable pageable);

    List<Screen> findByModule(String module);

    Page<Screen> findByModule(String module, Pageable pageable);

    List<Screen> findByRoute(String route);

    List<Screen> findByPublicAccess(Boolean publicAccess);

    Page<Screen> findByPublicAccess(Boolean publicAccess, Pageable pageable);

    List<Screen> findByAuthRequired(Boolean authRequired);

    Page<Screen> findByAuthRequired(Boolean authRequired, Pageable pageable);

    @Query("SELECT s FROM Screen s WHERE s.active = true")
    List<Screen> findAllActiveScreens();

    @Query("SELECT s FROM Screen s WHERE s.active = true")
    Page<Screen> findAllActiveScreens(Pageable pageable);

    @Query("SELECT s FROM Screen s WHERE s.type = :type AND s.active = true")
    Page<Screen> findByTypeAndActive(@Param("type") ScreenType type, Pageable pageable);

    @Query("SELECT s FROM Screen s WHERE s.module = :module AND s.active = true")
    Page<Screen> findByModuleAndActive(@Param("module") String module, Pageable pageable);

    @Query("SELECT s FROM Screen s WHERE " +
           "(LOWER(s.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.code) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.module) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "s.active = true")
    Page<Screen> searchActiveScreens(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT s FROM Screen s WHERE s.publicAccess = :publicAccess AND s.active = true")
    Page<Screen> findByPublicAccessAndActive(@Param("publicAccess") Boolean publicAccess, Pageable pageable);

    @Query("SELECT s FROM Screen s WHERE s.authRequired = :authRequired AND s.active = true")
    Page<Screen> findByAuthRequiredAndActive(@Param("authRequired") Boolean authRequired, Pageable pageable);

    @Query("SELECT COUNT(s) FROM Screen s WHERE s.type = :type AND s.active = true")
    long countByTypeAndActive(@Param("type") ScreenType type);

    @Query("SELECT COUNT(s) FROM Screen s WHERE s.module = :module AND s.active = true")
    long countByModuleAndActive(@Param("module") String module);

    @Query("SELECT s.module, COUNT(s) FROM Screen s WHERE s.active = true GROUP BY s.module")
    List<Object[]> countScreensByModule();

    @Query("SELECT DISTINCT s.module FROM Screen s WHERE s.active = true ORDER BY s.module")
    List<String> findDistinctModules();

    @Query("SELECT s FROM Screen s WHERE s.cacheEnabled = true AND s.active = true")
    List<Screen> findCacheEnabledScreens();

    @Query("SELECT s FROM Screen s WHERE SIZE(s.requiredPermissions) = 0 AND s.active = true")
    List<Screen> findScreensWithoutPermissions();

    @Query("SELECT s FROM Screen s WHERE :permissionCode MEMBER OF s.requiredPermissions AND s.active = true")
    List<Screen> findByRequiredPermissionAndActive(@Param("permissionCode") String permissionCode);
}