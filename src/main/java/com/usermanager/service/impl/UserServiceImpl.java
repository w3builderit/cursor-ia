package com.usermanager.service.impl;

import com.usermanager.domain.entity.Role;
import com.usermanager.domain.entity.User;
import com.usermanager.domain.enums.UserStatus;
import com.usermanager.dto.UserDto;
import com.usermanager.mapper.UserMapper;
import com.usermanager.repository.RoleRepository;
import com.usermanager.repository.UserRepository;
import com.usermanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, 
                          RoleRepository roleRepository,
                          UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        // Validate uniqueness
        if (existsByUsername(userDto.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + userDto.getUsername());
        }
        if (existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + userDto.getEmail());
        }
        if (userDto.getKeycloakId() != null && existsByKeycloakId(userDto.getKeycloakId())) {
            throw new IllegalArgumentException("Keycloak ID already exists: " + userDto.getKeycloakId());
        }

        User user = userMapper.toEntity(userDto);
        user.setStatus(UserStatus.ACTIVE);
        user.activate();
        
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public Optional<UserDto> getUserById(String id) {
        return userRepository.findById(id)
                .filter(User::isActive)
                .map(userMapper::toDto);
    }

    @Override
    public Optional<UserDto> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .filter(User::isActive)
                .map(userMapper::toDto);
    }

    @Override
    public Optional<UserDto> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .filter(User::isActive)
                .map(userMapper::toDto);
    }

    @Override
    public Optional<UserDto> getUserByKeycloakId(String keycloakId) {
        return userRepository.findByKeycloakId(keycloakId)
                .filter(User::isActive)
                .map(userMapper::toDto);
    }

    @Override
    public UserDto updateUser(String id, UserDto userDto) {
        User existingUser = userRepository.findById(id)
                .filter(User::isActive)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Validate uniqueness for updated fields
        if (!existingUser.getUsername().equals(userDto.getUsername()) && 
            existsByUsername(userDto.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + userDto.getUsername());
        }
        if (!existingUser.getEmail().equals(userDto.getEmail()) && 
            existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + userDto.getEmail());
        }
        if (userDto.getKeycloakId() != null && 
            !userDto.getKeycloakId().equals(existingUser.getKeycloakId()) && 
            existsByKeycloakId(userDto.getKeycloakId())) {
            throw new IllegalArgumentException("Keycloak ID already exists: " + userDto.getKeycloakId());
        }

        // Update fields
        existingUser.setUsername(userDto.getUsername());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setFirstName(userDto.getFirstName());
        existingUser.setLastName(userDto.getLastName());
        existingUser.setPhoneNumber(userDto.getPhoneNumber());
        existingUser.setKeycloakId(userDto.getKeycloakId());
        existingUser.setDepartment(userDto.getDepartment());
        existingUser.setPosition(userDto.getPosition());
        existingUser.setBio(userDto.getBio());
        existingUser.setProfilePictureUrl(userDto.getProfilePictureUrl());

        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDto(updatedUser);
    }

    @Override
    public void deleteUser(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    @Override
    public void softDeleteUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.deactivate();
        userRepository.save(user);
    }

    @Override
    public void activateUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.activate();
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
    }

    @Override
    public void deactivateUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.deactivate();
        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
    }

    @Override
    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDto);
    }

    @Override
    public Page<UserDto> getActiveUsers(Pageable pageable) {
        return userRepository.findAllActiveUsers(pageable)
                .map(userMapper::toDto);
    }

    @Override
    public Page<UserDto> getUsersByStatus(UserStatus status, Pageable pageable) {
        return userRepository.findByStatusAndActive(status, pageable)
                .map(userMapper::toDto);
    }

    @Override
    public Page<UserDto> searchUsers(String searchTerm, Pageable pageable) {
        return userRepository.searchActiveUsers(searchTerm, pageable)
                .map(userMapper::toDto);
    }

    @Override
    public List<UserDto> getUsersByDepartment(String department) {
        return userRepository.findByDepartmentAndActive(department)
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public List<UserDto> getUsersByPosition(String position) {
        return userRepository.findByPositionAndActive(position)
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public List<UserDto> getUsersByRole(String roleCode) {
        return userRepository.findByRoleCodeAndActive(roleCode)
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public void lockUser(String id, LocalDateTime until) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.lock(until);
        userRepository.save(user);
    }

    @Override
    public void unlockUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.unlock();
        userRepository.save(user);
    }

    @Override
    public void incrementLoginAttempts(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.incrementLoginAttempts();
        userRepository.save(user);
    }

    @Override
    public void resetLoginAttempts(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.resetLoginAttempts();
        userRepository.save(user);
    }

    @Override
    public void updateLastLogin(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.updateLastLogin();
        userRepository.save(user);
    }

    @Override
    public void verifyEmail(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.verifyEmail();
        userRepository.save(user);
    }

    @Override
    public void assignRole(String userId, String roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
        
        user.addRole(role);
        userRepository.save(user);
    }

    @Override
    public void assignRoles(String userId, List<String> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        List<Role> roles = roleRepository.findAllById(roleIds);
        if (roles.size() != roleIds.size()) {
            throw new RuntimeException("One or more roles not found");
        }
        
        roles.forEach(user::addRole);
        userRepository.save(user);
    }

    @Override
    public void removeRole(String userId, String roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
        
        user.removeRole(role);
        userRepository.save(user);
    }

    @Override
    public void removeAllRoles(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        user.getRoles().clear();
        userRepository.save(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByKeycloakId(String keycloakId) {
        return userRepository.existsByKeycloakId(keycloakId);
    }

    @Override
    public long countUsersByStatus(UserStatus status) {
        return userRepository.countByStatusAndActive(status);
    }

    @Override
    public long countUsersCreatedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return userRepository.countUsersCreatedBetween(startDate, endDate);
    }

    @Override
    public List<Object[]> getUserCountByDepartment() {
        // This would need to be implemented differently in MongoDB
        // For now, returning empty list
        return List.of();
    }

    @Override
    public List<UserDto> findUsersNotLoggedInSince(LocalDateTime date) {
        return userRepository.findUsersNotLoggedInSince(date)
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public List<UserDto> findUnverifiedUsersCreatedBefore(LocalDateTime date) {
        return userRepository.findUnverifiedUsersCreatedBefore(date)
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public List<UserDto> findLockedUsers() {
        return userRepository.findLockedUsers()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }
}