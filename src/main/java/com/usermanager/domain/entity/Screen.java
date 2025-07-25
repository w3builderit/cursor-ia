package com.usermanager.domain.entity;

import com.usermanager.domain.enums.ScreenType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "screens")
public class Screen extends BaseEntity {

    @NotBlank
    @Size(max = 100)
    @Field("code")
    @Indexed(unique = true)
    private String code;

    @NotBlank
    @Size(max = 100)
    @Field("name")
    private String name;

    @Size(max = 500)
    @Field("description")
    private String description;

    @Field("type")
    @Indexed
    private ScreenType type;

    @NotBlank
    @Size(max = 100)
    @Field("module")
    @Indexed
    private String module;

    @Size(max = 255)
    @Field("route")
    private String route;

    @Size(max = 255)
    @Field("component")
    private String component;

    @Field("public_access")
    private Boolean publicAccess = false;

    @Field("auth_required")
    private Boolean authRequired = true;

    @Field("required_permissions")
    private Set<String> requiredPermissions = new HashSet<>();

    @Field("cache_enabled")
    private Boolean cacheEnabled = false;

    @Field("cache_duration")
    private Integer cacheDuration; // in minutes

    // Constructors
    public Screen() {
        super();
    }

    public Screen(String code, String name, ScreenType type, String module) {
        this();
        this.code = code;
        this.name = name;
        this.type = type;
        this.module = module;
    }

    public Screen(String code, String name, ScreenType type, String module, String route) {
        this(code, name, type, module);
        this.route = route;
    }

    // Business methods
    public void addRequiredPermission(String permissionCode) {
        this.requiredPermissions.add(permissionCode);
    }

    public void removeRequiredPermission(String permissionCode) {
        this.requiredPermissions.remove(permissionCode);
    }

    public boolean hasRequiredPermission(String permissionCode) {
        return requiredPermissions.contains(permissionCode);
    }

    public boolean isPublicAccess() {
        return Boolean.TRUE.equals(publicAccess);
    }

    public boolean isAuthRequired() {
        return Boolean.TRUE.equals(authRequired);
    }

    public boolean isCacheEnabled() {
        return Boolean.TRUE.equals(cacheEnabled);
    }

    public void enableCache(Integer durationMinutes) {
        this.cacheEnabled = true;
        this.cacheDuration = durationMinutes;
    }

    public void disableCache() {
        this.cacheEnabled = false;
        this.cacheDuration = null;
    }

    // Getters and Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ScreenType getType() {
        return type;
    }

    public void setType(ScreenType type) {
        this.type = type;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public Boolean getPublicAccess() {
        return publicAccess;
    }

    public void setPublicAccess(Boolean publicAccess) {
        this.publicAccess = publicAccess;
    }

    public Boolean getAuthRequired() {
        return authRequired;
    }

    public void setAuthRequired(Boolean authRequired) {
        this.authRequired = authRequired;
    }

    public Set<String> getRequiredPermissions() {
        return requiredPermissions;
    }

    public void setRequiredPermissions(Set<String> requiredPermissions) {
        this.requiredPermissions = requiredPermissions;
    }

    public Boolean getCacheEnabled() {
        return cacheEnabled;
    }

    public void setCacheEnabled(Boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    public Integer getCacheDuration() {
        return cacheDuration;
    }

    public void setCacheDuration(Integer cacheDuration) {
        this.cacheDuration = cacheDuration;
    }
}