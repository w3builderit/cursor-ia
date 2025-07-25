package com.usermanager.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles", indexes = {
    @Index(name = "idx_role_name", columnList = "name", unique = true),
    @Index(name = "idx_role_code", columnList = "code", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"users", "permissions"})
@SuperBuilder
public class Role extends BaseEntity {

    @NotBlank
    @Size(max = 100)
    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @NotBlank
    @Size(max = 50)
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "system_role", nullable = false)
    @Builder.Default
    private Boolean systemRole = false;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<User> users = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "role_permissions",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id"),
        indexes = {
            @Index(name = "idx_role_permissions_role_id", columnList = "role_id"),
            @Index(name = "idx_role_permissions_permission_id", columnList = "permission_id")
        }
    )
    @Builder.Default
    private Set<Permission> permissions = new HashSet<>();

    // Custom constructors for business use
    public Role(String name, String code, String description) {
        super();
        this.name = name;
        this.code = code;
        this.description = description;
        this.systemRole = false;
        this.users = new HashSet<>();
        this.permissions = new HashSet<>();
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
}