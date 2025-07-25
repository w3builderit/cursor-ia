package com.usermanager.domain.entity;

import com.usermanager.domain.enums.PermissionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "permissions")
public class Permission extends BaseEntity {

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
    private PermissionType type;

    @NotBlank
    @Size(max = 100)
    @Field("resource")
    @Indexed
    private String resource;

    @NotBlank
    @Size(max = 50)
    @Field("action")
    private String action;

    @Field("system_permission")
    private Boolean systemPermission = false;

    @DocumentReference(lazy = true)
    @Field("roles")
    private Set<Role> roles = new HashSet<>();

    // Constructors
    public Permission() {
        super();
    }

    public Permission(String code, String name, PermissionType type, String resource, String action) {
        this();
        this.code = code;
        this.name = name;
        this.type = type;
        this.resource = resource;
        this.action = action;
    }

    public Permission(String code, String name, String description, PermissionType type, String resource, String action) {
        this(code, name, type, resource, action);
        this.description = description;
    }

    // Business methods
    public boolean isSystemPermission() {
        return Boolean.TRUE.equals(systemPermission);
    }

    public String getFullCode() {
        return resource + ":" + action;
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

    public PermissionType getType() {
        return type;
    }

    public void setType(PermissionType type) {
        this.type = type;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Boolean getSystemPermission() {
        return systemPermission;
    }

    public void setSystemPermission(Boolean systemPermission) {
        this.systemPermission = systemPermission;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}