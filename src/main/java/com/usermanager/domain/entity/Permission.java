package com.usermanager.domain.entity;

import com.usermanager.domain.enums.PermissionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permissions", indexes = {
    @Index(name = "idx_permission_code", columnList = "code", unique = true),
    @Index(name = "idx_permission_type", columnList = "type"),
    @Index(name = "idx_permission_resource", columnList = "resource")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"roles"})
@SuperBuilder
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
    @Builder.Default
    private Boolean systemPermission = false;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    // Custom constructors for business use
    public Permission(String code, String name, PermissionType type, String resource, String action) {
        super();
        this.code = code;
        this.name = name;
        this.type = type;
        this.resource = resource;
        this.action = action;
        this.systemPermission = false;
        this.roles = new HashSet<>();
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
}