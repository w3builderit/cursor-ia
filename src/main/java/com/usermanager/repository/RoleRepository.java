package com.usermanager.repository;

import com.usermanager.domain.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {

    Optional<Role> findByCode(String code);

    Optional<Role> findByName(String name);

    boolean existsByCode(String code);

    boolean existsByName(String name);

    List<Role> findBySystemRole(Boolean systemRole);

    Page<Role> findBySystemRole(Boolean systemRole, Pageable pageable);

    @Query("{'active': true}")
    List<Role> findAllActiveRoles();

    @Query("{'active': true}")
    Page<Role> findAllActiveRoles(Pageable pageable);

    @Query("{'systemRole': ?0, 'active': true}")
    Page<Role> findBySystemRoleAndActive(Boolean systemRole, Pageable pageable);

    @Query("{'$and': [" +
           "{'$or': [" +
           "{'name': {'$regex': ?0, '$options': 'i'}}, " +
           "{'code': {'$regex': ?0, '$options': 'i'}}, " +
           "{'description': {'$regex': ?0, '$options': 'i'}}" +
           "]}, " +
           "{'active': true}" +
           "]}")
    Page<Role> searchActiveRoles(String searchTerm, Pageable pageable);

    @Query("{'permissions.code': ?0, 'active': true}")
    List<Role> findByPermissionCodeAndActive(String permissionCode);

    @Query("{'permissions.$id': ?0, 'active': true}")
    List<Role> findByPermissionIdAndActive(String permissionId);

    @Query("{'users.$id': ?0, 'active': true}")
    List<Role> findByUserIdAndActive(String userId);

    @Query(value = "{'systemRole': ?0, 'active': true}", count = true)
    long countBySystemRoleAndActive(Boolean systemRole);

    @Query("{'permissions': {'$size': 0}, 'active': true}")
    List<Role> findRolesWithoutPermissions();

    @Query("{'users': {'$size': 0}, 'active': true}")
    List<Role> findRolesWithoutUsers();
}