package com.usermanager.repository;

import com.usermanager.domain.entity.Screen;
import com.usermanager.domain.enums.ScreenType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScreenRepository extends MongoRepository<Screen, String> {

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

    @Query("{'active': true}")
    List<Screen> findAllActiveScreens();

    @Query("{'active': true}")
    Page<Screen> findAllActiveScreens(Pageable pageable);

    @Query("{'type': ?0, 'active': true}")
    Page<Screen> findByTypeAndActive(ScreenType type, Pageable pageable);

    @Query("{'module': ?0, 'active': true}")
    Page<Screen> findByModuleAndActive(String module, Pageable pageable);

    @Query("{'$and': [" +
           "{'$or': [" +
           "{'name': {'$regex': ?0, '$options': 'i'}}, " +
           "{'code': {'$regex': ?0, '$options': 'i'}}, " +
           "{'description': {'$regex': ?0, '$options': 'i'}}, " +
           "{'module': {'$regex': ?0, '$options': 'i'}}" +
           "]}, " +
           "{'active': true}" +
           "]}")
    Page<Screen> searchActiveScreens(String searchTerm, Pageable pageable);

    @Query("{'publicAccess': ?0, 'active': true}")
    Page<Screen> findByPublicAccessAndActive(Boolean publicAccess, Pageable pageable);

    @Query("{'authRequired': ?0, 'active': true}")
    Page<Screen> findByAuthRequiredAndActive(Boolean authRequired, Pageable pageable);

    @Query(value = "{'type': ?0, 'active': true}", count = true)
    long countByTypeAndActive(ScreenType type);

    @Query(value = "{'module': ?0, 'active': true}", count = true)
    long countByModuleAndActive(String module);

    @Query(value = "{'active': true}", fields = "{'module': 1, '_id': 0}")
    List<String> findDistinctModules();

    @Query("{'cacheEnabled': true, 'active': true}")
    List<Screen> findCacheEnabledScreens();

    @Query("{'requiredPermissions': {'$size': 0}, 'active': true}")
    List<Screen> findScreensWithoutPermissions();

    @Query("{'requiredPermissions': ?0, 'active': true}")
    List<Screen> findByRequiredPermissionAndActive(String permissionCode);
}