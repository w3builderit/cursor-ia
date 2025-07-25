package com.usermanager.domain.entity;

import com.usermanager.domain.enums.PaperStatus;
import com.usermanager.domain.enums.PaperType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "papers", indexes = {
    @Index(name = "idx_paper_code", columnList = "code", unique = true),
    @Index(name = "idx_paper_type", columnList = "type"),
    @Index(name = "idx_paper_status", columnList = "status"),
    @Index(name = "idx_paper_category", columnList = "category"),
    @Index(name = "idx_paper_created_by", columnList = "created_by")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"createdBy", "approvedBy"})
@SuperBuilder
public class Paper extends BaseEntity {

    @NotBlank
    @Size(max = 100)
    @Column(name = "code", nullable = false, unique = true, length = 100)
    private String code;

    @NotBlank
    @Size(max = 200)
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private PaperType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private PaperStatus status = PaperStatus.DRAFT;

    @Size(max = 100)
    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "file_path", length = 500)
    private String filePath;

    @Column(name = "file_name", length = 255)
    private String fileName;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "mime_type", length = 100)
    private String mimeType;

    @Column(name = "version_number", nullable = false)
    @Builder.Default
    private Integer versionNumber = 1;

    @Column(name = "is_latest_version", nullable = false)
    @Builder.Default
    private Boolean isLatestVersion = true;

    @Column(name = "parent_paper_id")
    private String parentPaperId; // For versioning

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "paper_permissions",
        joinColumns = @JoinColumn(name = "paper_id"),
        indexes = @Index(name = "idx_paper_permissions_paper_id", columnList = "paper_id")
    )
    @Column(name = "permission_code", length = 100)
    @Builder.Default
    private Set<String> requiredPermissions = new HashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "paper_tags",
        joinColumns = @JoinColumn(name = "paper_id"),
        indexes = @Index(name = "idx_paper_tags_paper_id", columnList = "paper_id")
    )
    @Column(name = "tag", length = 50)
    @Builder.Default
    private Set<String> tags = new HashSet<>();

    @Column(name = "download_count", nullable = false)
    @Builder.Default
    private Long downloadCount = 0L;

    @Column(name = "view_count", nullable = false)
    @Builder.Default
    private Long viewCount = 0L;

    // Custom constructor for business use
    public Paper(String code, String title, PaperType type, User createdBy) {
        super();
        this.code = code;
        this.title = title;
        this.type = type;
        this.createdBy = createdBy;
        this.status = PaperStatus.DRAFT;
        this.versionNumber = 1;
        this.isLatestVersion = true;
        this.requiredPermissions = new HashSet<>();
        this.tags = new HashSet<>();
        this.downloadCount = 0L;
        this.viewCount = 0L;
    }

    // Business methods
    public void publish() {
        this.status = PaperStatus.PUBLISHED;
        this.publishedAt = LocalDateTime.now();
    }

    public void approve(User approver) {
        this.status = PaperStatus.APPROVED;
        this.approvedBy = approver;
    }

    public void reject() {
        this.status = PaperStatus.REJECTED;
    }

    public void archive() {
        this.status = PaperStatus.ARCHIVED;
    }

    public void incrementDownloadCount() {
        this.downloadCount++;
    }

    public void incrementViewCount() {
        this.viewCount++;
    }

    public boolean isPublished() {
        return status == PaperStatus.PUBLISHED;
    }

    public boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(LocalDateTime.now());
    }

    public boolean isLatestVersion() {
        return Boolean.TRUE.equals(isLatestVersion);
    }

    public void addTag(String tag) {
        this.tags.add(tag.toLowerCase());
    }

    public void removeTag(String tag) {
        this.tags.remove(tag.toLowerCase());
    }

    public void addRequiredPermission(String permissionCode) {
        this.requiredPermissions.add(permissionCode);
    }

    public void removeRequiredPermission(String permissionCode) {
        this.requiredPermissions.remove(permissionCode);
    }
}