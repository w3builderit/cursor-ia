package com.usermanager.domain.entity;

import com.usermanager.domain.enums.ProfileType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Document(collection = "user_profiles")
public class UserProfile extends BaseEntity {

    @DocumentReference
    @Field("user_id")
    @Indexed
    private User user;

    @NotBlank
    @Size(max = 100)
    @Field("name")
    @Indexed
    private String name;

    @Size(max = 500)
    @Field("description")
    private String description;

    @Field("type")
    @Indexed
    private ProfileType type;

    @Field("is_default")
    private Boolean isDefault = false;

    @Field("is_public")
    private Boolean isPublic = false;

    @Field("attributes")
    private Map<String, String> attributes = new HashMap<>();

    @Field("permissions")
    private Set<String> permissions = new HashSet<>();

    @Field("preferences")
    private Map<String, String> preferences = new HashMap<>();

    @Field("context")
    private String context; // e.g., department, project, role context

    // Constructors
    public UserProfile() {
        super();
    }

    public UserProfile(User user, String name, ProfileType type) {
        this();
        this.user = user;
        this.name = name;
        this.type = type;
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

    // Getters and Setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public ProfileType getType() {
        return type;
    }

    public void setType(ProfileType type) {
        this.type = type;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public Map<String, String> getPreferences() {
        return preferences;
    }

    public void setPreferences(Map<String, String> preferences) {
        this.preferences = preferences;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}