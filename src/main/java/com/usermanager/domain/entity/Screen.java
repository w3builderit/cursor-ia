package com.usermanager.domain.entity;

import com.usermanager.domain.enums.ScreenType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "screens", indexes = {
    @Index(name = "idx_screen_code", columnList = "code", unique = true),
    @Index(name = "idx_screen_type", columnList = "type"),
    @Index(name = "idx_screen_module", columnList = "module")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class Screen extends BaseEntity {

    @NotBlank
    @Size(max = 100)
    @Column(name = "code", nullable = false, unique = true, length = 100)
    private String code;

    @NotBlank
    @Size(max = 100)
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private ScreenType type;

    @NotBlank
    @Size(max = 100)
    @Column(name = "module", nullable = false, length = 100)
    private String module;

    @Size(max = 255)
    @Column(name = "route", length = 255)
    private String route;

    @Size(max = 255)
    @Column(name = "component", length = 255)
    private String component;

    @Column(name = "public_access", nullable = false)
    @Builder.Default
    private Boolean publicAccess = false;

    @Column(name = "auth_required", nullable = false)
    @Builder.Default
    private Boolean authRequired = true;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "screen_permissions",
        joinColumns = @JoinColumn(name = "screen_id"),
        indexes = @Index(name = "idx_screen_permissions_screen_id", columnList = "screen_id")
    )
    @Column(name = "permission_code", length = 100)
    @Builder.Default
    private Set<String> requiredPermissions = new HashSet<>();

    @Column(name = "cache_enabled", nullable = false)
    @Builder.Default
    private Boolean cacheEnabled = false;

    @Column(name = "cache_duration")
    private Integer cacheDuration; // in minutes

    // Custom constructors for business use
    public Screen(String code, String name, ScreenType type, String module) {
        super();
        this.code = code;
        this.name = name;
        this.type = type;
        this.module = module;
        this.publicAccess = false;
        this.authRequired = true;
        this.requiredPermissions = new HashSet<>();
        this.cacheEnabled = false;
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
}