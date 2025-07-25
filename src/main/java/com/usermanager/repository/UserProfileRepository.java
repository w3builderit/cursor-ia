package com.usermanager.repository;

import com.usermanager.domain.entity.User;
import com.usermanager.domain.entity.UserProfile;
import com.usermanager.domain.enums.ProfileType;
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
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    List<UserProfile> findByUser(User user);

    Page<UserProfile> findByUser(User user, Pageable pageable);

    List<UserProfile> findByUserId(UUID userId);

    Page<UserProfile> findByUserId(UUID userId, Pageable pageable);

    List<UserProfile> findByType(ProfileType type);

    Page<UserProfile> findByType(ProfileType type, Pageable pageable);

    List<UserProfile> findByContext(String context);

    Page<UserProfile> findByContext(String context, Pageable pageable);

    List<UserProfile> findByIsDefault(Boolean isDefault);

    Page<UserProfile> findByIsDefault(Boolean isDefault, Pageable pageable);

    List<UserProfile> findByIsPublic(Boolean isPublic);

    Page<UserProfile> findByIsPublic(Boolean isPublic, Pageable pageable);

    Optional<UserProfile> findByUserAndIsDefaultTrue(User user);

    Optional<UserProfile> findByUserIdAndIsDefaultTrue(UUID userId);

    @Query("SELECT up FROM UserProfile up WHERE up.active = true")
    List<UserProfile> findAllActiveProfiles();

    @Query("SELECT up FROM UserProfile up WHERE up.active = true")
    Page<UserProfile> findAllActiveProfiles(Pageable pageable);

    @Query("SELECT up FROM UserProfile up WHERE up.user.id = :userId AND up.active = true")
    Page<UserProfile> findByUserIdAndActive(@Param("userId") UUID userId, Pageable pageable);

    @Query("SELECT up FROM UserProfile up WHERE up.type = :type AND up.active = true")
    Page<UserProfile> findByTypeAndActive(@Param("type") ProfileType type, Pageable pageable);

    @Query("SELECT up FROM UserProfile up WHERE up.context = :context AND up.active = true")
    Page<UserProfile> findByContextAndActive(@Param("context") String context, Pageable pageable);

    @Query("SELECT up FROM UserProfile up WHERE " +
           "(LOWER(up.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(up.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(up.context) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "up.active = true")
    Page<UserProfile> searchActiveProfiles(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT up FROM UserProfile up WHERE up.isPublic = :isPublic AND up.active = true")
    Page<UserProfile> findByIsPublicAndActive(@Param("isPublic") Boolean isPublic, Pageable pageable);

    @Query("SELECT up FROM UserProfile up WHERE up.isDefault = :isDefault AND up.active = true")
    Page<UserProfile> findByIsDefaultAndActive(@Param("isDefault") Boolean isDefault, Pageable pageable);

    @Query("SELECT COUNT(up) FROM UserProfile up WHERE up.user.id = :userId AND up.active = true")
    long countByUserIdAndActive(@Param("userId") UUID userId);

    @Query("SELECT COUNT(up) FROM UserProfile up WHERE up.type = :type AND up.active = true")
    long countByTypeAndActive(@Param("type") ProfileType type);

    @Query("SELECT up.type, COUNT(up) FROM UserProfile up WHERE up.active = true GROUP BY up.type")
    List<Object[]> countProfilesByType();

    @Query("SELECT up.context, COUNT(up) FROM UserProfile up WHERE up.context IS NOT NULL AND up.active = true GROUP BY up.context")
    List<Object[]> countProfilesByContext();

    @Query("SELECT DISTINCT up.context FROM UserProfile up WHERE up.context IS NOT NULL AND up.active = true ORDER BY up.context")
    List<String> findDistinctContexts();

    @Query("SELECT up FROM UserProfile up WHERE up.user.id = :userId AND up.type = :type AND up.active = true")
    List<UserProfile> findByUserIdAndTypeAndActive(@Param("userId") UUID userId, @Param("type") ProfileType type);

    @Query("SELECT up FROM UserProfile up WHERE up.user.id = :userId AND up.context = :context AND up.active = true")
    List<UserProfile> findByUserIdAndContextAndActive(@Param("userId") UUID userId, @Param("context") String context);

    @Query("SELECT up FROM UserProfile up WHERE SIZE(up.permissions) = 0 AND up.active = true")
    List<UserProfile> findProfilesWithoutPermissions();

    @Query("SELECT up FROM UserProfile up WHERE :permissionCode MEMBER OF up.permissions AND up.active = true")
    List<UserProfile> findByPermissionAndActive(@Param("permissionCode") String permissionCode);
}