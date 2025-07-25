package com.usermanager.domain.entity;

import com.usermanager.domain.enums.PaperStatus;
import com.usermanager.domain.enums.PaperType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "papers")
public class Paper extends BaseEntity {

    @NotBlank
    @Size(max = 100)
    @Field("code")
    @Indexed(unique = true)
    private String code;

    @NotBlank
    @Size(max = 200)
    @Field("title")
    private String title;

    @Size(max = 1000)
    @Field("description")
    private String description;

    @Field("type")
    @Indexed
    private PaperType type;

    @Field("status")
    @Indexed
    private PaperStatus status = PaperStatus.DRAFT;

    @Size(max = 100)
    @Field("category")
    @Indexed
    private String category;

    @Field("file_path")
    private String filePath;

    @Field("file_name")
    private String fileName;

    @Field("file_size")
    private Long fileSize;

    @Field("mime_type")
    private String mimeType;

    @Field("version_number")
    private Integer versionNumber = 1;

    @Field("is_latest_version")
    private Boolean isLatestVersion = true;

    @Field("parent_paper_id")
    private String parentPaperId; // For versioning

    @Field("published_at")
    private LocalDateTime publishedAt;

    @Field("expires_at")
    private LocalDateTime expiresAt;

    @DocumentReference
    @Field("created_by")
    @Indexed
    private User createdBy;

    @DocumentReference
    @Field("approved_by")
    private User approvedBy;

    @Field("required_permissions")
    private Set<String> requiredPermissions = new HashSet<>();

    @Field("tags")
    private Set<String> tags = new HashSet<>();

    @Field("download_count")
    private Long downloadCount = 0L;

    @Field("view_count")
    private Long viewCount = 0L;

    // Constructors
    public Paper() {
        super();
    }

    public Paper(String code, String title, PaperType type, User createdBy) {
        this();
        this.code = code;
        this.title = title;
        this.type = type;
        this.createdBy = createdBy;
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

    // Getters and Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PaperType getType() {
        return type;
    }

    public void setType(PaperType type) {
        this.type = type;
    }

    public PaperStatus getStatus() {
        return status;
    }

    public void setStatus(PaperStatus status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    public Boolean getIsLatestVersion() {
        return isLatestVersion;
    }

    public void setIsLatestVersion(Boolean isLatestVersion) {
        this.isLatestVersion = isLatestVersion;
    }

    public String getParentPaperId() {
        return parentPaperId;
    }

    public void setParentPaperId(String parentPaperId) {
        this.parentPaperId = parentPaperId;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Set<String> getRequiredPermissions() {
        return requiredPermissions;
    }

    public void setRequiredPermissions(Set<String> requiredPermissions) {
        this.requiredPermissions = requiredPermissions;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Long getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Long downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }
}