package com.usermanager.repository;

import com.usermanager.domain.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends MongoRepository<Menu, String> {

    Optional<Menu> findByCode(String code);

    boolean existsByCode(String code);

    @Query("{'parent': null}")
    List<Menu> findByParentIsNull();

    @Query(value = "{'parent': null}", sort = "{'displayOrder': 1}")
    List<Menu> findByParentIsNullOrderByDisplayOrder();

    @Query("{'parent.$id': ?0}")
    List<Menu> findByParentId(String parentId);

    @Query(value = "{'parent.$id': ?0}", sort = "{'displayOrder': 1}")
    List<Menu> findByParentIdOrderByDisplayOrder(String parentId);

    List<Menu> findByVisible(Boolean visible);

    Page<Menu> findByVisible(Boolean visible, Pageable pageable);

    List<Menu> findByLevel(Integer level);

    @Query("{'active': true}")
    List<Menu> findAllActiveMenus();

    @Query("{'active': true}")
    Page<Menu> findAllActiveMenus(Pageable pageable);

    @Query(value = "{'parent': null, 'active': true}", sort = "{'displayOrder': 1}")
    List<Menu> findRootMenusActive();

    @Query(value = "{'parent.$id': ?0, 'active': true}", sort = "{'displayOrder': 1}")
    List<Menu> findByParentIdAndActive(String parentId);

    @Query("{'visible': ?0, 'active': true}")
    Page<Menu> findByVisibleAndActive(Boolean visible, Pageable pageable);

    @Query("{'$and': [" +
           "{'$or': [" +
           "{'name': {'$regex': ?0, '$options': 'i'}}, " +
           "{'code': {'$regex': ?0, '$options': 'i'}}, " +
           "{'description': {'$regex': ?0, '$options': 'i'}}" +
           "]}, " +
           "{'active': true}" +
           "]}")
    Page<Menu> searchActiveMenus(String searchTerm, Pageable pageable);

    @Query("{'requiredPermission': ?0, 'active': true}")
    List<Menu> findByRequiredPermissionAndActive(String permissionCode);

    @Query(value = "{'level': ?0, 'active': true}", sort = "{'displayOrder': 1}")
    List<Menu> findByLevelAndActive(Integer level);

    @Query(value = "{'parent.$id': ?0, 'active': true}", count = true)
    long countByParentIdAndActive(String parentId);

    @Query(value = "{'parent': null, 'visible': true, 'active': true}", sort = "{'displayOrder': 1}")
    List<Menu> findVisibleRootMenus();

    @Query(value = "{'parent.$id': ?0, 'visible': true, 'active': true}", sort = "{'displayOrder': 1}")
    List<Menu> findVisibleChildMenus(String parentId);

    @Query("{'url': {'$ne': null}, 'active': true}")
    List<Menu> findMenusWithUrl();

    @Query("{'parent.$id': ?0, 'active': true}")
    List<Menu> findByParentIdForMaxDisplayOrder(String parentId);

    @Query("{'parent': null, 'active': true}")
    List<Menu> findRootMenusForMaxDisplayOrder();
}