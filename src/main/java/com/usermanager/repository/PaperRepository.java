package com.usermanager.repository;

import com.usermanager.domain.entity.Paper;
import com.usermanager.domain.entity.User;
import com.usermanager.domain.enums.PaperStatus;
import com.usermanager.domain.enums.PaperType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaperRepository extends MongoRepository<Paper, String> {

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

    @Query("{'createdBy.$id': ?0}")
    List<Paper> findByCreatedById(String createdById);

    @Query("{'createdBy.$id': ?0}")
    Page<Paper> findByCreatedById(String createdById, Pageable pageable);

    List<Paper> findByIsLatestVersion(Boolean isLatestVersion);

    Page<Paper> findByIsLatestVersion(Boolean isLatestVersion, Pageable pageable);

    @Query("{'active': true}")
    List<Paper> findAllActivePapers();

    @Query("{'active': true}")
    Page<Paper> findAllActivePapers(Pageable pageable);

    @Query("{'status': ?0, 'active': true}")
    Page<Paper> findByStatusAndActive(PaperStatus status, Pageable pageable);

    @Query("{'type': ?0, 'active': true}")
    Page<Paper> findByTypeAndActive(PaperType type, Pageable pageable);

    @Query("{'category': ?0, 'active': true}")
    Page<Paper> findByCategoryAndActive(String category, Pageable pageable);

    @Query("{'$and': [" +
           "{'$or': [" +
           "{'title': {'$regex': ?0, '$options': 'i'}}, " +
           "{'code': {'$regex': ?0, '$options': 'i'}}, " +
           "{'description': {'$regex': ?0, '$options': 'i'}}, " +
           "{'category': {'$regex': ?0, '$options': 'i'}}" +
           "]}, " +
           "{'active': true}" +
           "]}")
    Page<Paper> searchActivePapers(String searchTerm, Pageable pageable);

    @Query("{'createdBy.$id': ?0, 'active': true}")
    Page<Paper> findByCreatedByIdAndActive(String userId, Pageable pageable);

    @Query("{'status': 'PUBLISHED', 'active': true, '$or': [{'expiresAt': null}, {'expiresAt': {'$gt': ?#{new java.util.Date()}}}]}")
    Page<Paper> findPublishedAndNotExpired(Pageable pageable);

    @Query("{'expiresAt': {'$ne': null, '$lt': ?#{new java.util.Date()}}, 'active': true}")
    List<Paper> findExpiredPapers();

    @Query("{'publishedAt': {'$ne': null, '$gte': ?0, '$lte': ?1}, 'active': true}")
    List<Paper> findPublishedBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("{'isLatestVersion': true, 'active': true}")
    Page<Paper> findLatestVersionsOnly(Pageable pageable);

    @Query(value = "{'parentPaperId': ?0, 'active': true}", sort = "{'versionNumber': -1}")
    List<Paper> findVersionsByParentPaperId(String parentPaperId);

    @Query(value = "{'status': ?0, 'active': true}", count = true)
    long countByStatusAndActive(PaperStatus status);

    @Query(value = "{'createdBy.$id': ?0, 'active': true}", count = true)
    long countByCreatedByIdAndActive(String userId);

    @Query(value = "{'active': true}", fields = "{'category': 1, '_id': 0}")
    List<String> findDistinctCategories();

    @Query("{'requiredPermissions': {'$size': 0}, 'active': true}")
    List<Paper> findPapersWithoutPermissions();

    @Query("{'requiredPermissions': ?0, 'active': true}")
    List<Paper> findByRequiredPermissionAndActive(String permissionCode);

    @Query("{'tags': ?0, 'active': true}")
    Page<Paper> findByTagAndActive(String tag, Pageable pageable);

    @Query(value = "{'active': true}", sort = "{'downloadCount': -1}")
    Page<Paper> findMostDownloaded(Pageable pageable);

    @Query(value = "{'active': true}", sort = "{'viewCount': -1}")
    Page<Paper> findMostViewed(Pageable pageable);

    @Query(value = "{'active': true}", sort = "{'publishedAt': -1}")
    Page<Paper> findRecentlyPublished(Pageable pageable);
}