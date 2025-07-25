package com.usermanager.repository;

import com.usermanager.domain.entity.Permission;
import com.usermanager.domain.enums.PermissionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends MongoRepository<Permission, String> {

    Optional<Permission> findByCode(String code);

    boolean existsByCode(String code);

    List<Permission> findByType(PermissionType type);

    Page<Permission> findByType(PermissionType type, Pageable pageable);

    List<Permission> findByResource(String resource);

    Page<Permission> findByResource(String resource, Pageable pageable);

    List<Permission> findByResourceAndAction(String resource, String action);

    List<Permission> findBySystemPermission(Boolean systemPermission);

    Page<Permission> findBySystemPermission(Boolean systemPermission, Pageable pageable);

    @Query("{'active': true}")
    List<Permission> findAllActivePermissions();

    @Query("{'active': true}")
    Page<Permission> findAllActivePermissions(Pageable pageable);

    @Query("{'type': ?0, 'active': true}")
    Page<Permission> findByTypeAndActive(PermissionType type, Pageable pageable);

    @Query("{'resource': ?0, 'active': true}")
    Page<Permission> findByResourceAndActive(String resource, Pageable pageable);

    @Query("{'$and': [" +
           "{'$or': [" +
           "{'name': {'$regex': ?0, '$options': 'i'}}, " +
           "{'code': {'$regex': ?0, '$options': 'i'}}, " +
           "{'description': {'$regex': ?0, '$options': 'i'}}, " +
           "{'resource': {'$regex': ?0, '$options': 'i'}}, " +
           "{'action': {'$regex': ?0, '$options': 'i'}}" +
           "]}, " +
           "{'active': true}" +
           "]}")
    Page<Permission> searchActivePermissions(String searchTerm, Pageable pageable);

    @Query("{'roles.$id': ?0, 'active': true}")
    List<Permission> findByRoleIdAndActive(String roleId);

    @Query("{'roles.code': ?0, 'active': true}")
    List<Permission> findByRoleCodeAndActive(String roleCode);

    @Query(value = "{'type': ?0, 'active': true}", count = true)
    long countByTypeAndActive(PermissionType type);

    @Query("{'roles': {'$size': 0}, 'active': true}")
    List<Permission> findPermissionsWithoutRoles();

    @Query(value = "{'active': true}", fields = "{'resource': 1, '_id': 0}")
    List<String> findDistinctResources();

    @Query(value = "{'resource': ?0, 'active': true}", fields = "{'action': 1, '_id': 0}")
    List<String> findDistinctActionsByResource(String resource);
}