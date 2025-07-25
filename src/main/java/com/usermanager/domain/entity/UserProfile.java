package com.usermanager.domain.entity;

import com.usermanager.domain.enums.ProfileType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "user_profiles", indexes = {
    @Index(name = "idx_user_profile_user_id", columnList = "user_id"),
    @Index(name = "idx_user_profile_type", columnList = "type"),
    @Index(name = "idx_user_profile_name", columnList = "name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"user"})
@SuperBuilder
public class UserProfile extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank
    @Size(max = 100)
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private ProfileType type;

    @Column(name = "is_default", nullable = false)
    @Builder.Default
    private Boolean isDefault = false;

    @Column(name = "is_public", nullable = false)
    @Builder.Default
    private Boolean isPublic = false;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "user_profile_attributes",
        joinColumns = @JoinColumn(name = "profile_id"),
        indexes = @Index(name = "idx_user_profile_attributes_profile_id", columnList = "profile_id")
    )
    @MapKeyColumn(name = "attribute_key", length = 100)
    @Column(name = "attribute_value", length = 1000)
    @Builder.Default
    private Map<String, String> attributes = new HashMap<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "user_profile_permissions",
        joinColumns = @JoinColumn(name = "profile_id"),
        indexes = @Index(name = "idx_user_profile_permissions_profile_id", columnList = "profile_id")
    )
    @Column(name = "permission_code", length = 100)
    @Builder.Default
    private Set<String> permissions = new HashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "user_profile_preferences",
        joinColumns = @JoinColumn(name = "profile_id"),
        indexes = @Index(name = "idx_user_profile_preferences_profile_id", columnList = "profile_id")
    )
    @MapKeyColumn(name = "preference_key", length = 100)
    @Column(name = "preference_value", length = 500)
    @Builder.Default
    private Map<String, String> preferences = new HashMap<>();

    @Column(name = "context", length = 100)
    private String context; // e.g., department, project, role context

    // Custom constructors for business use
    public UserProfile(User user, String name, ProfileType type) {
        super();
        this.user = user;
        this.name = name;
        this.type = type;
        this.isDefault = false;
        this.isPublic = false;
        this.attributes = new HashMap<>();
        this.permissions = new HashSet<>();
        this.preferences = new HashMap<>();
    }

    public UserProfile(User user, String name, ProfileType type, String context) {
        this(user, name, type);
        this.context = context;
    }

    // Business methods
    public void addAttribute(String key, String value) {
        this.attributes.put(key, value);
    }

    public void removeAttribute(String key) {
        this.attributes.remove(key);
    }

    public String getAttribute(String key) {
        return this.attributes.get(key);
    }

    public void addPermission(String permissionCode) {
        this.permissions.add(permissionCode);
    }

    public void removePermission(String permissionCode) {
        this.permissions.remove(permissionCode);
    }

    public boolean hasPermission(String permissionCode) {
        return this.permissions.contains(permissionCode);
    }

    public void addPreference(String key, String value) {
        this.preferences.put(key, value);
    }

    public void removePreference(String key) {
        this.preferences.remove(key);
    }

    public String getPreference(String key) {
        return this.preferences.get(key);
    }

    public String getPreference(String key, String defaultValue) {
        return this.preferences.getOrDefault(key, defaultValue);
    }

    public void setAsDefault() {
        this.isDefault = true;
    }

    public void unsetAsDefault() {
        this.isDefault = false;
    }

    public boolean isDefault() {
        return Boolean.TRUE.equals(isDefault);
    }

    public boolean isPublic() {
        return Boolean.TRUE.equals(isPublic);
    }

    public void makePublic() {
        this.isPublic = true;
    }

    public void makePrivate() {
        this.isPublic = false;
    }
}