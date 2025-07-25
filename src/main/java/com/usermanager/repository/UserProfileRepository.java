package com.usermanager.repository;

import com.usermanager.domain.entity.User;
import com.usermanager.domain.entity.UserProfile;
import com.usermanager.domain.enums.ProfileType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends MongoRepository<UserProfile, String> {

    List<UserProfile> findByUser(User user);

    Page<UserProfile> findByUser(User user, Pageable pageable);

    @Query("{'user.$id': ?0}")
    List<UserProfile> findByUserId(String userId);

    @Query("{'user.$id': ?0}")
    Page<UserProfile> findByUserId(String userId, Pageable pageable);

    List<UserProfile> findByType(ProfileType type);

    Page<UserProfile> findByType(ProfileType type, Pageable pageable);

    List<UserProfile> findByContext(String context);

    Page<UserProfile> findByContext(String context, Pageable pageable);

    List<UserProfile> findByIsDefault(Boolean isDefault);

    Page<UserProfile> findByIsDefault(Boolean isDefault, Pageable pageable);

    List<UserProfile> findByIsPublic(Boolean isPublic);

    Page<UserProfile> findByIsPublic(Boolean isPublic, Pageable pageable);

    Optional<UserProfile> findByUserAndIsDefaultTrue(User user);

    @Query("{'user.$id': ?0, 'isDefault': true}")
    Optional<UserProfile> findByUserIdAndIsDefaultTrue(String userId);

    @Query("{'active': true}")
    List<UserProfile> findAllActiveProfiles();

    @Query("{'active': true}")
    Page<UserProfile> findAllActiveProfiles(Pageable pageable);

    @Query("{'user.$id': ?0, 'active': true}")
    Page<UserProfile> findByUserIdAndActive(String userId, Pageable pageable);

    @Query("{'type': ?0, 'active': true}")
    Page<UserProfile> findByTypeAndActive(ProfileType type, Pageable pageable);

    @Query("{'context': ?0, 'active': true}")
    Page<UserProfile> findByContextAndActive(String context, Pageable pageable);

    @Query("{'$and': [" +
           "{'$or': [" +
           "{'name': {'$regex': ?0, '$options': 'i'}}, " +
           "{'description': {'$regex': ?0, '$options': 'i'}}, " +
           "{'context': {'$regex': ?0, '$options': 'i'}}" +
           "]}, " +
           "{'active': true}" +
           "]}")
    Page<UserProfile> searchActiveProfiles(String searchTerm, Pageable pageable);

    @Query("{'isPublic': ?0, 'active': true}")
    Page<UserProfile> findByIsPublicAndActive(Boolean isPublic, Pageable pageable);

    @Query("{'isDefault': ?0, 'active': true}")
    Page<UserProfile> findByIsDefaultAndActive(Boolean isDefault, Pageable pageable);

    @Query(value = "{'user.$id': ?0, 'active': true}", count = true)
    long countByUserIdAndActive(String userId);

    @Query(value = "{'type': ?0, 'active': true}", count = true)
    long countByTypeAndActive(ProfileType type);

    @Query(value = "{'context': {'$ne': null}, 'active': true}", fields = "{'context': 1, '_id': 0}")
    List<String> findDistinctContexts();

    @Query("{'user.$id': ?0, 'type': ?1, 'active': true}")
    List<UserProfile> findByUserIdAndTypeAndActive(String userId, ProfileType type);

    @Query("{'user.$id': ?0, 'context': ?1, 'active': true}")
    List<UserProfile> findByUserIdAndContextAndActive(String userId, String context);

    @Query("{'permissions': {'$size': 0}, 'active': true}")
    List<UserProfile> findProfilesWithoutPermissions();

    @Query("{'permissions': ?0, 'active': true}")
    List<UserProfile> findByPermissionAndActive(String permissionCode);
}