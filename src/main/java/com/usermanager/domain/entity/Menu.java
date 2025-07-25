package com.usermanager.domain.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "menus")
public class Menu extends BaseEntity {

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

    @Size(max = 255)
    @Field("url")
    private String url;

    @Size(max = 100)
    @Field("icon")
    private String icon;

    @Field("display_order")
    @Indexed
    private Integer displayOrder = 0;

    @Field("visible")
    private Boolean visible = true;

    @DocumentReference
    @Field("parent_id")
    @Indexed
    private Menu parent;

    @DocumentReference(lazy = true)
    @Field("children")
    private List<Menu> children = new ArrayList<>();

    @Field("required_permission")
    private String requiredPermission;

    @Field("menu_level")
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