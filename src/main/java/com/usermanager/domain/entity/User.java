package com.usermanager.domain.entity;

import com.usermanager.domain.enums.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "users")
public class User extends BaseEntity {

    @NotBlank
    @Size(max = 100)
    @Field("username")
    @Indexed(unique = true)
    private String username;

    @NotBlank
    @Email
    @Size(max = 255)
    @Field("email")
    @Indexed(unique = true)
    private String email;

    @NotBlank
    @Size(max = 100)
    @Field("first_name")
    private String firstName;

    @NotBlank
    @Size(max = 100)
    @Field("last_name")
    private String lastName;

    @Size(max = 20)
    @Field("phone_number")
    private String phoneNumber;

    @Field("keycloak_id")
    @Indexed(unique = true, sparse = true)
    private String keycloakId;

    @Field("status")
    @Indexed
    private UserStatus status = UserStatus.ACTIVE;

    @Field("email_verified")
    private Boolean emailVerified = false;

    @Field("last_login_at")
    private LocalDateTime lastLoginAt;

    @Field("login_attempts")
    private Integer loginAttempts = 0;

    @Field("locked_until")
    private LocalDateTime lockedUntil;

    @Size(max = 1000)
    @Field("profile_picture_url")
    private String profilePictureUrl;

    @Size(max = 500)
    @Field("bio")
    private String bio;

    @Field("department")
    private String department;

    @Field("position")
    private String position;

    @DocumentReference(lazy = true)
    @Field("roles")
    private Set<Role> roles = new HashSet<>();

    @DocumentReference(lazy = true)
    @Field("profiles")
    private Set<UserProfile> profiles = new HashSet<>();

    // Constructors
    public User() {
        super();
    }

    public User(String username, String email, String firstName, String lastName) {
        this();
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Business methods
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean isLocked() {
        return lockedUntil != null && lockedUntil.isAfter(LocalDateTime.now());
    }

    public boolean isEmailVerified() {
        return Boolean.TRUE.equals(emailVerified);
    }

    public void lock(LocalDateTime until) {
        this.lockedUntil = until;
        this.status = UserStatus.LOCKED;
    }

    public void unlock() {
        this.lockedUntil = null;
        this.loginAttempts = 0;
        this.status = UserStatus.ACTIVE;
    }

    public void incrementLoginAttempts() {
        this.loginAttempts++;
    }

    public void resetLoginAttempts() {
        this.loginAttempts = 0;
    }

    public void updateLastLogin() {
        this.lastLoginAt = LocalDateTime.now();
        resetLoginAttempts();
    }

    public void verifyEmail() {
        this.emailVerified = true;
    }

    public void addRole(Role role) {
        this.roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
        role.getUsers().remove(this);
    }

    public void addProfile(UserProfile profile) {
        this.profiles.add(profile);
        profile.setUser(this);
    }

    public void removeProfile(UserProfile profile) {
        this.profiles.remove(profile);
        profile.setUser(null);
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getKeycloakId() {
        return keycloakId;
    }

    public void setKeycloakId(String keycloakId) {
        this.keycloakId = keycloakId;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public Integer getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(Integer loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public LocalDateTime getLockedUntil() {
        return lockedUntil;
    }

    public void setLockedUntil(LocalDateTime lockedUntil) {
        this.lockedUntil = lockedUntil;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<UserProfile> getProfiles() {
        return profiles;
    }

    public void setProfiles(Set<UserProfile> profiles) {
        this.profiles = profiles;
    }
}