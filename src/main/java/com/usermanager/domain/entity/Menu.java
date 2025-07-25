package com.usermanager.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menus", indexes = {
    @Index(name = "idx_menu_code", columnList = "code", unique = true),
    @Index(name = "idx_menu_parent_id", columnList = "parent_id"),
    @Index(name = "idx_menu_order", columnList = "display_order")
})
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
    private Integer displayOrder = 0;

    @Column(name = "visible", nullable = false)
    private Boolean visible = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Menu parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderBy("displayOrder ASC")
    private List<Menu> children = new ArrayList<>();

    @Column(name = "required_permission", length = 100)
    private String requiredPermission;

    @Column(name = "menu_level", nullable = false)
    private Integer level = 0;

    // Constructors
    public Menu() {
        super();
    }

    public Menu(String code, String name, String url) {
        this();
        this.code = code;
        this.name = name;
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Menu getParent() {
        return parent;
    }

    public void setParent(Menu parent) {
        this.parent = parent;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    public String getRequiredPermission() {
        return requiredPermission;
    }

    public void setRequiredPermission(String requiredPermission) {
        this.requiredPermission = requiredPermission;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}