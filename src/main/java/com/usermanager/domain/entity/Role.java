package com.usermanager.domain.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "roles")
public class Role extends BaseEntity {

    @NotBlank
    @Size(max = 100)
    @Field("name")
    @Indexed(unique = true)
    private String name;

    @NotBlank
    @Size(max = 50)
    @Field("code")
    @Indexed(unique = true)
    private String code;

    @Size(max = 500)
    @Field("description")
    private String description;

    @Field("system_role")
    private Boolean systemRole = false;

    @DocumentReference(lazy = true)
    @Field("users")
    private Set<User> users = new HashSet<>();

    @DocumentReference(lazy = true)
    @Field("permissions")
    private Set<Permission> permissions = new HashSet<>();

    // Constructors
    public Role() {
        super();
    }

    public Role(String name, String code, String description) {
        this();
        this.name = name;
        this.code = code;
        this.description = description;
    }

    public Role(String name, String code, String description, Boolean systemRole) {
        this(name, code, description);
        this.systemRole = systemRole;
    }

    // Business methods
    public void addPermission(Permission permission) {
        this.permissions.add(permission);
        permission.getRoles().add(this);
    }

    public void removePermission(Permission permission) {
        this.permissions.remove(permission);
        permission.getRoles().remove(this);
    }

    public boolean hasPermission(String permissionCode) {
        return permissions.stream()
                .anyMatch(permission -> permission.getCode().equals(permissionCode));
    }

    public boolean isSystemRole() {
        return Boolean.TRUE.equals(systemRole);
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getSystemRole() {
        return systemRole;
    }

    public void setSystemRole(Boolean systemRole) {
        this.systemRole = systemRole;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }
}