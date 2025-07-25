package com.usermanager.repository;

import com.usermanager.domain.entity.Menu;
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
public interface MenuRepository extends JpaRepository<Menu, UUID> {

    Optional<Menu> findByCode(String code);

    boolean existsByCode(String code);

    List<Menu> findByParentIsNull();

    List<Menu> findByParentIsNullOrderByDisplayOrder();

    List<Menu> findByParentId(UUID parentId);

    List<Menu> findByParentIdOrderByDisplayOrder(UUID parentId);

    List<Menu> findByVisible(Boolean visible);

    Page<Menu> findByVisible(Boolean visible, Pageable pageable);

    List<Menu> findByLevel(Integer level);

    @Query("SELECT m FROM Menu m WHERE m.active = true")
    List<Menu> findAllActiveMenus();

    @Query("SELECT m FROM Menu m WHERE m.active = true")
    Page<Menu> findAllActiveMenus(Pageable pageable);

    @Query("SELECT m FROM Menu m WHERE m.parent IS NULL AND m.active = true ORDER BY m.displayOrder")
    List<Menu> findRootMenusActive();

    @Query("SELECT m FROM Menu m WHERE m.parent.id = :parentId AND m.active = true ORDER BY m.displayOrder")
    List<Menu> findByParentIdAndActive(@Param("parentId") UUID parentId);

    @Query("SELECT m FROM Menu m WHERE m.visible = :visible AND m.active = true")
    Page<Menu> findByVisibleAndActive(@Param("visible") Boolean visible, Pageable pageable);

    @Query("SELECT m FROM Menu m WHERE " +
           "(LOWER(m.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(m.code) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(m.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "m.active = true")
    Page<Menu> searchActiveMenus(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT m FROM Menu m WHERE m.requiredPermission = :permissionCode AND m.active = true")
    List<Menu> findByRequiredPermissionAndActive(@Param("permissionCode") String permissionCode);

    @Query("SELECT m FROM Menu m WHERE m.level = :level AND m.active = true ORDER BY m.displayOrder")
    List<Menu> findByLevelAndActive(@Param("level") Integer level);

    @Query("SELECT COUNT(m) FROM Menu m WHERE m.parent.id = :parentId AND m.active = true")
    long countByParentIdAndActive(@Param("parentId") UUID parentId);

    @Query("SELECT m FROM Menu m WHERE m.parent IS NULL AND m.visible = true AND m.active = true ORDER BY m.displayOrder")
    List<Menu> findVisibleRootMenus();

    @Query("SELECT m FROM Menu m WHERE m.parent.id = :parentId AND m.visible = true AND m.active = true ORDER BY m.displayOrder")
    List<Menu> findVisibleChildMenus(@Param("parentId") UUID parentId);

    @Query("SELECT m FROM Menu m WHERE m.url IS NOT NULL AND m.active = true")
    List<Menu> findMenusWithUrl();

    @Query("SELECT MAX(m.displayOrder) FROM Menu m WHERE m.parent.id = :parentId AND m.active = true")
    Integer findMaxDisplayOrderByParentId(@Param("parentId") UUID parentId);

    @Query("SELECT MAX(m.displayOrder) FROM Menu m WHERE m.parent IS NULL AND m.active = true")
    Integer findMaxDisplayOrderForRootMenus();
}