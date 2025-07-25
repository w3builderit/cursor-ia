package com.usermanager.domain.entity;

import com.usermanager.domain.enums.PermissionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permissions", indexes = {
    @Index(name = "idx_permission_code", columnList = "code", unique = true),
    @Index(name = "idx_permission_type", columnList = "type"),
    @Index(name = "idx_permission_resource", columnList = "resource")
})
public class Permission extends BaseEntity {

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
    private PermissionType type;

    @NotBlank
    @Size(max = 100)
    @Column(name = "resource", nullable = false, length = 100)
    private String resource;

    @NotBlank
    @Size(max = 50)
    @Column(name = "action", nullable = false, length = 50)
    private String action;

    @Column(name = "system_permission", nullable = false)
    private Boolean systemPermission = false;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
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