package com.usermanager.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menus", indexes = {
    @Index(name = "idx_menu_code", columnList = "code", unique = true),
    @Index(name = "idx_menu_parent_id", columnList = "parent_id"),
    @Index(name = "idx_menu_order", columnList = "display_order")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"parent", "children"})
@SuperBuilder
public class Menu extends BaseEntity {

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

    @Size(max = 255)
    @Column(name = "url", length = 255)
    private String url;

    @Size(max = 100)
    @Column(name = "icon", length = 100)
    private String icon;

    @Column(name = "display_order", nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;

    @Column(name = "visible", nullable = false)
    @Builder.Default
    private Boolean visible = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Menu parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderBy("displayOrder ASC")
    @Builder.Default
    private List<Menu> children = new ArrayList<>();

    @Column(name = "required_permission", length = 100)
    private String requiredPermission;

    @Column(name = "menu_level", nullable = false)
    @Builder.Default
    private Integer level = 0;

    // Custom constructors for business use
    public Menu(String code, String name, String url) {
        super();
        this.code = code;
        this.name = name;
        this.url = url;
        this.displayOrder = 0;
        this.visible = true;
        this.children = new ArrayList<>();
        this.level = 0;
    }

    public Menu(String code, String name, String url, Menu parent) {
        this(code, name, url);
        this.parent = parent;
        if (parent != null) {
            this.level = parent.getLevel() + 1;
            parent.addChild(this);
        }
    }

    // Business methods
    public void addChild(Menu child) {
        this.children.add(child);
        child.setParent(this);
        child.setLevel(this.level + 1);
    }

    public void removeChild(Menu child) {
        this.children.remove(child);
        child.setParent(null);
        child.setLevel(0);
    }

    public boolean isRootMenu() {
        return parent == null;
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public boolean isVisible() {
        return Boolean.TRUE.equals(visible);
    }

    public void show() {
        this.visible = true;
    }

    public void hide() {
        this.visible = false;
    }
}