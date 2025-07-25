package com.usermanager.repository;

import com.usermanager.domain.entity.User;
import com.usermanager.domain.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByKeycloakId(String keycloakId);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByKeycloakId(String keycloakId);

    List<User> findByStatus(UserStatus status);

    Page<User> findByStatus(UserStatus status, Pageable pageable);

    @Query("{'active': true}")
    List<User> findAllActiveUsers();

    @Query("{'active': true}")
    Page<User> findAllActiveUsers(Pageable pageable);

    @Query("{'status': ?0, 'active': true}")
    Page<User> findByStatusAndActive(UserStatus status, Pageable pageable);

    @Query("{'$and': [" +
           "{'$or': [" +
           "{'firstName': {'$regex': ?0, '$options': 'i'}}, " +
           "{'lastName': {'$regex': ?0, '$options': 'i'}}, " +
           "{'username': {'$regex': ?0, '$options': 'i'}}, " +
           "{'email': {'$regex': ?0, '$options': 'i'}}" +
           "]}, " +
           "{'active': true}" +
           "]}")
    Page<User> searchActiveUsers(String searchTerm, Pageable pageable);

    @Query("{'department': ?0, 'active': true}")
    List<User> findByDepartmentAndActive(String department);

    @Query("{'position': ?0, 'active': true}")
    List<User> findByPositionAndActive(String position);

    @Query("{'lastLoginAt': {'$lt': ?0}}")
    List<User> findUsersNotLoggedInSince(LocalDateTime date);

    @Query("{'emailVerified': false, 'createdAt': {'$lt': ?0}}")
    List<User> findUnverifiedUsersCreatedBefore(LocalDateTime date);

    @Query("{'$and': [{'lockedUntil': {'$ne': null}}, {'lockedUntil': {'$gt': ?#{new java.util.Date()}}}]}")
    List<User> findLockedUsers();

    @Query("{'loginAttempts': {'$gte': ?0}}")
    List<User> findUsersWithExcessiveLoginAttempts(Integer maxAttempts);

    @Query("{'roles.code': ?0, 'active': true}")
    List<User> findByRoleCodeAndActive(String roleCode);

    @Query("{'roles.$id': ?0, 'active': true}")
    Page<User> findByRoleIdAndActive(String roleId, Pageable pageable);

    @Query(value = "{'status': ?0, 'active': true}", count = true)
    long countByStatusAndActive(UserStatus status);

    @Query(value = "{'createdAt': {'$gte': ?0, '$lte': ?1}}", count = true)
    long countUsersCreatedBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("{'active': true}")
    Page<User> findRecentlyActiveUsers(Pageable pageable);
}