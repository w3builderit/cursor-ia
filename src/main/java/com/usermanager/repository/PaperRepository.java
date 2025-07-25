package com.usermanager.repository;

import com.usermanager.domain.entity.Paper;
import com.usermanager.domain.entity.User;
import com.usermanager.domain.enums.PaperStatus;
import com.usermanager.domain.enums.PaperType;
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
public interface PaperRepository extends JpaRepository<Paper, UUID> {

    Optional<Paper> findByCode(String code);

    boolean existsByCode(String code);

    List<Paper> findByType(PaperType type);

    Page<Paper> findByType(PaperType type, Pageable pageable);

    List<Paper> findByStatus(PaperStatus status);

    Page<Paper> findByStatus(PaperStatus status, Pageable pageable);

    List<Paper> findByCategory(String category);

    Page<Paper> findByCategory(String category, Pageable pageable);

    List<Paper> findByCreatedBy(User createdBy);

    Page<Paper> findByCreatedBy(User createdBy, Pageable pageable);

    List<Paper> findByCreatedById(UUID createdById);

    Page<Paper> findByCreatedById(UUID createdById, Pageable pageable);

    List<Paper> findByIsLatestVersion(Boolean isLatestVersion);

    Page<Paper> findByIsLatestVersion(Boolean isLatestVersion, Pageable pageable);

    @Query("SELECT p FROM Paper p WHERE p.active = true")
    List<Paper> findAllActivePapers();

    @Query("SELECT p FROM Paper p WHERE p.active = true")
    Page<Paper> findAllActivePapers(Pageable pageable);

    @Query("SELECT p FROM Paper p WHERE p.status = :status AND p.active = true")
    Page<Paper> findByStatusAndActive(@Param("status") PaperStatus status, Pageable pageable);

    @Query("SELECT p FROM Paper p WHERE p.type = :type AND p.active = true")
    Page<Paper> findByTypeAndActive(@Param("type") PaperType type, Pageable pageable);

    @Query("SELECT p FROM Paper p WHERE p.category = :category AND p.active = true")
    Page<Paper> findByCategoryAndActive(@Param("category") String category, Pageable pageable);

    @Query("SELECT p FROM Paper p WHERE " +
           "(LOWER(p.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.code) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.category) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "p.active = true")
    Page<Paper> searchActivePapers(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT p FROM Paper p WHERE p.createdBy.id = :userId AND p.active = true")
    Page<Paper> findByCreatedByIdAndActive(@Param("userId") UUID userId, Pageable pageable);

    @Query("SELECT p FROM Paper p WHERE p.status = 'PUBLISHED' AND p.active = true AND " +
           "(p.expiresAt IS NULL OR p.expiresAt > CURRENT_TIMESTAMP)")
    Page<Paper> findPublishedAndNotExpired(Pageable pageable);

    @Query("SELECT p FROM Paper p WHERE p.expiresAt IS NOT NULL AND p.expiresAt < CURRENT_TIMESTAMP AND p.active = true")
    List<Paper> findExpiredPapers();

    @Query("SELECT p FROM Paper p WHERE p.publishedAt IS NOT NULL AND " +
           "p.publishedAt >= :startDate AND p.publishedAt <= :endDate AND p.active = true")
    List<Paper> findPublishedBetween(@Param("startDate") LocalDateTime startDate, 
                                     @Param("endDate") LocalDateTime endDate);

    @Query("SELECT p FROM Paper p WHERE p.isLatestVersion = true AND p.active = true")
    Page<Paper> findLatestVersionsOnly(Pageable pageable);

    @Query("SELECT p FROM Paper p WHERE p.parentPaperId = :parentPaperId AND p.active = true ORDER BY p.versionNumber DESC")
    List<Paper> findVersionsByParentPaperId(@Param("parentPaperId") String parentPaperId);

    @Query("SELECT COUNT(p) FROM Paper p WHERE p.status = :status AND p.active = true")
    long countByStatusAndActive(@Param("status") PaperStatus status);

    @Query("SELECT COUNT(p) FROM Paper p WHERE p.createdBy.id = :userId AND p.active = true")
    long countByCreatedByIdAndActive(@Param("userId") UUID userId);

    @Query("SELECT p.category, COUNT(p) FROM Paper p WHERE p.active = true GROUP BY p.category")
    List<Object[]> countPapersByCategory();

    @Query("SELECT p.type, COUNT(p) FROM Paper p WHERE p.active = true GROUP BY p.type")
    List<Object[]> countPapersByType();

    @Query("SELECT DISTINCT p.category FROM Paper p WHERE p.active = true ORDER BY p.category")
    List<String> findDistinctCategories();

    @Query("SELECT p FROM Paper p WHERE SIZE(p.requiredPermissions) = 0 AND p.active = true")
    List<Paper> findPapersWithoutPermissions();

    @Query("SELECT p FROM Paper p WHERE :permissionCode MEMBER OF p.requiredPermissions AND p.active = true")
    List<Paper> findByRequiredPermissionAndActive(@Param("permissionCode") String permissionCode);

    @Query("SELECT p FROM Paper p WHERE :tag MEMBER OF p.tags AND p.active = true")
    Page<Paper> findByTagAndActive(@Param("tag") String tag, Pageable pageable);

    @Query("SELECT p FROM Paper p WHERE p.active = true ORDER BY p.downloadCount DESC")
    Page<Paper> findMostDownloaded(Pageable pageable);

    @Query("SELECT p FROM Paper p WHERE p.active = true ORDER BY p.viewCount DESC")
    Page<Paper> findMostViewed(Pageable pageable);

    @Query("SELECT p FROM Paper p WHERE p.active = true ORDER BY p.publishedAt DESC")
    Page<Paper> findRecentlyPublished(Pageable pageable);
}