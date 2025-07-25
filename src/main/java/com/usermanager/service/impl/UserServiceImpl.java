package com.usermanager.service.impl;

import com.usermanager.domain.entity.Role;
import com.usermanager.domain.entity.User;
import com.usermanager.domain.enums.UserStatus;
import com.usermanager.dto.UserDto;
import com.usermanager.mapper.UserMapper;
import com.usermanager.repository.RoleRepository;
import com.usermanager.repository.UserRepository;
import com.usermanager.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
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
    @Transactional(readOnly = true)
    public Optional<UserDto> getUserById(UUID id) {
        return userRepository.findById(id)
                .filter(User::isActive)
                .map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .filter(User::isActive)
                .map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .filter(User::isActive)
                .map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> getUserByKeycloakId(String keycloakId) {
        return userRepository.findByKeycloakId(keycloakId)
                .filter(User::isActive)
                .map(userMapper::toDto);
    }

    @Override
    public UserDto updateUser(UUID id, UserDto userDto) {
        User existingUser = userRepository.findById(id)
                .filter(User::isActive)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        // Validate uniqueness for updated fields
        if (!existingUser.getUsername().equals(userDto.getUsername()) && 
            existsByUsername(userDto.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + userDto.getUsername());
        }
        if (!existingUser.getEmail().equals(userDto.getEmail()) && 
            existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + userDto.getEmail());
        }

        userMapper.updateEntity(existingUser, userDto);
        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDto(updatedUser);
    }

    @Override
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }

    @Override
    public void softDeleteUser(UUID id) {
        User user = userRepository.findById(id)
                .filter(User::isActive)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        
        user.deactivate();
        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
    }

    @Override
    public void activateUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        
        user.activate();
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
    }

    @Override
    public void deactivateUser(UUID id) {
        User user = userRepository.findById(id)
                .filter(User::isActive)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        
        user.deactivate();
        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAllActiveUsers(pageable)
                .map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> getActiveUsers(Pageable pageable) {
        return userRepository.findByStatusAndActive(UserStatus.ACTIVE, pageable)
                .map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> getUsersByStatus(UserStatus status, Pageable pageable) {
        return userRepository.findByStatusAndActive(status, pageable)
                .map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> searchUsers(String searchTerm, Pageable pageable) {
        return userRepository.searchActiveUsers(searchTerm, pageable)
                .map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsersByDepartment(String department) {
        return userMapper.toDtoList(userRepository.findByDepartmentAndActive(department));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsersByPosition(String position) {
        return userMapper.toDtoList(userRepository.findByPositionAndActive(position));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsersByRole(String roleCode) {
        return userMapper.toDtoList(userRepository.findByRoleCodeAndActive(roleCode));
    }

    @Override
    public void lockUser(UUID id, LocalDateTime until) {
        User user = userRepository.findById(id)
                .filter(User::isActive)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        
        user.lock(until);
        userRepository.save(user);
    }

    @Override
    public void unlockUser(UUID id) {
        User user = userRepository.findById(id)
                .filter(User::isActive)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        
        user.unlock();
        userRepository.save(user);
    }

    @Override
    public void incrementLoginAttempts(UUID id) {
        User user = userRepository.findById(id)
                .filter(User::isActive)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        
        user.incrementLoginAttempts();
        userRepository.save(user);
    }

    @Override
    public void resetLoginAttempts(UUID id) {
        User user = userRepository.findById(id)
                .filter(User::isActive)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        
        user.resetLoginAttempts();
        userRepository.save(user);
    }

    @Override
    public void updateLastLogin(UUID id) {
        User user = userRepository.findById(id)
                .filter(User::isActive)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        
        user.updateLastLogin();
        userRepository.save(user);
    }

    @Override
    public void verifyEmail(UUID id) {
        User user = userRepository.findById(id)
                .filter(User::isActive)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        
        user.verifyEmail();
        userRepository.save(user);
    }

    @Override
    public void assignRole(UUID userId, UUID roleId) {
        User user = userRepository.findById(userId)
                .filter(User::isActive)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        
        Role role = roleRepository.findById(roleId)
                .filter(Role::isActive)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + roleId));
        
        user.addRole(role);
        userRepository.save(user);
    }

    @Override
    public void assignRoles(UUID userId, List<UUID> roleIds) {
        User user = userRepository.findById(userId)
                .filter(User::isActive)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        
        for (UUID roleId : roleIds) {
            Role role = roleRepository.findById(roleId)
                    .filter(Role::isActive)
                    .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + roleId));
            user.addRole(role);
        }
        
        userRepository.save(user);
    }

    @Override
    public void removeRole(UUID userId, UUID roleId) {
        User user = userRepository.findById(userId)
                .filter(User::isActive)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + roleId));
        
        user.removeRole(role);
        userRepository.save(user);
    }

    @Override
    public void removeAllRoles(UUID userId) {
        User user = userRepository.findById(userId)
                .filter(User::isActive)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        
        user.getRoles().clear();
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByKeycloakId(String keycloakId) {
        return userRepository.existsByKeycloakId(keycloakId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countUsersByStatus(UserStatus status) {
        return userRepository.countByStatusAndActive(status);
    }

    @Override
    @Transactional(readOnly = true)
    public long countUsersCreatedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return userRepository.countUsersCreatedBetween(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getUserCountByDepartment() {
        return userRepository.countUsersByDepartment();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findUsersNotLoggedInSince(LocalDateTime date) {
        return userMapper.toDtoList(userRepository.findUsersNotLoggedInSince(date));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findUnverifiedUsersCreatedBefore(LocalDateTime date) {
        return userMapper.toDtoList(userRepository.findUnverifiedUsersCreatedBefore(date));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findLockedUsers() {
        return userMapper.toDtoList(userRepository.findLockedUsers());
    }
}